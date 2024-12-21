package com.sourabh.todoapp.presentation.add_screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sourabh.todoapp.viewmodel.TaskViewModel

@Composable
fun AddScreen(navController: NavHostController, viewModel: TaskViewModel = hiltViewModel()) {

    Text("Add Screen", style = MaterialTheme.typography.titleMedium)
}
