package xyz.tberghuis.mediaswiperforreddit.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.ui.PlayerView
import kotlin.math.absoluteValue
import kotlinx.coroutines.flow.collect


@Composable
fun SubredditPager(subreddit: String) {
  val pagerViewModel = viewModel<PagerViewModel>()
  val context = LocalContext.current
  // the alternative is to initialise in AndroidView factory
  LaunchedEffect(Unit) {
    pagerViewModel.initializePlayers(context)
  }

  // this should be called by vm init block
  LaunchedEffect(Unit) {
    pagerViewModel.fetchMore(subreddit)
  }

  if (pagerViewModel.playersInitialised.value) {
    MyPager(subreddit)
  }


}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyPager(subreddit: String) {
  val pagerViewModel = viewModel<PagerViewModel>()
  val pagerState = rememberPagerState(pageCount = pagerViewModel.urls.size)

  LaunchedEffect(pagerState) {
    val currentPageFlow = snapshotFlow { pagerState.currentPage }
    currentPageFlow.collect { page ->
      if (pagerViewModel.urls.size - page < 3) {
        pagerViewModel.fetchMore(subreddit)
      }
    }
  }

  VerticalPager(state = pagerState) { page ->

    val pageDelta = page - currentPage
    if (pageDelta.absoluteValue <= 1) {
      Box(modifier = Modifier
        .fillMaxSize()
        .clickable {
          pagerViewModel.boxClick(page)
        }
      ) {
        PlayerViewContainer(page, pagerState.currentPage)
      }
    }
  }
}


// I should probably only reuse Player instead of PlayerView???
@Composable
fun PlayerViewContainer(page: Int, currentPage: Int) {
  // this url to be passed in
//  val gifUrl = "https://i.imgur.com/xxxxx.mp4"

  val pagerViewModel = viewModel<PagerViewModel>()

  AndroidView(
    factory = {
      PlayerView(it).apply {
        useController = false
        player = pagerViewModel.getPlayer(page)
      }
    },
    update = {
      Log.d("xxx", "page $page currentPage $currentPage")
      if (page == currentPage) {
        it.player?.play()
      } else {
        it.player?.pause()
      }
    }
  )
}