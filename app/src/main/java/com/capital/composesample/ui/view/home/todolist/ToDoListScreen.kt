package com.capital.composesample.ui.view.home.todolist

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.capital.composesample.model.data.ToDoInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun ToDoListScreen(
    state: ToDoListContract.State,
    effectFlow: Flow<ToDoListContract.Effect>?,
    onRequestAction: (item: ToDoInfo) -> Unit,
    onShowDialog: (isShow: Boolean) -> Unit
) {
    val context = LocalContext.current
    val isShowEditDialog = remember { mutableStateOf(false) }
    val editItem = remember { mutableStateOf(ToDoInfo())}

    LaunchedEffect(effectFlow) {
        effectFlow?.onEach { effect ->
            if (effect is ToDoListContract.Effect.DataWasLoaded) {
                Toast.makeText(context, "todo list is loaded", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LazyColumn {
        items(
            items = state.todoInfo,
            itemContent = {
                ToDoListItem(
                    item = it, state.editMode,
                    onItemClick = { item ->
                        when(state.editMode) {
                            ToDoListEditMode.Normal -> {}
                            ToDoListEditMode.Delete -> { onRequestAction(item) }
                            ToDoListEditMode.Add -> {}
                            ToDoListEditMode.Edit -> {
                                editItem.value = item
                                isShowEditDialog.value = true
                            }
                        }
                    }
                )
            }
        )
    }

    Box {
        if (state.isLoading) {
            LoadingBar()
        }

        if(state.isShowNewDialog) {
            ItemDialog(
                mode = ToDoListEditMode.Add,
                no = state.todoInfo.size.toString(),
                setShowDialog = { show ->
                    onShowDialog(show)
                },
                setValue = { no, title, content ->
                    onRequestAction(ToDoInfo(no, title, content, false))
                }
            )
        }

        if(isShowEditDialog.value) {
            ItemDialog(
                mode = ToDoListEditMode.Edit,
                no = editItem.value.no!!,
                title = editItem.value.title!!,
                content = editItem.value.content!!,
                setShowDialog = { show ->
                    isShowEditDialog.value = show
                },
                setValue = { no, title, content ->
                    onRequestAction(ToDoInfo(no, title, content, false))
                }
            )
        }
    }
}

@Composable
fun ToDoListItem(item: ToDoInfo, mode: ToDoListEditMode, onItemClick: (item: ToDoInfo) -> Unit) {
    var isChecked by remember { mutableStateOf(item.check) }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
    ){
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (mode) {
                ToDoListEditMode.Normal -> {
                    Checkbox(
                        checked = isChecked!!,
                        onCheckedChange = { checked ->
                            isChecked = checked
                            item.check = isChecked
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primarySurface,
                            uncheckedColor = MaterialTheme.colors.primary
                        )
                    )
                }
                ToDoListEditMode.Delete -> {
                    IconButton(
                        onClick = {
                            onItemClick(item)
                        }
                    ) {
                        Icon(Icons.Filled.Delete,
                            contentDescription = "delete",
                            tint = MaterialTheme.colors.primaryVariant)
                    }
                }
                ToDoListEditMode.Edit -> {
                    IconButton(
                        onClick = {
                            onItemClick(item)
                        }
                    ) {
                        Icon(Icons.Filled.Edit,
                            contentDescription = "edit",
                            tint = MaterialTheme.colors.primaryVariant)
                    }
                }
                ToDoListEditMode.Add -> {}
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(start = 5.dp)
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    text = "${item.title}",
                    fontSize = 20.sp,
                    color = if (isChecked == true) Color.LightGray else Color.Black
                )

                if (item.content?.isNotEmpty() == true) {
                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        text = "${item.content}",
                        fontSize = 16.sp,
                        color = if (isChecked == true) Color.LightGray else Color.Gray
                    )
                }
            }
        }

        Divider(
            color = Color.LightGray,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Composable
fun ItemDialog(
    mode: ToDoListEditMode,
    no: String,
    title: String = "",
    content: String = "",
    setShowDialog: (Boolean) -> Unit,
    setValue: (tag: String, title: String, content: String) -> Unit) {

    val txtFieldError = remember { mutableStateOf("") }
    val titleTextField = remember { mutableStateOf(title) }
    val contentTextField = remember { mutableStateOf(content) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text =
                        when (mode) {
                            ToDoListEditMode.Add -> { "New" }
                            ToDoListEditMode.Edit -> { "Edit" }
                            else -> { "" }
                        },
                        style = TextStyle(fontSize = 24.sp,)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.primary
                                ),
                                shape = RoundedCornerShape(25)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        },
                        placeholder = { Text(text = "Enter title") },
                        value = titleTextField.value,
                        onValueChange = {
                            titleTextField.value = it.take(20)
                        })

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.primary
                                ),
                                shape = RoundedCornerShape(25)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        },
                        placeholder = { Text(text = "Enter content") },
                        value = contentTextField.value,
                        onValueChange = {
                            contentTextField.value = it.take(20)
                        })

                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth()
                            .padding(2.dp),
                        text = txtFieldError.value,
                        style = TextStyle(fontSize = 16.sp,),
                        color = Color.Red
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                setShowDialog(false)
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
                            Text("CANCEL")
                        }

                        Button(
                            onClick = {
                                if (titleTextField.value.isEmpty()) {
                                    txtFieldError.value = "title can not be empty"
                                    return@Button
                                }
                                setValue(no, titleTextField.value, contentTextField.value)
                                setShowDialog(false)
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
                            Text("OK")
                        }
                    }
                }
            }
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
fun DefaultPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.surface
    ) {
        var state = ToDoListContract.State()
        state = state.copy(
            todoInfo = listOf(ToDoInfo("0", "待辦事項文字1", "待辦事項內文1", false)),
            isLoading = false)
        ToDoListScreen(state, null, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun NewItemDialogPreview() {
    ItemDialog(ToDoListEditMode.Add,"","","",{},{ _, _, _->})
}