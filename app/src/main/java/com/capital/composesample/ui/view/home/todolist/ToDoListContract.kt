package com.capital.composesample.ui.view.home.todolist

import com.capital.composesample.model.data.ToDoInfo

class ToDoListContract {
    data class State(
        val todoInfo: List<ToDoInfo> = mutableListOf(),
        val editMode: ToDoListEditMode = ToDoListEditMode.Normal,
        val isLoading: Boolean = false,
        val isShowNewDialog: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded: Effect()
    }
}

enum class ToDoListEditMode {
    Normal,
    Add,
    Edit,
    Delete
}