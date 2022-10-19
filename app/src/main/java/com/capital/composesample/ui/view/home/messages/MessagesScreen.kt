package com.capital.composesample.ui.view.home.messages

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capital.composesample.model.data.MessagesInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun MessagesScreen(
    state: MessagesContract.State,
    effectFlow: Flow<MessagesContract.Effect>?
) {
    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow?.onEach { effect ->
            if (effect is MessagesContract.Effect.DataWasLoaded) {
                Toast.makeText(context, "messages is loaded", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LazyColumn {
        items(
            items = state.messageInfo,
            itemContent = {
//                MessageListItem(item = it)
                MessageItem(item = it, itemShouldExpand = true)
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
fun MessageItem(item: MessagesInfo,
                itemShouldExpand: Boolean = false) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        Row(modifier = Modifier.animateContentSize()) {
            MessagesInfoDetail(
                item = item,
                expandedLines = if (expanded) 10 else 2,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth(0.80f)
                    .align(Alignment.CenterVertically)
            )
            if (itemShouldExpand)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable { expanded = !expanded }
                ) {
                    ExpandableContentIcon(expanded)
                }
        }
    }
}


@Composable
private fun ExpandableContentIcon(expanded: Boolean) {
    Icon(
        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
        contentDescription = "Expand row icon",
        modifier = Modifier.padding(all = 16.dp)
    )
}

@Composable
fun MessagesInfoDetail(
    item: MessagesInfo?,
    expandedLines: Int,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = item?.title ?: "--",
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            color = Color.DarkGray,
        )

        Text(
            text = item?.time ?: "--",
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = item?.content ?: "--",
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = expandedLines,
            modifier = Modifier.padding(vertical = 10.dp)
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
        var state = MessagesContract.State()
        state = state.copy(
            messageInfo = listOf(MessagesInfo("title", "content", "time")),
            isLoading = false)
        MessagesScreen(state, null)
    }
}