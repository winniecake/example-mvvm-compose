A simple project based on Kotlin MVVM architecture and compose

## Features
1. Contact Information List
2. Message Information List (A list of expandable cards)
3. To-Do List (allow to add/edit/delete item)
4. Bottom Navigation
5. Multi Floating Action Button
6. Side Menu

## Tech/Tools
* [Kotlin](https://kotlinlang.org/) 100% coverage
* [Jetpack](https://developer.android.com/jetpack)
    * [Compose](https://developer.android.com/jetpack/compose) 
    * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) for navigation between composables
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that stores, exposes and manages UI state
    * [MutableState](https://developer.android.com/jetpack/compose/state)
    * [Drawers](https://developer.android.com/jetpack/compose/layouts/material#drawers)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-jetpack) for dependency injection
* [Moshi](https://github.com/square/moshi) parse JSON into Kotlin classes
* Implementation of [Multi Floating Action Button.kt](https://github.com/winniecake/example-mvvm-compose/blob/master/app/src/main/java/com/capital/composesample/ui/view/MultiFloatingActionButton.kt)

<img src="readme/demo_side_menu.gif" width="256" height="540" hspace="10" vspace="10">  <img src="readme/demo_contacts_messages_list.gif" width="256" height="540" hspace="10" vspace="10">  <img src="readme/demo_todo_list.gif" width="256" height="540" hspace="10" vspace="10">

## Architecture

![image](https://github.com/winniecake/example-mvvm-compose/blob/master/readme/structure.png)
