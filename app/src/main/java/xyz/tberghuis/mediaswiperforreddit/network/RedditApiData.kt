package xyz.tberghuis.mediaswiperforreddit.network

data class RedditApiResponse(val data: RedditApiResponseData)

data class RedditApiResponseData(val after: String, val children: List<RedditApiPost>)

data class RedditApiPost(val data: RedditApiPostData)

data class RedditApiPostData(val media: RedditApiMedia?, val url: String)

data class RedditApiMedia(val oembed: RedditApiOembed)

data class RedditApiOembed(val thumbnail_url: String?)