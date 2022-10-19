package com.capital.composesample.ui.view.login

class LoginContract {
    data class State(
        val loginId: String,
        val loginToken: String,
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object LoginSuccess: Effect()
        object LoginFail: Effect()
    }
}