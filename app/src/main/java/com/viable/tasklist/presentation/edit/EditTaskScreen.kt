package com.viable.tasklist.presentation.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viable.tasklist.BuildConfig

@Stable
class PlayerPageState(backgroundColor: Color?) {

    var backgroundColor by mutableStateOf(backgroundColor)
    var outlineMasksAndMattes by mutableStateOf(false)
    var applyOpacityToLayers by mutableStateOf(false)
    var enableMergePaths by mutableStateOf(false)
    var focusMode by mutableStateOf(false)
    var showWarningsDialog by mutableStateOf(false)

    var borderToolbar by mutableStateOf(false)
    var speedToolbar by mutableStateOf(false)
    var backgroundColorToolbar by mutableStateOf(false)

    var progressSliderGesture: Float? by mutableStateOf(null)
    var shouldPlay by mutableStateOf(true)
    var targetSpeed by mutableStateOf(1f)
    var shouldLoop by mutableStateOf(true)
}

@Composable
fun PlayerPage(
    animationBackgroundColor: Color? = null,
) {
    val scaffoldState = rememberScaffoldState()
    val state = remember { PlayerPageState(animationBackgroundColor) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { PlayerPageTopAppBar(state) },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding),
        ) {
            PlayerPageContent(
                state,
            )
        }
    }
}

@Composable
private fun ColumnScope.ExpandVisibility(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        content()
    }
}

@Composable
private fun PlayerPageTopAppBar(
    state: PlayerPageState,
) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    TopAppBar(
        title = {},
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(
                onClick = { backPressedDispatcher?.onBackPressed() },
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
fun PlayerPageContent(
    state: PlayerPageState,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                // .maybeBackground(state.backgroundColor)
                .fillMaxWidth(),
        ) {
        }

        ExpandVisibility(!state.focusMode) {
            PlayerControlsRow(state)
        }
    }
}

@Composable
private fun PlayerControlsRow(
    state: PlayerPageState,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = { state.shouldPlay = !state.shouldPlay },
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = null,
                    )
                }
                Text(
                    "Some Text",
                    style = TextStyle(fontSize = 8.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 48.dp, bottom = 8.dp),
                )
            }
            /*Slider(
                value = state.progressSliderGesture ?: state.animatable.progress,
                onValueChange = { state.progressSliderGesture = it },
                onValueChangeFinished = { state.progressSliderGesture = null },
                modifier = Modifier.weight(1f)
            )*/
            IconButton(
                onClick = { state.shouldLoop = !state.shouldLoop },
            ) {
                Icon(
                    Icons.Filled.Call,
                    tint = Color.Black,
                    contentDescription = null,
                )
            }
        }
        Text(
            BuildConfig.VERSION_NAME,
            fontSize = 6.sp,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
        )
    }
}

@Composable
private fun BackgroundToolbarItem(
    color: Color,
    strokeColor: Color = color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .size(24.dp)
            .border(1.dp, strokeColor, shape = CircleShape),
    )
}

@Preview
@Composable
fun SpeedToolbarPreview() {
    val state = remember { PlayerPageState(null) }
    PlayerPage(null)
}

/*@Composable
fun EditTaskScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Screen") },
                actions = {
                    IconButton(onClick = { *//* ... *//* }) {
                        Icon(Icons.Default.Call, contentDescription = "Cancel")
                    }
                }
            )
        }
    ) {
        *//*Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 8.dp
            ) {
                Text(
                    text = stringResource(R.string.priority),
                    style = MaterialTheme.typography.h6
                )
            }
            Button(
                onClick = { *//**//* ... *//**//* },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding),
                enabled = false
            ) {
                Text(stringResource(R.string.delete))
            }
        }*//*
    }
}*/

/*
@Preview(showBackground = true)
@Composable
fun MyLayoutPreview() {
    PlayerPage(null)//EditTaskScreen()
}*/

@Composable
fun EditScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Edit")
                },
                actions = {
                    IconButton(onClick = { /* Cancel clicked */ }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Item name") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /*Save clicked */ },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Preview
@Composable
fun TaskEditScreenPreview() {
    EditScreen()
}
