package com.sourabh.todoapp.presentation.list_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sourabh.todoapp.R
import com.sourabh.todoapp.data.local.entity.Task
import com.sourabh.todoapp.viewmodel.TaskViewModel

@Composable
fun ListScreen(viewModel: TaskViewModel = hiltViewModel()) {

    val localTasks by viewModel.tasks.collectAsState()
    val remoteTasks by viewModel.remoteTasks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLocalTask()
        viewModel.loadRemoteTasks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Local Tasks", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(localTasks) { task ->
                TaskItem(task)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Remote Tasks", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(remoteTasks) { task ->
                TaskItem(task)
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Title: ${task.title}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, bottom = 8.dp)
            )

            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_box_outline_blank_24),
                    contentDescription = "checkbox"
                )
            }
        }
    }
}
