package com.sourabh.todoapp.presentation.add_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sourabh.todoapp.data.local.entity.Task
import com.sourabh.todoapp.viewmodel.TaskViewModel

@Composable
fun AddScreen(navController: NavHostController, viewModel: TaskViewModel = hiltViewModel()) {

    var title by remember { mutableStateOf(TextFieldValue("")) }
    val snackBarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf<String?>(null) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 0.1.dp)
            )
        },
        modifier = Modifier.imePadding(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    textStyle = TextStyle(fontSize = 30.sp),
                    placeholder = {
                        Text(
                            "Enter Task Title"
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (title.text.isEmpty()) {
                            showSnackbar = "Title cannot be empty"
                            return@Button
                        }
                        viewModel.addTask(Task(title = title.text, isCompleted = false))
                        title = TextFieldValue("")
                        Toast.makeText(
                            navController.context,
                            "Saved ToDo",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save", style = MaterialTheme.typography.bodyLarge)
                }

            }
            showSnackbar?.let { message ->
                LaunchedEffect(message) {
                    snackBarHostState.showSnackbar(message)
                    showSnackbar = null
                }
            }
        }
    )
}
