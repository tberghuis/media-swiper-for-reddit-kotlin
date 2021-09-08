package xyz.tberghuis.mediaswiperforreddit.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import xyz.tberghuis.mediaswiperforreddit.db.Subreddit
import xyz.tberghuis.mediaswiperforreddit.db.SubredditDao
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
  private val subredditDao: SubredditDao
) : ViewModel() {
  fun getSubreddits(): Flow<List<String>> {
    return subredditDao.getAll().map { list -> list.map { it.subreddit } }
  }
  fun addSubreddit(subreddit: String) {
    // why didn't i need Dispatchers.IO in My Lists???
    viewModelScope.launch(Dispatchers.IO) {
      subredditDao.insertAll(Subreddit(subreddit))
    }
  }
}