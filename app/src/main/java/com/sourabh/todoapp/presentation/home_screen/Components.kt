package com.sourabh.todoapp.presentation.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sourabh.todoapp.R
import com.sourabh.todoapp.data.local.entity.Task

@Composable
fun AddTaskPopup(onSave: (String) -> Unit, onDiscard: () -> Unit) {
    var taskTitle by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            colors = CardColors(
                containerColor = colorResource(id = R.color.accent_dim_light),
                contentColor = colorResource(id = R.color.black),
                disabledContainerColor = colorResource(id = R.color.accent_dim_light),
                disabledContentColor = colorResource(id = R.color.accent_dim_light),
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add New Task",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(id = R.color.accent_dark)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = {
                        Text(
                            text = "Task Title"
                        )
                    },
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDiscard) {
                        Text(
                            text = "Discard",
                            color = colorResource(id = R.color.error),
                        )
                    }

                    TextButton(onClick = { if (taskTitle.isNotEmpty()) onSave(taskTitle) }) {
                        Text(
                            text = "Save",
                            color = colorResource(id = R.color.accent_dark)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditTaskPopup(
    task: Task,
    onSave: (Task) -> Unit,
    onDiscard: () -> Unit,
    onDelete: (Task) -> Unit
) {
    var taskTitle by remember { mutableStateOf(task.title) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            colors = CardColors(
                containerColor = colorResource(id = R.color.accent_dim_light),
                contentColor = colorResource(id = R.color.black),
                disabledContainerColor = colorResource(id = R.color.accent_dim_light),
                disabledContentColor = colorResource(id = R.color.accent_dim_light),
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Edit Task",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(id = R.color.accent_dark)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = {
                        Text(
                            text = "Task Title"
                        )
                    },
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { onDelete(task) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = "Delete",
                            tint = colorResource(id = R.color.black)
                        )
                    }

//                    TextButton(onClick = { onDelete(task) }) {
//                        Text(
//                            text = "Delete",
//                            color = colorResource(id = R.color.error),
//                        )
//                    }

                    TextButton(onClick = onDiscard) {
                        Text(
                            text = "Discard",
                            color = colorResource(id = R.color.error),
                        )
                    }

                    TextButton(onClick = { if (taskTitle.isNotEmpty()) onSave(task.copy(title = taskTitle)) }) {
                        Text(
                            text = "Save",
                            color = colorResource(id = R.color.accent_dark)
                        )
                    }
                }
            }
        }
    }
}
