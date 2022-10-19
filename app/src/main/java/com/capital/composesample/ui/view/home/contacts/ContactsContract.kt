package com.capital.composesample.ui.view.home.contacts

import com.capital.composesample.model.data.ContactsInfo

class ContactsContract {
    data class State(
        val contactsInfo: List<ContactsInfo> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded: Effect()
    }
}