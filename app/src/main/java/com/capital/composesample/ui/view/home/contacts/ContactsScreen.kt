package com.capital.composesample.ui.view.home.contacts

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capital.composesample.model.data.ContactsInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun ContactsScreen(
    state: ContactsContract.State,
    effectFlow: Flow<ContactsContract.Effect>?
) {
    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow?.onEach { effect ->
            if (effect is ContactsContract.Effect.DataWasLoaded) {
                Toast.makeText(context, "contacts is loaded", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LazyColumn {
        items(
            items = state.contactsInfo,
            itemContent = {
                ContactListItem(item = it)
            }
        )
    }

    Box {
        if (state.isLoading) {
            LoadingBar()
        }
    }
}

@Composable
fun ContactListItem(item: ContactsInfo) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
    ){
        Text(
            text = "${item.name}",
            fontSize = 20.sp,
            color = Color.DarkGray
        )
        Text(
            text = "${item.email}",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            text = "${item.phone}",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .padding(top = 20.dp)
        )

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
        var state = ContactsContract.State()
        state = state.copy(
            contactsInfo = listOf(ContactsInfo(
                id = 0,
                name = "name",
                gender = "gender",
                email = "email",
                phone = "phone"
            )),
            isLoading = false)
        ContactsScreen(state, null)
    }
}