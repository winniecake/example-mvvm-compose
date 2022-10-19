package com.capital.composesample.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capital.composesample.center.UserDataCenter
import com.capital.composesample.ui.theme.ComposeSampleTheme
import com.capital.composesample.ui.view.home.contacts.ContactsScreen
import com.capital.composesample.ui.view.home.contacts.ContactsViewModel
import com.capital.composesample.ui.view.home.messages.MessagesScreen
import com.capital.composesample.ui.view.home.messages.MessagesViewModel
import com.capital.composesample.ui.view.home.todolist.ToDoListEditMode
import com.capital.composesample.ui.view.home.todolist.ToDoListScreen
import com.capital.composesample.ui.view.home.todolist.ToDoListViewModel
import com.capital.composesample.ui.view.home.userinfo.UserInfoScreen
import com.capital.composesample.ui.view.home.userinfo.UserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.putExtra("USER_ID", UserDataCenter.instance.mUserId)
        intent.putExtra("USER_TOKEN", UserDataCenter.instance.mToken)

        setContent {
            ComposeSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface
                ) {
                    HomeContent()
                }
            }
        }
    }
}

@Composable
fun HomeContent() {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scaffoldState, navController) },
        drawerContent = { SideMenu() },
        drawerShape = MaterialTheme.shapes.small,
        floatingActionButton = { FloatingActionButton(navController) },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = false,
        bottomBar =  { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigation(navController = navController)
        }
    }
}

@Composable
fun TopBar(scaffoldState: ScaffoldState, navController: NavController) {
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route

    TopAppBar(
        title = {
            Text(
                text =
                when(route) {
                    NavigationItem.Contacts.route -> NavigationItem.Contacts.title
                    NavigationItem.Messages.route -> NavigationItem.Messages.title
                    NavigationItem.TODOLists.route -> NavigationItem.TODOLists.title
                    else -> ""
                },
                color = Color.White
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            IconButton(
                onClick = {
                    // 開啟選單
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            ) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "menu icon")
            }
        },
        actions = {
            when(route) {
                NavigationItem.Contacts.route -> {}
                NavigationItem.Messages.route -> {}
                NavigationItem.TODOLists.route -> {
                    val todoListViewModel: ToDoListViewModel = hiltViewModel()
                    if (todoListViewModel.state.editMode == ToDoListEditMode.Delete
                        || todoListViewModel.state.editMode == ToDoListEditMode.Edit) {
                        IconButton(
                            onClick = {
                                todoListViewModel.setEditMode(ToDoListEditMode.Normal)
                            }
                        ) {
                            Icon(Icons.Filled.Done,
                                contentDescription = "",
                                tint = Color.White)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FloatingActionButton(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when(navBackStackEntry?.destination?.route) {
        NavigationItem.Contacts.route -> {}
        NavigationItem.Messages.route -> {}
        NavigationItem.TODOLists.route -> { TODOListsFloatingActionButton() }
    }
}

@Composable
fun TODOListsFloatingActionButton(){
    val vm: ToDoListViewModel = hiltViewModel()
    if (vm.state.editMode == ToDoListEditMode.Normal) {
        MultiFloatingActionButton(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(end = 10.dp, bottom = 10.dp),
            mainButtonParam = MultiFabParam(
                tag = 0,
                buttonSize = 55.dp,
                iconSize = 25.dp,
                icon = Icons.Filled.Add,
                iconTintColor = Color.White,
                iconBackgroundColor = MaterialTheme.colors.primary,
                description = "打開選單"
            ),
            subButtonsParam = mutableListOf(
                MultiFabParam(
                    tag = 1,
                    buttonSize = 50.dp,
                    iconSize = 20.dp,
                    icon = Icons.Filled.Delete,
                    iconTintColor = Color.White,
                    iconBackgroundColor = MaterialTheme.colors.primary,
                    description = "刪除"
                ),
                MultiFabParam(
                    tag = 2,
                    buttonSize = 50.dp,
                    iconSize = 20.dp,
                    icon = Icons.Filled.Edit,
                    iconTintColor = Color.White,
                    iconBackgroundColor = MaterialTheme.colors.primary,
                    description = "編輯"
                ),
                MultiFabParam(
                    tag = 3,
                    buttonSize = 50.dp,
                    iconSize = 20.dp,
                    icon = Icons.Filled.Add,
                    iconTintColor = Color.White,
                    iconBackgroundColor = MaterialTheme.colors.primary,
                    description = "新增"
                )
            ),
            onItemClicked = {
                when (it.tag) {
                    0 -> {}
                    1 -> { vm.setEditMode(ToDoListEditMode.Delete) }
                    2 -> { vm.setEditMode(ToDoListEditMode.Edit) }
                    3 -> {
                        vm.setEditMode(ToDoListEditMode.Add)
                        vm.setIsShowNewDialog(true)
                    }
                }
            }
        )
    }
}

@Composable
fun SideMenu() {
    val vm: UserInfoViewModel = hiltViewModel()
    UserInfoScreen(
        state = vm.state,
        effectFlow = vm.effects.receiveAsFlow()
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Contacts,
        NavigationItem.Messages,
        NavigationItem.TODOLists
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title)},
                label = { Text(text = item.title)},
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.5f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // 避免navigate切換tab造成BackStack無限增長, 將startDestination之外的頁面移除
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // 避免選同一個tab重複加入stack
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    val contactsViewModel: ContactsViewModel = hiltViewModel()
    val messagesViewModel: MessagesViewModel = hiltViewModel()
    val todoListViewModel: ToDoListViewModel = hiltViewModel()

    NavHost(navController, startDestination = NavigationItem.Contacts.route) {
        composable(NavigationItem.Contacts.route) {
            ContactsScreen(
                state = contactsViewModel.state,
                effectFlow = contactsViewModel.effects.receiveAsFlow()
            )
        }
        composable(NavigationItem.Messages.route) {
            MessagesScreen(
                state = messagesViewModel.state,
                effectFlow = messagesViewModel.effects.receiveAsFlow()
            )
        }
        composable(NavigationItem.TODOLists.route) {
            ToDoListScreen(
                state = todoListViewModel.state,
                effectFlow = todoListViewModel.effects.receiveAsFlow(),
                onRequestAction = { item ->
                    when(todoListViewModel.state.editMode) {
                        ToDoListEditMode.Edit -> { todoListViewModel.editItem(item) }
                        ToDoListEditMode.Delete -> { todoListViewModel.deleteItem(item) }
                        ToDoListEditMode.Add -> { todoListViewModel.addNewItem(item) }
                        else -> {}
                    }
                },
                onShowDialog = { isShow->
                    todoListViewModel.setIsShowNewDialog(isShow)
                    if (!isShow) {
                        todoListViewModel.setEditMode(ToDoListEditMode.Normal)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(
        scaffoldState = rememberScaffoldState(),
        navController = rememberNavController()
    )
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    BottomNavigationBar(rememberNavController())
}
