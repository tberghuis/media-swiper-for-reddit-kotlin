package xyz.tberghuis.mediaswiperforreddit.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.MimeTypes
import xyz.tberghuis.mediaswiperforreddit.network.RedditApi
import xyz.tberghuis.mediaswiperforreddit.network.RedditApiResponse

fun extractImgurUrls(urls: List<String>): List<String> {
  val imgurUrls = urls.mapNotNull {
    val regex = Regex("""https://i\.imgur\.com/(.*)\.gifv""")
    regex.matchEntire(it)?.groups?.get(1)?.value
  }.map {
    "https://i.imgur.com/$it.mp4"
  }
  return imgurUrls
}

// https://www.redgifs.com/watch/xxxxxxxx
// https://thumbs2.redgifs.com/XxxxXxxx-mobile.mp4
// https://thumbs2.redgifs.com/XxxxXxxx-mobile.jpg

fun extractRedgifsUrls(redditApiResponse: RedditApiResponse): List<String> {
  val allThumbnailUrls = redditApiResponse.data.children.mapNotNull { it.data.media?.oembed?.thumbnail_url }
  val redgifUrls = allThumbnailUrls.mapNotNull {
    val regex = Regex("""https://thumbs2\.redgifs\.com/(.*)\.jpg""")
    regex.matchEntire(it)?.groups?.get(1)?.value
  }.map {
    "https://thumbs2.redgifs.com/$it.mp4"
  }
  return redgifUrls
}

class PagerViewModel : ViewModel() {

  val urls = mutableStateListOf<String>()
  // todo clear on back pressed
  private var nextAfter = ""
  private var isFetching = false

  suspend fun fetchMore(subreddit: String) {
    if (isFetching) {
      return
    }
    isFetching = true
    // todo properly
    val redditApiResponse =
      RedditApi.retrofitService.fetchRedditApi(subreddit = subreddit, after = nextAfter)
    nextAfter = redditApiResponse.data.after
    val unfilteredUrls = redditApiResponse.data.children.map { it.data.url }
    Log.d("xxx", unfilteredUrls.toString())

    // do this stuff in parallel
    urls.addAll(extractImgurUrls(unfilteredUrls))
    urls.addAll(extractRedgifsUrls(redditApiResponse))

    isFetching = false
  }

  val playersInitialised = mutableStateOf(false)

  private var players: Array<SimpleExoPlayer?> = arrayOfNulls(3)

  fun initializePlayers(context: Context) {
    for (i in players.indices) {
      players[i] = SimpleExoPlayer.Builder(context).build().apply {
        playWhenReady = false
        repeatMode = Player.REPEAT_MODE_ONE
      }
    }
    playersInitialised.value = true
  }

  // only call from factory
  fun getPlayer(page: Int): Player {
    val mod = page % 3
    return players[mod]!!.apply {
      val mediaItem = MediaItem.Builder()
        .setUri(urls[page])
        .setMimeType(MimeTypes.VIDEO_MP4)
        .build()
      setMediaItem(mediaItem)
      prepare()
    }
  }

  // better name
  fun boxClick(page: Int) {
    val mod = page % 3
    val player = players[mod]!!
    if (player.isPlaying) {
      player.pause()
    } else {
      player.play()
    }
  }
}