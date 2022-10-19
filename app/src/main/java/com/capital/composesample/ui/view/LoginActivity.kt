package com.capital.composesample.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capital.composesample.center.UserSettingCenter
import com.capital.composesample.ui.theme.ComposeSampleTheme
import com.capital.composesample.ui.view.login.LoginContract
import com.capital.composesample.ui.view.login.LoginViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserSettingCenter.instance.apply {
            setSharedPreferences(applicationContext)
        }
        setContent {
            ComposeSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContent() {
    val context = LocalContext.current
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val vm = LoginViewModel()
    val effectFlow: Flow<LoginContract.Effect> = vm.effects.receiveAsFlow()
    LaunchedEffect(effectFlow) {
        effectFlow.onEach { effect ->
            if (effect is LoginContract.Effect.LoginSuccess) {
                // 跳轉頁面
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
            } else if (effect is LoginContract.Effect.LoginFail) {
                Toast.makeText(context, "login fail", Toast.LENGTH_SHORT).show()
            }
        }.collect()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            LoginContent { id, pwd, isRemember ->
                // 點擊登入按鈕callback id, password
                if (id.isEmpty()) {
                    Toast.makeText(context, "please input id", Toast.LENGTH_SHORT).show()

                } else if (pwd.isEmpty()) {
                    Toast.makeText(context, "please input password", Toast.LENGTH_SHORT).show()

                } else {
                    // 儲存ID
                    UserSettingCenter.instance.setRememberID(isRemember)
                    if (isRemember) {
                        UserSettingCenter.instance.setUserID(id)
                    } else {
                        UserSettingCenter.instance.setUserID("")
                    }
                    // 開始登入
                    vm.loginAPI(id, pwd)
                }
            }

            Box {
                if (vm.state.isLoading) {
                    LoadingBar()
                }
            }
        }
    )
}

@Composable
fun LoginContent(onLoginClick: (id: String, pwd: String, isRemember: Boolean) -> Unit) {
    // 讀取記憶ID設定
    var account = ""
    UserSettingCenter.instance.getUserID()?.let {
        account = it
    }

    var remember = false
    UserSettingCenter.instance.getRememberID()?.let {
        remember = it
    }

    var accountInput by remember { mutableStateOf(account) }
    var passwordInput by remember { mutableStateOf("") }
    var isRememberID by remember { mutableStateOf(remember) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        // -------- 標題 --------
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )
                ) {
                    append("Login")
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Center
        )

        // -------- 帳號輸入 --------
        TextField(
            value = accountInput,
            onValueChange = { accountInput = it.uppercase()},
            label = { Text(text = "UserID") },
            maxLines = 1,
            trailingIcon = {
                Icon(Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier.clickable { accountInput = "" })
            },
            //keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        )

        // -------- 密碼輸入 --------
        TextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Password") },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                Icon(Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier.clickable { passwordInput = "" })
            },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        )

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,

            ) {
            Checkbox(
                checked = isRememberID,
                onCheckedChange = { checked ->
                    isRememberID = checked
                    if (!isRememberID) {
                        UserSettingCenter.instance.setRememberID(false)
                        UserSettingCenter.instance.setUserID("")
                    }
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primarySurface,
                    uncheckedColor = MaterialTheme.colors.primary
                )
            )

            Text("remember me")
        }

        // -------- 登入按鈕 --------
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    accountInput = ""
                    passwordInput = ""
                    isRememberID = false
                },
                contentPadding = PaddingValues(
                    start = 5.dp,
                    end = 5.dp,
                ),
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(0.5f)
                    .padding(horizontal = 5.dp)
            ) {
                Text("Clear data")
            }

            Button(
                onClick = {
                    onLoginClick(accountInput, passwordInput, isRememberID)


                          },
                contentPadding = PaddingValues(
                    start = 5.dp,
                    end = 5.dp,
                ),
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(0.5f)
                    .padding(horizontal = 5.dp),
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Login")
            }
        }

        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .padding(15.dp)
        )

        Text(
            text ="A Simple Practice for Jetpack Compose\n2022/10/01 by Elaine",
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.Gray)



        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val githubLinkString = buildAnnotatedString {
                val str = "download source code from github"
                val startIndex = str.indexOf("source")
                val endIndex = startIndex + 11

                append(str)
                addStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                    ),
                    start = startIndex,
                    end = endIndex
                )

                addStringAnnotation(
                    tag = "URL",
                    annotation = "https://github.com/winniecake/example-mvvm-compose",
                    start = startIndex,
                    end = endIndex
                )
            }

            val uriHandler = LocalUriHandler.current

            ClickableText(
                text = githubLinkString,
                style = TextStyle(textAlign = TextAlign.Center),
                onClick = {
                    githubLinkString
                        .getStringAnnotations("URL", it, it)
                        .firstOrNull()?.let { stringAnnotation ->
                            uriHandler.openUri(stringAnnotation.item)
                        }
                }
            )
        }
    }
}

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}


@Preview(showBackground = true)
@Composable
fun LoginDefaultPreview() {
    ComposeSampleTheme {
        MainContent()
    }
}