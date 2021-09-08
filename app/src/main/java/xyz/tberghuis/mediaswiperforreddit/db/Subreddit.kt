package xyz.tberghuis.mediaswiperforreddit.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subreddit(
  @PrimaryKey val subreddit: String
)