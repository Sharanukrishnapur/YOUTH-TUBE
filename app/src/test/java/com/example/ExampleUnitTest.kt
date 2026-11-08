package com.example

import androidx.compose.ui.graphics.Color
import com.example.model.LiveChatMessage
import com.example.model.LiveStreamChannel
import com.example.model.LiveVideoFilter
import com.example.model.TaskPriority
import com.example.model.TaskStatus
import com.example.viewmodel.DevPulseViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testHexColorParsing() {
    val cyan = DevPulseViewModel.parseColorSafe("#00D2FF", Color.Black)
    assertEquals(Color(0xFF00D2FF), cyan)

    val fallback = DevPulseViewModel.parseColorSafe("invalid-color", Color.Red)
    assertEquals(Color.Red, fallback)
  }

  @Test
  fun testTaskPriorityLabels() {
    assertEquals("Critical", TaskPriority.CRITICAL.label)
    assertEquals("High", TaskPriority.HIGH.label)
    assertEquals("Medium", TaskPriority.MEDIUM.label)
    assertEquals("Low", TaskPriority.LOW.label)
  }

  @Test
  fun testTaskStatusTransitions() {
    assertEquals("Backlog", TaskStatus.BACKLOG.label)
    assertEquals("In Progress", TaskStatus.IN_PROGRESS.label)
    assertEquals("Review", TaskStatus.REVIEW.label)
    assertEquals("Done", TaskStatus.DONE.label)
  }

  @Test
  fun testLiveVideoFilters() {
    assertEquals("Clean RAW", LiveVideoFilter.CLEAN.label)
    assertEquals("RAW", LiveVideoFilter.CLEAN.badge)
    assertEquals("Cyberpunk", LiveVideoFilter.CYBERPUNK.label)
    assertEquals("NEON", LiveVideoFilter.CYBERPUNK.badge)
    assertEquals("Night Vision", LiveVideoFilter.NIGHT_VISION.label)
    assertEquals("NVG", LiveVideoFilter.NIGHT_VISION.badge)
    assertEquals("Noir Cinema", LiveVideoFilter.NOIR.label)
    assertEquals("Retro VHS", LiveVideoFilter.RETRO_VHS.label)
  }

  @Test
  fun testLiveStreamChannelModel() {
    val channel = LiveStreamChannel(
      id = "test_feed",
      title = "Test Live Broadcast",
      creator = "Studio Alpha",
      category = "Tech",
      viewerCount = 12500,
      resolution = "1080p60",
      bitrateKbps = 6000,
      fps = 60,
      streamUrl = "https://example.com/live.m3u8",
      description = "Test stream feed"
    )
    assertTrue(channel.isLive)
    assertEquals(60, channel.fps)
    assertEquals("1080p60", channel.resolution)
  }

  @Test
  fun testLiveChatMessageModel() {
    val msg = LiveChatMessage(
      id = "m1",
      author = "Tester",
      message = "Hello live video!",
      timestamp = "12:00",
      isVerified = true
    )
    assertTrue(msg.isVerified)
    assertEquals("Tester", msg.author)
  }

  @Test
  fun testAuthRoleAssignment() {
    val adminUser = com.example.model.UserProfile(
      email = "sharanukrishnapur48@gmail.com",
      displayName = "Sharanu Krishnapur",
      role = com.example.model.UserRole.SUPER_ADMIN,
      channelName = "Youth Tube Official",
      subscribersCount = "1.2M"
    )
    assertTrue(adminUser.isAdmin)
    assertEquals("Super Admin · ಮುಖ್ಯಸ್ಥರು", adminUser.role.label)

    val creatorUser = com.example.model.UserProfile(
      email = "creator@kannada.in",
      displayName = "Ramesh Kumar",
      role = com.example.model.UserRole.CREATOR_USER,
      channelName = "Kannada Tech"
    )
    org.junit.Assert.assertFalse(creatorUser.isAdmin)
  }

  @Test
  fun testAdminModerationQueueAndApproval() {
    val adminVm = com.example.viewmodel.AdminViewModel()
    val initialModeration = adminVm.moderationQueue.value
    assertTrue(initialModeration.isNotEmpty())
    val targetId = initialModeration.first().id

    // Approve video
    adminVm.approveModeration(targetId)

    // Check it moved to published
    val published = adminVm.publishedVideos.value
    assertTrue(published.any { it.id == targetId })
    assertEquals("Published · ಪ್ರಕಟಿಸಲಾಗಿದೆ", adminVm.moderationQueue.value.first { it.id == targetId }.status)
  }

  @Test
  fun testAdMobBannerConfig() {
    assertEquals("ca-app-pub-3940256099942544/6300978111", com.example.model.AdMobBannerConfig.TEST_BANNER_ID)
    assertEquals("ca-app-pub-3940256099942544/5224354917", com.example.model.AdMobBannerConfig.TEST_REWARDED_ID)
  }
}
