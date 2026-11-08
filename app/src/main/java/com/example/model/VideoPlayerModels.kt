package com.example.model

data class VideoDetails(
  val id: String,
  val title: String,
  val subtitle: String,
  val duration: String,
  val durationSeconds: Int,
  val channelName: String,
  val channelSubscribers: String,
  val views: String,
  val likes: String,
  val thumbnailRes: Int,
  val headline: String,
  val hashtags: List<String>,
  val commentsCount: String,
  val topCommentUser: String,
  val topCommentText: String,
  val is4K: Boolean = true
)

enum class LikeStatus {
  NONE,
  LIKED,
  DISLIKED
}

data class NextVideoItem(
  val id: String,
  val title: String,
  val channel: String,
  val duration: String,
  val durationSeconds: Int,
  val thumbnailRes: Int,
  val views: String,
  val subtitle: String = "",
  val headline: String = "",
  val hashtags: List<String> = emptyList()
)
