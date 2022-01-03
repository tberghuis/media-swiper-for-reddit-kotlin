package xyz.tberghuis.mediaswiperforreddit.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(navController: NavHostController) {
  val openDialog = rememberSaveable { mutableStateOf(false) }
  val closeDialog = { openDialog.value = false }
  Scaffold(
    topBar = { TopAppBar(title = { Text("Media Swiper for Reddit") }) },
    floatingActionButton = {
      FloatingActionButton(onClick = {
        openDialog.value = true
      }) {
        Text("X")
      }
    },
    content = {
      HomeScreenContent(navController)
      if (openDialog.value) AddSubredditDialog(closeDialog)
    },
  )
}

@Composable
fun HomeScreenContent(navController: NavHostController) {
  val homeViewModel: HomeViewModel = hiltViewModel()

  var deleteDialog: String? by rememberSaveable { mutableStateOf(null) }

  deleteDialog?.let {
    DeleteSubredditDialog({ deleteDialog = null }, deleteDialog!!)
  }

  // no need for remember???
  // what about lifecycleowner??? that androiddevelopers medium post flow compose
  val subreddits: List<String> by homeViewModel.getSubreddits().collectAsState(listOf())

  LazyColumn(contentPadding = PaddingValues(10.dp)) {

    item() {
      Text("hello world")
    }

    items(items = subreddits, itemContent = { subreddit ->
      Row {
        Button(
          modifier = Modifier.weight(1f),
          onClick = {
            navController.navigate("subredditPager/$subreddit")
          }) {
          Text(subreddit)
        }
        IconButton(onClick = { deleteDialog = subreddit }) {
          Icon(Icons.Filled.Delete, contentDescription = "delete")
        }
      }
    })
  }
}

@Composable
fun AddSubredditDialog(closeDialog: () -> Unit) {
  val homeViewModel: HomeViewModel = hiltViewModel()
  var text by rememberSaveable { mutableStateOf("") }
  AlertDialog(onDismissRequest = closeDialog,
//    title = { Text("Add Subreddit") },
    text = {
      TextField(
        value = text,
        onValueChange = { text = it },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
          onDone = {
            //todo try catch UNIQUE constraint...
            homeViewModel.addSubreddit(text)
            closeDialog()
          }
        ),
        label = { Text("Add Subreddit") }
      )
    },
    buttons = {})
}

@Composable
fun DeleteSubredditDialog(closeDialog: () -> Unit, subreddit: String) {
  val homeViewModel: HomeViewModel = hiltViewModel()
  AlertDialog(onDismissRequest = closeDialog,
//    title = { Text("Add Subreddit") },
    text = {
      Text("Delete \"$subreddit\"")
    },
    confirmButton = {
      Button(
        onClick = {
          homeViewModel.deleteSubreddit(subreddit)
          closeDialog()
        }) {
        Text("OK")
      }
    },
    dismissButton = {
      Button(
        onClick = closeDialog
      ) {
        Text("Cancel")
      }
    })
}

