package com.example.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class AdminMetric(
  val id: String,
  val title: String,
  val value: String,
  val growthDelta: String,
  val isPositive: Boolean = true,
  val color: Color,
  val icon: ImageVector
)

data class ModerationItem(
  val id: String,
  val title: String,
  val status: String = "Pending Review",
  val thumbnailRes: Int,
  val category: String = "Kids"
)

data class RecentActivity(
  val id: String,
  val title: String,
  val subtitle: String,
  val timeAgo: String,
  val badgeText: String? = null,
  val badgeColor: Color? = null,
  val iconType: String
)

data class CategoryShare(
  val name: String,
  val percentage: Int,
  val color: Color
)

data class AnalyticsDataPoint(
  val dateLabel: String,
  val viewsK: Float,
  val likesK: Float,
  val subsK: Float
)

enum class UserRole(val label: String) {
  SUPER_ADMIN("Super Admin · ಮುಖ್ಯಸ್ಥರು"),
  CREATOR_USER("Creator · ರಚನೆಕಾರರು"),
  COMMUNITY_USER("Viewer · ವೀಕ್ಷಕರು")
}

data class UserProfile(
  val email: String,
  val displayName: String,
  val role: UserRole = UserRole.COMMUNITY_USER,
  val channelName: String = "Youth Tube User",
  val subscribersCount: String = "0"
) {
  val isAdmin: Boolean get() = role == UserRole.SUPER_ADMIN
}

data class PublishedVideo(
  val id: String,
  val title: String,
  val category: String,
  val thumbnailRes: Int,
  val publishedAt: String = "Just now · ಇದೀಗ",
  val status: String = "Published · ಪ್ರಕಟಿಸಲಾಗಿದೆ"
) {
  val publishedTime: String get() = publishedAt
}

object AdMobBannerConfig {
  const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
  const val TEST_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917"
}
