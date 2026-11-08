package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.model.LikeStatus
import com.example.model.NextVideoItem
import com.example.model.VideoDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class VideoPlayerViewModel : ViewModel() {

  private val _currentVideo = MutableStateFlow(
    VideoDetails(
      id = "shivaji_01",
      title = "Chhatrapati Shivaji Maharaj",
      subtitle = "Life and Legacy | ಮಹಾರಾಜರ ಜೀವನ ಗಾಥೆ",
      duration = "12:48",
      durationSeconds = 768, // 12 min 48 sec
      channelName = "YOUTH TUBE",
      channelSubscribers = "1.2M subscribers",
      views = "4.5M views",
      likes = "259K",
      thumbnailRes = R.drawable.player_hero_shivaji,
      headline = "Swarajya Lives On! | ಜಯ ಭವಾನಿ, ಜಯ ಶಿವಾಜಿ!",
      hashtags = listOf("#ShivajiMaharaj", "#Swarajya", "#History", "#Kannada"),
      commentsCount = "12K",
      topCommentUser = "SwarajyaBhakt",
      topCommentText = "Jai Shivaji Maharaj! 🙏",
      is4K = true
    )
  )
  val currentVideo: StateFlow<VideoDetails> = _currentVideo.asStateFlow()

  private val _isPlaying = MutableStateFlow(true)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _currentSeconds = MutableStateFlow(35) // starts at 0:35 matching mockup
  val currentSeconds: StateFlow<Int> = _currentSeconds.asStateFlow()

  private val _isMuted = MutableStateFlow(false)
  val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

  private val _isFullscreen = MutableStateFlow(false)
  val isFullscreen: StateFlow<Boolean> = _isFullscreen.asStateFlow()

  private val _showQualityPicker = MutableStateFlow(false)
  val showQualityPicker: StateFlow<Boolean> = _showQualityPicker.asStateFlow()

  private val _selectedQuality = MutableStateFlow("Auto")
  val selectedQuality: StateFlow<String> = _selectedQuality.asStateFlow()

  private val _isSubscribed = MutableStateFlow(false)
  val isSubscribed: StateFlow<Boolean> = _isSubscribed.asStateFlow()

  private val _likeStatus = MutableStateFlow(LikeStatus.LIKED)
  val likeStatus: StateFlow<LikeStatus> = _likeStatus.asStateFlow()

  private val _likesCount = MutableStateFlow("259K")
  val likesCount: StateFlow<String> = _likesCount.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _nextVideos = MutableStateFlow(
    listOf(
      NextVideoItem(
        id = "next_01",
        title = "Krishna Stories in Kannada",
        channel = "YOUTH TUBE",
        duration = "10:15",
        durationSeconds = 615,
        thumbnailRes = R.drawable.thumb_next_krishna,
        views = "3.8M views",
        subtitle = "Devotional & Tales | ಭಕ್ತಿ ಕಥೆಗಳು",
        headline = "Sri Krishna Balaleela and Teachings",
        hashtags = listOf("#Krishna", "#KannadaStories", "#Kids", "#Bhakti")
      ),
      NextVideoItem(
        id = "next_02",
        title = "Learning is Fun | Children Songs",
        channel = "YOUTH TUBE",
        duration = "8:20",
        durationSeconds = 500,
        thumbnailRes = R.drawable.thumb_next_learning,
        views = "2.9M views",
        subtitle = "Educational Rhymes & Learning",
        headline = "Sing Along! Educational Kids Rhymes in Full HD",
        hashtags = listOf("#KidsZone", "#Rhymes", "#Learning", "#Fun")
      ),
      NextVideoItem(
        id = "next_03",
        title = "Kashi Darshan | Travel Guide",
        channel = "YOUTH TUBE",
        duration = "14:32",
        durationSeconds = 872,
        thumbnailRes = R.drawable.thumb_next_kashi,
        views = "5.1M views",
        subtitle = "Sacred Heritage & Yatra | ಕಾಶಿ ದರ್ಶನ",
        headline = "Spiritual Journey to Varanasi & Ganga Aarti",
        hashtags = listOf("#Kashi", "#Varanasi", "#Darshan", "#Heritage")
      ),
      NextVideoItem(
        id = "next_04",
        title = "Masala Dosa Recipe | Easy & Tasty",
        channel = "YOUTH TUBE",
        duration = "7:18",
        durationSeconds = 438,
        thumbnailRes = R.drawable.thumb_next_dosa,
        views = "1.8M views",
        subtitle = "South Indian Kitchen Secrets",
        headline = "Crispy Hotel Style Masala Dosa & Chutney",
        hashtags = listOf("#Cooking", "#MasalaDosa", "#KannadaRecipe", "#Food")
      ),
      NextVideoItem(
        id = "next_05",
        title = "Chamundeshwari Devi Darshana",
        channel = "YOUTH TUBE",
        duration = "12:17",
        durationSeconds = 737,
        thumbnailRes = R.drawable.thumb_next_chamundeshwari,
        views = "6.4M views",
        subtitle = "Mysuru Hill Temple Darshana | ಚಾಮುಂಡಿ ಬೆಟ್ಟ",
        headline = "Divine Blessings of Sri Chamundeshwari Temple Mysuru",
        hashtags = listOf("#ChamundiHill", "#Mysore", "#Bhakti", "#Devotional")
      )
    )
  )
  val nextVideos: StateFlow<List<NextVideoItem>> = _nextVideos.asStateFlow()

  private var playbackTickerJob: Job? = null

  init {
    startPlaybackTicker()
  }

  private fun startPlaybackTicker() {
    playbackTickerJob?.cancel()
    playbackTickerJob = viewModelScope.launch {
      while (isActive) {
        delay(1000)
        if (_isPlaying.value) {
          val totalSec = _currentVideo.value.durationSeconds
          _currentSeconds.value = if (_currentSeconds.value + 1 >= totalSec) 0 else _currentSeconds.value + 1
        }
      }
    }
  }

  fun togglePlayPause() {
    _isPlaying.value = !_isPlaying.value
  }

  fun seekTo(seconds: Int) {
    val totalSec = _currentVideo.value.durationSeconds
    _currentSeconds.value = seconds.coerceIn(0, totalSec)
  }

  fun seekToFraction(fraction: Float) {
    val totalSec = _currentVideo.value.durationSeconds
    _currentSeconds.value = (fraction * totalSec).toInt().coerceIn(0, totalSec)
  }

  fun toggleMute() {
    _isMuted.value = !_isMuted.value
  }

  fun toggleFullscreen() {
    _isFullscreen.value = !_isFullscreen.value
  }

  fun toggleQualityPicker() {
    _showQualityPicker.value = !_showQualityPicker.value
  }

  fun setQuality(quality: String) {
    _selectedQuality.value = quality
    _showQualityPicker.value = false
  }

  fun toggleSubscribe() {
    _isSubscribed.value = !_isSubscribed.value
  }

  fun toggleLike() {
    if (_likeStatus.value == LikeStatus.LIKED) {
      _likeStatus.value = LikeStatus.NONE
      _likesCount.value = "258K"
    } else {
      _likeStatus.value = LikeStatus.LIKED
      _likesCount.value = "259K"
    }
  }

  fun toggleDislike() {
    if (_likeStatus.value == LikeStatus.DISLIKED) {
      _likeStatus.value = LikeStatus.NONE
    } else {
      _likeStatus.value = LikeStatus.DISLIKED
    }
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun playNextVideo(item: NextVideoItem) {
    val old = _currentVideo.value
    // Create new current video from selected next item
    _currentVideo.value = VideoDetails(
      id = item.id,
      title = item.title,
      subtitle = item.subtitle.ifEmpty { "${item.title} | ${item.channel}" },
      duration = item.duration,
      durationSeconds = item.durationSeconds,
      channelName = item.channel,
      channelSubscribers = "1.2M subscribers",
      views = item.views,
      likes = "180K",
      thumbnailRes = item.thumbnailRes,
      headline = item.headline.ifEmpty { item.title },
      hashtags = if (item.hashtags.isNotEmpty()) item.hashtags else listOf("#YouthTube", "#India", "#Trending"),
      commentsCount = "4.2K",
      topCommentUser = "YouthFan",
      topCommentText = "Awesome video! Thanks Youth Tube 🙏",
      is4K = true
    )
    // Swap old into next list so user can navigate back
    val updatedList = _nextVideos.value.filter { it.id != item.id }.toMutableList()
    updatedList.add(
      NextVideoItem(
        id = old.id,
        title = old.title,
        channel = old.channelName,
        duration = old.duration,
        durationSeconds = old.durationSeconds,
        thumbnailRes = old.thumbnailRes,
        views = old.views,
        subtitle = old.subtitle,
        headline = old.headline,
        hashtags = old.hashtags
      )
    )
    _nextVideos.value = updatedList
    _currentSeconds.value = 0
    _isPlaying.value = true
  }

  fun playCustomVideo(
    id: String,
    title: String,
    duration: String,
    durationSeconds: Int = 600,
    thumbnailRes: Int,
    subtitle: String,
    views: String = "1.5M views",
    hashtags: List<String> = listOf("#YouthTube", "#India", "#Kannada")
  ) {
    val old = _currentVideo.value
    _currentVideo.value = VideoDetails(
      id = id,
      title = title,
      subtitle = subtitle,
      duration = duration,
      durationSeconds = durationSeconds,
      channelName = "YOUTH TUBE",
      channelSubscribers = "1.2M subscribers",
      views = views,
      likes = "210K",
      thumbnailRes = thumbnailRes,
      headline = "$title | $subtitle",
      hashtags = hashtags,
      commentsCount = "5.8K",
      topCommentUser = "YouthFan",
      topCommentText = "Incredible video on Youth Tube! ಧನ್ಯವಾದಗಳು 🙏",
      is4K = true
    )
    val updatedList = _nextVideos.value.filter { it.id != id }.toMutableList()
    if (!updatedList.any { it.id == old.id }) {
      updatedList.add(
        NextVideoItem(
          id = old.id,
          title = old.title,
          channel = old.channelName,
          duration = old.duration,
          durationSeconds = old.durationSeconds,
          thumbnailRes = old.thumbnailRes,
          views = old.views,
          subtitle = old.subtitle,
          headline = old.headline,
          hashtags = old.hashtags
        )
      )
    }
    _nextVideos.value = updatedList
    _currentSeconds.value = 0
    _isPlaying.value = true
  }

  override fun onCleared() {
    super.onCleared()
    playbackTickerJob?.cancel()
  }
}
