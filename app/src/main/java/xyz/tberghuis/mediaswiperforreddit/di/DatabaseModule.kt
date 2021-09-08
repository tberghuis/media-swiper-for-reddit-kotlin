package xyz.tberghuis.mediaswiperforreddit.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xyz.tberghuis.mediaswiperforreddit.db.AppDatabase
import xyz.tberghuis.mediaswiperforreddit.db.SubredditDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
  @Provides
  fun provideSubredditDao(database: AppDatabase): SubredditDao {
    return database.subredditDao()
  }

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
    return Room.databaseBuilder(
      appContext,
      AppDatabase::class.java,
      "subreddit.db"
    ).build()
  }
}