package com.capital.composesample.center

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException

class UserSettingCenter private constructor() {
    private var mEncryptedSharedPreferences: EncryptedSharedPreferences? = null

    companion object {
        private var mInstance: UserSettingCenter? = null
        val instance: UserSettingCenter
            get() {
                if (mInstance == null)
                    mInstance = UserSettingCenter()
                return mInstance!!
            }
    }

    fun freeInstance() {
        mInstance = null
    }

    fun setSharedPreferences(context: Context) {
        try {
            mEncryptedSharedPreferences = getMasterKey(context)?.let { key ->
                EncryptedSharedPreferences.create(
                    context,
                    "com.capital.composesample",
                    key,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } as EncryptedSharedPreferences
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getMasterKey(context: Context): MasterKey? {
        try {
            return MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun setValue(strKey: String, Value: String) {
        mEncryptedSharedPreferences?.let {
            val editor = it.edit()
            editor.putString(strKey, Value)
            editor.apply()
        }
    }

    private fun setValue(strKey: String, Value: Boolean) {
        mEncryptedSharedPreferences?.let {
            val editor = it.edit()
            editor.putBoolean(strKey, Value)
            editor.apply()
        }
    }

    private fun getString(strKey: String, strDefault: String): String? {
        var strValue: String? = null
        mEncryptedSharedPreferences?.let {
            strValue = it.getString(strKey, strDefault)
        }
        return strValue
    }

    fun getBoolean(strKey: String, bDefault: Boolean): Boolean? {
        var bValue = bDefault
        mEncryptedSharedPreferences?.let {
            bValue = it.getBoolean(strKey, bDefault)
        }
        return bValue
    }

    fun setUserID(ID: String) {
        setValue("UserID", ID)
    }

    fun getUserID(): String? {
        return getString("UserID", "")
    }

    fun setRememberID(remember: Boolean) {
        setValue("RememberID", remember)
    }

    fun getRememberID(): Boolean? {
        return getBoolean("RememberID", false)
    }
}