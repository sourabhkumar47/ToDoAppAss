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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            composable("task_list") { ListScreen(navController) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("add_task") },
            icon = { Icon(Icons.Default.Add, contentDescription = "Add Task") },
            label = { Text("Add Task") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("task_list") },
            icon = { Icon(Icons.Default.Checklist, contentDescription = "Task List") },
            label = { Text("Task List") }
        )
    }
}