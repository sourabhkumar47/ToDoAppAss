package com.sourabh.todoapp.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sourabh.todoapp.R
import com.sourabh.todoapp.presentation.add_screen.AddScreen
import com.sourabh.todoapp.presentation.list_screen.ListScreen

@Composable
fun Nav() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "add_task",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("add_task") { AddScreen(navController) }
            composable("task_list") { ListScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "add_task",
            onClick = {
                navController.navigate("add_task") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task",
//                    tint = if (currentRoute == "add_task") colorResource(R.color.accent_dark) else colorResource(
//                        R.color.accent_light
//                    )
                )
            }, label = { Text("Add Task") }
        )
        NavigationBarItem(
            selected = currentRoute == "task_list",
            onClick = {
                navController.navigate("task_list") {
                    popUpTo("add_task")
                }
            },
            icon = {
                Icon(
                    Icons.Default.Checklist,
                    contentDescription = "Task List",
//                    tint = if (currentRoute == "add_task") colorResource(R.color.accent_dark) else colorResource(
//                        R.color.accent_light
//                    )
                )
            },
            label = { Text("Task List") }
        )
    }
}