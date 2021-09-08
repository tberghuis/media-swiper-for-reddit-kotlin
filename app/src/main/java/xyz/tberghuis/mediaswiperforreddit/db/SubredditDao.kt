package xyz.tberghuis.mediaswiperforreddit.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface SubredditDao {
  @Query("SELECT * FROM subreddit")
  fun getAll(): Flow<List<Subreddit>>

  @Insert
  fun insertAll(vararg subreddits: Subreddit)

  @Delete
  fun delete(subreddit: Subreddit)
}