package com.capital.composesample.ui.view

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Contacts: NavigationItem("contacts", android.R.drawable.ic_dialog_email, "Contacts")
    object Messages: NavigationItem("messages", android.R.drawable.ic_dialog_info, "Messages")
    object TODOLists: NavigationItem("todo", android.R.drawable.ic_dialog_dialer, "To-Do List")
}