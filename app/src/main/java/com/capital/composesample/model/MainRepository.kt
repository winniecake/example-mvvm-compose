package com.capital.composesample.model

import android.util.Log
import com.capital.composesample.model.data.ToDoInfo
import com.capital.composesample.model.response.*
import com.squareup.moshi.Moshi
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor() {

    suspend fun requestLoginAPI(id: String, password: String): LoginData? {
        Log.i("MainRepository", "requestLoginAPI...")
        delay(2000L)
        val jsonStr = "{\"code\":\"000\",\"msg\":\"login success\"," +
                "\"data\":{\"id\":\"A123456789\",\"token\":\"a52216c4-7210-4205-8dee-773a9896d7cd\"}}"

        var loginData: LoginData? = null
        try {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(LoginAPIResponse::class.java)
            val reportData = jsonAdapter.fromJson(jsonStr)
            if (reportData?.code != null) {
                when (reportData.code) {
                    "000" -> {
                        loginData = reportData.data
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return loginData
    }

    suspend fun requestUserInfoAPI(id: String, token: String): UserData? {
        Log.i("MainRepository", "requestUserInfoAPI...")
        delay(2000L)
        val jsonStr = "{\"code\":\"000\"," +
                "\"data\":{\"id\":\"A123456789\",\"name\":\"Anny Li\",\"gender\":\"female\"," +
                "\"birthday\":\"2022-09-30\",\"email\":\"annyli12345@mail.com\",\"phone\":\"02-1234567\"}}"

        var userData: UserData? = null
        try {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(UserInfoAPIResponse::class.java)
            val reportData = jsonAdapter.fromJson(jsonStr)
            if (reportData?.code != null) {
                when (reportData.code) {
                    "000" -> {
                        userData = reportData.data
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return userData
    }

    suspend fun requestUserContactsAPI(id: String, token: String): List<ContactsData>?{
        Log.i("MainRepository", "requestUserContactsAPI...")
        delay(2000L)
        val jsonStr = "{\"code\":\"000\",\"data\":[\n" +
                "{\"id\":1, \"name\":\"Elaine Hsieh\",\"gender\":\"female\",\"email\":\"elaine123@mail.com\",\"phone\":\"03-1234567\"},\n" +
                "{\"id\":2, \"name\":\"Mary Wang\",\"gender\":\"female\",\"email\":\"mary123@mail.com\",\"phone\":\"03-1234567\"},\n" +
                "{\"id\":3, \"name\":\"Jack Lee\",\"gender\":\"male\",\"email\":\"jack123@mail.com\",\"phone\":\"03-1234567\"},\n" +
                "{\"id\":4, \"name\":\"Janny Su\",\"gender\":\"female\",\"email\":\"janny123@mail.com\",\"phone\":\"03-1234567\"},\n" +
                "{\"id\":5, \"name\":\"Ray Kuo\",\"gender\":\"male\",\"email\":\"ray123@mail.com\",\"phone\":\"03-1234567\"},\n" +
                "{\"id\":6, \"name\":\"Nick Wang\",\"gender\":\"male\",\"email\":\"nick123@mail.com\",\"phone\":\"03-1234567\"},\n" +
                "{\"id\":7, \"name\":\"Amy Lee\",\"gender\":\"female\",\"email\":\"amy123@mail.com\",\"phone\":\"03-1234567\"},\n" +
                "{\"id\":8, \"name\":\"Max Hsieh\",\"gender\":\"male\",\"email\":\"max123@mail.com\",\"phone\":\"03-1234567\"},\n" +
                "{\"id\":9, \"name\":\"John Hsieh\",\"gender\":\"male\",\"email\":\"john123@mail.com\",\"phone\":\"03-1234567\"},\n" +
                "{\"id\":10, \"name\":\"Oliver Su\",\"gender\":\"male\",\"email\":\"oliver123@mail.com\",\"phone\":\"03-1234567\"}\n" +
                "]}"

        var contactsDatas: List<ContactsData>? = null
        try {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(ContactsAPIResponse::class.java)
            val reportData = jsonAdapter.fromJson(jsonStr)
            if (reportData?.code != null) {
                when (reportData.code) {
                    "000" -> {
                        contactsDatas = reportData.data
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return  contactsDatas
    }

    suspend fun requestUserMessagesAPI(id: String, token: String): List<MessagesData>? {
        Log.i("MainRepository", "requestUserMessagesAPI...")
        delay(2000L)
        val jsonStr = "{\"code\":\"000\",\"data\":[" +
                "{\"title\":\"Welcome!\",\"content\":\"this is the test message!\nI’d love to hear what you think of this simple project and if there is anything we can improve.\nKeeping an eye on the recent developments and constantly analyze the performance\",\"time\":\"2022-10-02\"}," +
                "{\"title\":\"Happy Birthday! Anny!\",\"content\":\"Wishing You A Very Happy Birthday. Celebrate with a FREE gift from QQ shop. Hope you love this little surprise.\",\"time\":\"2022-10-19\"}," +
                "{\"title\":\"Plan A Meeting Schedule\",\"content\":\"Dear Anny,\nMy name is Elaine. Please let me know which times and dates are best for you.\nThank you very much.\",\"time\":\"2022-10-21\"}]}"

        var messageDatas: List<MessagesData>? = null
        try {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(MessagesAPIResponse::class.java)
            val reportData = jsonAdapter.fromJson(jsonStr)
            if (reportData?.code != null) {
                when (reportData.code) {
                    "000" -> {
                        messageDatas = reportData.data
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return messageDatas
    }

    suspend fun getUserToDoListAPI(id: String, token: String): List<ToDoInfo>{
        Log.i("MainRepository","getUserToDoListAPI...")
        delay(2000L)
        val list: ArrayList<ToDoInfo> = arrayListOf()
        list.add(ToDoInfo("0", "Design Gaming AD","deadline 10/21", false))
        list.add(ToDoInfo("1", "Clean Living Room","", false))
        list.add(ToDoInfo("2", "Monthly Report","to Nick", false))
        list.add(ToDoInfo("3", "Daily Report","to Amy", false))
        list.add(ToDoInfo("4", "Lunch","11:30", false))
        return list
    }
}
