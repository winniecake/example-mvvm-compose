package com.capital.composesample.ui.view.home.messages

import com.capital.composesample.model.data.MessagesInfo

class MessagesContract {
    data class State(
        val messageInfo: List<MessagesInfo> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded: Effect()
    }
}