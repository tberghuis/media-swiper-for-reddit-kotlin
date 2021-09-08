package xyz.tberghuis.mediaswiperforreddit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subreddit::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun subredditDao(): SubredditDao
}