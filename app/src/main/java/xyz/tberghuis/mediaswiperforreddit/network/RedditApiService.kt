package xyz.tberghuis.mediaswiperforreddit.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL =
  "https://reddit.com"

private val moshi = Moshi.Builder()
  .add(KotlinJsonAdapterFactory())
  .build()

private val retrofit = Retrofit.Builder()
  .addConverterFactory(MoshiConverterFactory.create(moshi))
  .baseUrl(BASE_URL)
  .build()

interface RedditApiService {
  @GET("r/{subreddit}.json")
  suspend fun fetchRedditApi(
    @Path("subreddit") subreddit: String,
    @Query("after") after: String
  ): RedditApiResponse
}

object RedditApi {
  val retrofitService: RedditApiService by lazy {
    retrofit.create(RedditApiService::class.java)
  }
}