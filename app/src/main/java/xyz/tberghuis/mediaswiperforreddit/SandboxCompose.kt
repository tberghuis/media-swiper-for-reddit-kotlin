package xyz.tberghuis.mediaswiperforreddit

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlinx.coroutines.flow.collect
import xyz.tberghuis.mediaswiperforreddit.ui.PagerViewModel

//sealed class Event {
//  object Click : Event()
//}
//class SandboxViewModel : ViewModel() {
////  private val eventFlow = MutableSharedFlow<Event>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
//  val count = mutableStateOf(0)
//  fun boxClick() {
//    Log.d("xxx", "box click")
////    count.value = count.value + 1
//    count.value++
//  }
//}


