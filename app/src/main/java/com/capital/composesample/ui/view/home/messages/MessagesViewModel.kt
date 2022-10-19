package com.capital.composesample.ui.view.home.messages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capital.composesample.model.MainRepository
import com.capital.composesample.model.data.MessagesInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val repository: MainRepository,
): ViewModel() {

    var state by mutableStateOf(
        MessagesContract.State(
            messageInfo = listOf(),
            isLoading = false
        )
    )

    var effects = Channel<MessagesContract.Effect> { Channel.UNLIMITED }

    init {
        val id = stateHandle.get<String>("USER_ID")
        val token = stateHandle.get<String>("USER_TOKEN")
        if (id != null && token != null) {
            requestMessagesInfoAPI(id, token)
        }
    }

    // 查詢歷史訊息
    private fun requestMessagesInfoAPI(id: String, token: String) {
        viewModelScope.launch {
            state = state.copy(messageInfo = listOf(), isLoading = true)
            val messageDataList = repository.requestUserMessagesAPI(id, token)
            if (messageDataList != null) {
                val list: ArrayList<MessagesInfo> = arrayListOf()
                for (i in messageDataList.indices) {
                    val message = MessagesInfo(
                        title = messageDataList[i].title,
                        content = messageDataList[i].content,
                        time = messageDataList[i].time
                    )
                    list.add(message)
                }
                state = state.copy(messageInfo = list, isLoading = false)
                effects.send(MessagesContract.Effect.DataWasLoaded)
            } else {
                state = state.copy(isLoading = false)
            }

        }
    }
}