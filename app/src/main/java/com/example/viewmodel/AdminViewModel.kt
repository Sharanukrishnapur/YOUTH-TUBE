package com.example.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.R
import com.example.model.AdminMetric
import com.example.model.AnalyticsDataPoint
import com.example.model.CategoryShare
import com.example.model.ModerationItem
import com.example.model.PublishedVideo
import com.example.model.RecentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminViewModel : ViewModel() {

  private val _activeNavItem = MutableStateFlow("Dashboard")
  val activeNavItem: StateFlow<String> = _activeNavItem.asStateFlow()

  private val _selectedLanguage = MutableStateFlow("ಕನ್ನಡ")
  val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

  private val _selectedTimeframe = MutableStateFlow("Last 30 Days")
  val selectedTimeframe: StateFlow<String> = _selectedTimeframe.asStateFlow()

  // Video Moderation Queue
  private val _moderationQueue = MutableStateFlow(
    listOf(
      ModerationItem("m1", "Kids Learning Colors", "Pending Review", R.drawable.thumb_animals, "Kids"),
      ModerationItem("m2", "Tirupati Balaji Darshan", "Pending Review", R.drawable.thumb_ganesh, "Devotional"),
      ModerationItem("m3", "Shivaji Maharaj Fort History", "Pending Review", R.drawable.thumb_shivaji, "Education")
    )
  )
  val moderationQueue: StateFlow<List<ModerationItem>> = _moderationQueue.asStateFlow()

  // Published videos from approved moderation
  private val _publishedVideos = MutableStateFlow<List<PublishedVideo>>(
    listOf(
      PublishedVideo("p1", "Kids Learning Colors", "Kids", R.drawable.thumb_animals, "5 mins ago"),
      PublishedVideo("p2", "Tirupati Balaji Darshan", "Devotional", R.drawable.thumb_ganesh, "10 mins ago")
    )
  )
  val publishedVideos: StateFlow<List<PublishedVideo>> = _publishedVideos.asStateFlow()

  // AdMob Settings
  private val _homeBannerAds = MutableStateFlow(true)
  val homeBannerAds: StateFlow<Boolean> = _homeBannerAds.asStateFlow()

  private val _videoAds = MutableStateFlow(true)
  val videoAds: StateFlow<Boolean> = _videoAds.asStateFlow()

  private val _shortsAds = MutableStateFlow(true)
  val shortsAds: StateFlow<Boolean> = _shortsAds.asStateFlow()

  // AdMob Performance Metrics
  val adImpressions: String = "48,210"
  val adClicks: String = "1,150"
  val adRevenue: String = "₹14,850.50"

  val categoryShares = listOf(
    CategoryShare("Kids", 35, Color(0xFF007BFF)),
    CategoryShare("Education", 28, Color(0xFFFF9800)),
    CategoryShare("Devotional", 20, Color(0xFF00C853)),
    CategoryShare("Music", 10, Color(0xFF9C27B0)),
    CategoryShare("Others", 7, Color(0xFFE91E63))
  )

  val analyticsData = listOf(
    AnalyticsDataPoint("Oct 25", 80f, 48f, 22f),
    AnalyticsDataPoint("Oct 31", 105f, 58f, 29f),
    AnalyticsDataPoint("Nov 06", 95f, 52f, 25f),
    AnalyticsDataPoint("Nov 12", 130f, 75f, 38f),
    AnalyticsDataPoint("Nov 18", 145f, 92f, 46f)
  )

  val recentActivities = listOf(
    RecentActivity(
      id = "a1",
      title = "New video uploaded",
      subtitle = "Learning is Fun for Kids",
      timeAgo = "2 mins ago",
      badgeText = "Approved",
      badgeColor = Color(0xFF4CAF50),
      iconType = "upload"
    ),
    RecentActivity(
      id = "a2",
      title = "Reported Video",
      subtitle = "Inappropriate content",
      timeAgo = "5 mins ago",
      badgeText = "Review",
      badgeColor = Color(0xFFFFB300),
      iconType = "report"
    ),
    RecentActivity(
      id = "a3",
      title = "New Subscriber",
      subtitle = "Ramesh Kumar",
      timeAgo = "8 mins ago",
      iconType = "subscriber"
    ),
    RecentActivity(
      id = "a4",
      title = "New Comment",
      subtitle = "Super video!",
      timeAgo = "10 mins ago",
      iconType = "comment"
    )
  )

  fun selectNavItem(item: String) {
    _activeNavItem.value = item
  }

  fun setLanguage(lang: String) {
    _selectedLanguage.value = lang
  }

  fun setTimeframe(timeframe: String) {
    _selectedTimeframe.value = timeframe
  }

  fun approveModeration(id: String) {
    val target = _moderationQueue.value.find { it.id == id }
    if (target != null) {
      _publishedVideos.value = _publishedVideos.value + PublishedVideo(
        id = target.id,
        title = target.title,
        category = target.category,
        thumbnailRes = target.thumbnailRes,
        publishedAt = "Just now · ಇದೀಗ"
      )
      _moderationQueue.value = _moderationQueue.value.map {
        if (it.id == id) it.copy(status = "Published · ಪ್ರಕಟಿಸಲಾಗಿದೆ") else it
      }
    }
  }

  fun rejectModeration(id: String) {
    _moderationQueue.value = _moderationQueue.value.filterNot { it.id == id }
  }

  fun toggleHomeBannerAds() {
    _homeBannerAds.value = !_homeBannerAds.value
  }

  fun toggleVideoAds() {
    _videoAds.value = !_videoAds.value
  }

  fun toggleShortsAds() {
    _shortsAds.value = !_shortsAds.value
  }
}
