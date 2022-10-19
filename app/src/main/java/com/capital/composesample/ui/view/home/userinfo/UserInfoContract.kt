package com.capital.composesample.ui.view.home.userinfo

import com.capital.composesample.model.data.UserInfo

class UserInfoContract {
    data class State(
        val userInfo: UserInfo = UserInfo(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded: Effect()
    }
}