package com.capital.composesample.ui.view.home.userinfo

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capital.composesample.model.MainRepository
import com.capital.composesample.model.data.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val repository: MainRepository,
): ViewModel() {

    var state by mutableStateOf(
        UserInfoContract.State(
            userInfo = UserInfo(),
            isLoading = false
        )
    )

    var effects = Channel<UserInfoContract.Effect> { Channel.UNLIMITED }

    init {
        val id = stateHandle.get<String>("USER_ID")
        val token = stateHandle.get<String>("USER_TOKEN")
        if (id != null && token != null) {
            requestUserInfoAPI(id, token)
        }
    }

    // 查詢使用者資料
    private fun requestUserInfoAPI(id: String, token: String) {
        viewModelScope.launch {
            state = state.copy(userInfo = UserInfo(), isLoading = true)
            val userData = repository.requestUserInfoAPI(id, token)
            if (userData != null) {
                val userInfo = UserInfo()
                userInfo.id = userData.id
                userInfo.name = userData.name
                userInfo.gender = userData.gender
                userInfo.birthday = userData.birthday
                userInfo.email = userData.email
                userInfo.phone = userData.phone
                state = state.copy(userInfo = userInfo, isLoading = false)
                effects.send(UserInfoContract.Effect.DataWasLoaded)
            } else {
                state = state.copy(isLoading = false)
            }
        }
    }
}