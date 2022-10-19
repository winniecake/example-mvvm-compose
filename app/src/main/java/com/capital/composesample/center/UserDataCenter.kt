package com.capital.composesample.center


class UserDataCenter private constructor() {

    var mUserId: String = ""
    var mToken: String = ""

    companion object {
        private var mInstance: UserDataCenter? = null
        val instance: UserDataCenter
            get() {
                if (mInstance == null)
                    mInstance = UserDataCenter()
                return mInstance!!
            }
    }

    fun freeInstance() {
        mInstance = null
    }

}