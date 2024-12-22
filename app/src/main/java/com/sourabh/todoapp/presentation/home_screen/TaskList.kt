package com.sourabh.todoapp.presentation.home_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sourabh.todoapp.R
import com.sourabh.todoapp.data.local.entity.Task
import com.sourabh.todoapp.viewmodel.TaskViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    val remoteTasks by viewModel.remoteTasks.collectAsState()
    val showPopup by viewModel.showAddTaskPopup.collectAsState()
    val showEditPopup by viewModel.showEditTaskPopup.collectAsState()
    val taskBeingEdited by viewModel.taskBeingEdited.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val topBarFont = FontFamily(
        Font(R.font.hindipagefont)
    )

    LaunchedEffect(Unit) {
        viewModel.loadLocalTask()
        viewModel.loadRemoteTasks()
    }

    Scaffold(
        containerColor = colorResource(id = R.color.accent_dim_light),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "To Do App",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = topBarFont
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_settings_suggest_24),
                            contentDescription = "Setting",
                            modifier = Modifier
                                .size(32.dp)
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = colorResource(id = R.color.accent_dark),
                    navigationIconContentColor = colorResource(id = R.color.surface),
                    scrolledContainerColor = colorResource(id = R.color.surface),
                    titleContentColor = colorResource(id = R.color.surface),
                    actionIconContentColor = colorResource(id = R.color.surface)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddTaskPopup() },
                containerColor = colorResource(id = R.color.accent_dark_dim),
                modifier = Modifier.padding(bottom = 26.dp, end = 20.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "Add Task",
                    tint = colorResource(id = R.color.accent_dim_light)
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    tasks.isEmpty() && remoteTasks.isEmpty() -> {
                        EmptyTaskListAnimation()
                    }

                    else -> {
//                        TaskList(
//                            tasks = tasks,
//                            onEdit = { task -> viewModel.showEditTaskPopup(task) }
//                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        ) {
                            Text(
                                "Local Tasks",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (tasks.isEmpty()) {
                                Text(
                                    "No local tasks found",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                )
                            }

                            TaskList(
                                modifier = Modifier.heightIn(max = 250.dp),
                                tasks = tasks,
                                onEdit = { task -> viewModel.showEditTaskPopup(task) },
                                onDelete = { task -> viewModel.deleteTask(task) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "Remote Tasks",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TaskList(
                                tasks = remoteTasks,
                                onEdit = { task -> viewModel.showEditTaskPopup(task) },
                                modifier = Modifier,
                                onDelete = { task -> viewModel.deleteTask(task) }
                            )
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
fun TaskList(
    modifier: Modifier,
    tasks: List<Task>,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    LazyColumn(
        modifier = Modifier
//            .fillMaxSize()
    ) {
        itemsIndexed(tasks) { index, task ->
            TaskItem(
                task = task,
                tasks = tasks,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    tasks: List<Task>,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {

    val bodyFont = FontFamily(
        Font(R.font.kayphodudegular)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
            .clickable { onEdit(task) },
        colors = CardColors(
            containerColor = colorResource(id = R.color.accent_dark),
            contentColor = colorResource(id = R.color.surface),
            disabledContainerColor = colorResource(id = R.color.surface),
            disabledContentColor = colorResource(id = R.color.surface),
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onDelete(task)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_box_outline_blank_24),
                    contentDescription = "checkbox"
                )
            }

            Text(
                text = task.title,
                fontFamily = bodyFont,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, bottom = 8.dp)
            )

            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_drag_indicator_24),
                    contentDescription = "Reorder"
                )
            }
        }
    }
}


@Composable
fun EmptyTaskListAnimation() {
    var showAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        showAnimation = true
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (showAnimation) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_girl_box))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.6f)
            )
        } else {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}