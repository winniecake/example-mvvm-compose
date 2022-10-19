package com.capital.composesample.ui.view.home.todolist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capital.composesample.model.MainRepository
import com.capital.composesample.model.data.ToDoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel@Inject constructor(
    stateHandle: SavedStateHandle,
    private val repository: MainRepository
): ViewModel() {

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
        val id = stateHandle.get<String>("USER_ID")
        val token = stateHandle.get<String>("USER_TOKEN")
        if (id != null && token != null) {
            requestToDoListAPIAPI(id, token)
        }
    }

    private fun requestToDoListAPIAPI(id: String, token: String) {
        viewModelScope.launch {
            state = state.copy(editMode = ToDoListEditMode.Normal, todoInfo = listOf(), isLoading = true, isShowNewDialog = false)
            val todoList = repository.getUserToDoListAPI(id, token)
            state = state.copy(editMode = ToDoListEditMode.Normal, todoInfo = todoList, isLoading = false, isShowNewDialog = false)
        }
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
