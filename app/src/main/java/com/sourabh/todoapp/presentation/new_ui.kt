package com.sourabh.todoapp.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sourabh.todoapp.R
import com.sourabh.todoapp.data.local.entity.Task
import com.sourabh.todoapp.presentation.home_screen.AddTaskPopup
import com.sourabh.todoapp.presentation.home_screen.EditTaskPopup
import com.sourabh.todoapp.viewmodel.TaskViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    val remoteTasks by viewModel.remoteTasks.collectAsState()
    val showPopup by viewModel.showAddTaskPopup.collectAsState()
    val showEditPopup by viewModel.showEditTaskPopup.collectAsState()
    val taskBeingEdited by viewModel.taskBeingEdited.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLocalTask()
        viewModel.loadRemoteTasks()
    }

    fun getCurrentFormattedDate(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM", Locale.ENGLISH)
        return currentDateTime.format(formatter)
    }

    val currentDate = getCurrentFormattedDate()

    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedFilter by remember { mutableStateOf("Local") }

    val tabTitles = listOf("Messages", "Today's Task", "Last Activity")

    Scaffold(
        containerColor = Color.White,

        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.White
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        color = if (selectedTab == index) Color.Black else Color.LightGray
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when (selectedTab) {
                        0 -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Messages Available",
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        1 -> {
                            if (isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                Column {
                                    Row {
                                        Column(
                                            modifier = Modifier
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                text = "Today's Task",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = currentDate,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.LightGray
                                            )
                                        }

                                        Button(
                                            onClick = { viewModel.showAddTaskPopup() },
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .height(45.dp)
                                                .weight(1f)
                                        ) {
                                            Text("+ New Task")
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    ) {
                                        FilterButton("Local", tasks.size, selectedFilter) {
                                            selectedFilter = "Local"
                                        }
                                        FilterButton(
                                            "Remote",
                                            remoteTasks.size,
                                            selectedFilter
                                        ) { selectedFilter = "Remote" }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    LazyColumn {
                                        itemsIndexed(if (selectedFilter == "Local") tasks else remoteTasks) { _, task ->
                                            TaskItem(
                                                task = task,
                                                onEdit = {task -> viewModel.showEditTaskPopup(task) },
                                                onDelete = { task -> viewModel.deleteTask(task) }
                                            )
                                        }
                                    }
                                }
                            }

                        }

                        2 -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Recent Activity",
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
                if (showPopup) {

                    AddTaskPopup(
                        onSave = { title ->
                            viewModel.addTask(Task(title = title, isCompleted = false))
                            viewModel.hideAddTaskPopup()
                        },
                        onDiscard = { viewModel.hideAddTaskPopup() }
                    )

                }

                if (showEditPopup && taskBeingEdited != null) {
                    EditTaskPopup(
                        task = taskBeingEdited!!,
                        onSave = { updatedTask ->
                            viewModel.editTask(updatedTask)
                        },
                        onDiscard = { viewModel.hideEditTaskPopup() },
                        onDelete = { task ->
                            viewModel.deleteTask(task)
                            viewModel.hideEditTaskPopup()
                        }
                    )
                }

            }
        }
    )
}


@Composable
fun TaskItem(
    task: Task,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.LightGray, shape = MaterialTheme.shapes.medium)
            .clickable { onEdit(task) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = task.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onDelete(task) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_box_outline_blank_24),
                        contentDescription = "Delete Task"
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Today 10:00 PM - 11:45 PM",
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun FilterButton(label: String, count: Int, selectedFilter: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedFilter == label) Color.Gray else Color(0xFFEAEAEA)
        ),
        modifier = Modifier.height(36.dp)
    ) {
        Text(text = "$label ($count)", color = Color.Black)
    }
}
