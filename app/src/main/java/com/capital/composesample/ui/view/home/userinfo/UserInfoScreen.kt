package com.capital.composesample.ui.view.home.userinfo

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capital.composesample.R
import com.capital.composesample.center.UserDataCenter
import com.capital.composesample.ui.theme.ComposeSampleTheme
import com.capital.composesample.ui.view.home.contacts.ContactsContract
import com.capital.composesample.ui.view.home.contacts.ContactsScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun UserInfoScreen(
    state: UserInfoContract.State,
    effectFlow: Flow<UserInfoContract.Effect>?
) {
    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow?.onEach { effect ->
            if (effect is UserInfoContract.Effect.DataWasLoaded) {
                Toast.makeText(context, "user info is loaded", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape)
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
        )

        Text(
            "${state.userInfo.name}",
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        Text(
            "(ID: ${state.userInfo.id})",
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .padding(10.dp)
        )

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(start = 25.dp, end = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_send),
                    contentDescription = "email",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                )

                Text(
                    "${state.userInfo.email}",
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_call),
                    contentDescription = "phone number",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                )

                Text(
                    "${state.userInfo.phone}",
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }
        }

        Button(
            onClick = {
                UserDataCenter.instance.freeInstance()
                (context as? Activity)?.finish()
            },
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
            ),
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(20.dp)
        ) {
            Text("Logout")
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UserInfoScreen(
        UserInfoContract.State(),
        null)
}