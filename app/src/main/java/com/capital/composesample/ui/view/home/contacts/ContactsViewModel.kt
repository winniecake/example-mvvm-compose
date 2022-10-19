package com.capital.composesample.ui.view.home.contacts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capital.composesample.model.data.ContactsInfo
import com.capital.composesample.model.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel@Inject constructor(
    stateHandle: SavedStateHandle,
    private val repository: MainRepository,
): ViewModel() {

    var state by mutableStateOf(
        ContactsContract.State(
            contactsInfo = listOf(),
            isLoading = false
        )
    )

    var effects = Channel<ContactsContract.Effect> { Channel.UNLIMITED }

    init {
        val id = stateHandle.get<String>("USER_ID")
        val token = stateHandle.get<String>("USER_TOKEN")
        if (id != null && token != null) {
            requestContactsInfoAPI(id, token)
        }
    }

    // 查詢聯絡人
    private fun requestContactsInfoAPI(id: String, token: String) {
        viewModelScope.launch {
            state = state.copy(contactsInfo = listOf(), isLoading = true)
            val contactsDataList = repository.requestUserContactsAPI(id, token)
            if (contactsDataList != null) {
                val list: ArrayList<ContactsInfo> = arrayListOf()
                for (i in contactsDataList.indices) {
                    val contact = ContactsInfo(
                        id = contactsDataList[i].id,
                        name = contactsDataList[i].name,
                        gender = contactsDataList[i].gender,
                        email = contactsDataList[i].email,
                        phone = contactsDataList[i].phone
                    )
                    list.add(contact)
                }
                state = state.copy(contactsInfo = list, isLoading = false)
                effects.send(ContactsContract.Effect.DataWasLoaded)
            } else {
                state = state.copy(isLoading = false)
            }
        }
    }
}