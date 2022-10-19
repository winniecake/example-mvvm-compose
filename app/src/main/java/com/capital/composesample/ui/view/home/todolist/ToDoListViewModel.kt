package com.capital.composesample.ui.view.home.todolist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.capital.composesample.model.data.ToDoInfo
import kotlinx.coroutines.channels.Channel

class ToDoListViewModel: ViewModel() {

    var state by mutableStateOf(
        ToDoListContract.State(
            todoInfo = mutableListOf(),
            editMode = ToDoListEditMode.Normal,
            isLoading = false,
            isShowNewDialog = false
        )
    )

    var effects = Channel<ToDoListContract.Effect> { Channel.UNLIMITED }

    init {
        val list: ArrayList<ToDoInfo> = arrayListOf()
        list.add(ToDoInfo("0", "Design Gaming AD","deadline 10/21", false))
        list.add(ToDoInfo("1", "Clean Living Room","", false))
        list.add(ToDoInfo("2", "Monthly Report","to Nick", false))
        list.add(ToDoInfo("3", "Daily Report","to Amy", false))
        list.add(ToDoInfo("4", "Lunch","11:30", false))
        state = state.copy(editMode = ToDoListEditMode.Normal, todoInfo = list, isLoading = false)
    }

    fun setEditMode(mode: ToDoListEditMode) {
        state = state.copy(editMode = mode)
    }

    fun addNewItem(item: ToDoInfo) {
        val list: ArrayList<ToDoInfo> = ArrayList(state.todoInfo)
        list.add(item)
        state = state.copy(todoInfo = list)
    }

    fun deleteItem(item: ToDoInfo) {
        val list: ArrayList<ToDoInfo> = ArrayList(state.todoInfo)
        list.remove(item)
        for (i in 0 until list.size) {
            list[i].no = i.toString()
        }
        state = state.copy(todoInfo = list)
    }

    fun editItem(editItem: ToDoInfo) {
        val list: ArrayList<ToDoInfo> = ArrayList(state.todoInfo)
        for (i in 0 until list.size) {
            if (editItem.no.equals(list[i].no)) {
                list[i].title = editItem.title
                list[i].content = editItem.content
            }
        }
        state = state.copy(todoInfo = list)
    }

    fun setIsShowNewDialog(isShow: Boolean) {
        state = state.copy(isShowNewDialog = isShow)
    }

    fun printInfo(){
        state.todoInfo.forEach{ item ->
            Log.i("ToDoInfo", item.no + ", " + item.title + ", " + item.content + ", " + item.check)
        }
    }
}