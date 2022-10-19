package com.capital.composesample.ui.view.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capital.composesample.center.UserDataCenter
import com.capital.composesample.model.MainRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val mRepository = MainRepository()

    var state by mutableStateOf(
        LoginContract.State(
            loginId = "",
            loginToken = "",
            isLoading = false
        )
    )

    var effects = Channel<LoginContract.Effect> {UNLIMITED}

    fun loginAPI(id: String, pwd: String) {
        requestLoginAPI(id, pwd)
    }

    // 查詢使用者資料
    private fun requestLoginAPI(id: String, pwd: String) {
        viewModelScope.launch {
            state = state.copy(loginId = "", loginToken = "", isLoading = true)
            val loginData = mRepository.requestLoginAPI(id, pwd)
            if (loginData?.id != null && loginData.token != null) {
                UserDataCenter.instance.mUserId = state.loginId
                UserDataCenter.instance.mToken = state.loginToken
                state = state.copy(loginId = loginData.id, loginToken = loginData.token, isLoading = false)
                effects.send(LoginContract.Effect.LoginSuccess)
            } else {
                state = state.copy(isLoading = false)
                effects.send(LoginContract.Effect.LoginFail)
            }
        }
    }
}