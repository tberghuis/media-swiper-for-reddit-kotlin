package xyz.tberghuis.mediaswiperforreddit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.tberghuis.mediaswiperforreddit.ui.HomeScreen
import xyz.tberghuis.mediaswiperforreddit.ui.SubredditPager
import xyz.tberghuis.mediaswiperforreddit.ui.theme.MediaSwiperForRedditTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MediaSwiperForRedditTheme {
        Surface(color = MaterialTheme.colors.background) {
          MyApp()
        }
      }
    }
  }
}

@Composable
fun MyApp() {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen(navController) }
    composable(
      "subredditPager/{subreddit}",
      arguments = listOf(
        navArgument("subreddit") { type = NavType.StringType },
      )
    ) { backStackEntry ->
      val subreddit = backStackEntry.arguments?.getString("subreddit")!!
      SubredditPager(
        subreddit = subreddit
      )
    }
  }
}