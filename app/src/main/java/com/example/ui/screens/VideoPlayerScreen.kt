package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.LikeStatus
import com.example.model.NextVideoItem
import com.example.model.VideoDetails
import com.example.ui.components.AdMobBannerView
import com.example.ui.components.SocialShareSheet
import com.example.viewmodel.VideoPlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
  onBackClick: () -> Unit,
  videoPlayerViewModel: VideoPlayerViewModel = viewModel(),
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val currentVideo by videoPlayerViewModel.currentVideo.collectAsState()
  val isPlaying by videoPlayerViewModel.isPlaying.collectAsState()
  val currentSeconds by videoPlayerViewModel.currentSeconds.collectAsState()
  val isMuted by videoPlayerViewModel.isMuted.collectAsState()
  val isFullscreen by videoPlayerViewModel.isFullscreen.collectAsState()
  val showQualityPicker by videoPlayerViewModel.showQualityPicker.collectAsState()
  val selectedQuality by videoPlayerViewModel.selectedQuality.collectAsState()
  val isSubscribed by videoPlayerViewModel.isSubscribed.collectAsState()
  val likeStatus by videoPlayerViewModel.likeStatus.collectAsState()
  val likesCount by videoPlayerViewModel.likesCount.collectAsState()
  val nextVideos by videoPlayerViewModel.nextVideos.collectAsState()
  val searchQuery by videoPlayerViewModel.searchQuery.collectAsState()

  var isCcEnabled by remember { mutableStateOf(true) }
  var showCommentsDialog by remember { mutableStateOf(false) }
  var showShareSheet by remember { mutableStateOf(false) }

  BackHandler {
    if (showShareSheet) {
      showShareSheet = false
    } else if (showQualityPicker) {
      videoPlayerViewModel.toggleQualityPicker()
    } else {
      onBackClick()
    }
  }

  // Format seconds to mm:ss
  val currentMinutes = currentSeconds / 60
  val currentSecRem = currentSeconds % 60
  val formattedCurrentTime = String.format("%d:%02d", currentMinutes, currentSecRem)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF0F0F0F))
      .testTag("video_player_screen_root")
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // 1. Top App Bar matching mockup
      VideoPlayerTopBar(
        onBackClick = onBackClick,
        searchQuery = searchQuery,
        onSearchQueryChange = { videoPlayerViewModel.updateSearchQuery(it) },
        onCastClick = {
          Toast.makeText(context, "Scanning for Chromecast / Smart TV devices...", Toast.LENGTH_SHORT).show()
        },
        onNotificationClick = {
          Toast.makeText(context, "3 New Notifications from Youth Tube", Toast.LENGTH_SHORT).show()
        },
        onProfileClick = {
          Toast.makeText(context, "Admin & Creator Profile", Toast.LENGTH_SHORT).show()
        }
      )

      // 2. Adaptive Layout: Side-by-side on wide screens, stacked on compact screens
      BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxWidth()) {
        val isWide = maxWidth >= 800.dp

        if (isWide) {
          // Landscape Side-by-Side (exact mockup layout)
          Row(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            // Left Column: Player Box + Channel & Comments underneath
            Column(
              modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              // Main Video Box
              MainVideoPlayerBox(
                video = currentVideo,
                isPlaying = isPlaying,
                currentTimeText = formattedCurrentTime,
                currentSeconds = currentSeconds,
                isMuted = isMuted,
                isFullscreen = isFullscreen,
                isCcEnabled = isCcEnabled,
                showQualityPicker = showQualityPicker,
                selectedQuality = selectedQuality,
                likeStatus = likeStatus,
                likesCount = likesCount,
                onPlayPauseToggle = { videoPlayerViewModel.togglePlayPause() },
                onSeekFraction = { videoPlayerViewModel.seekToFraction(it) },
                onMuteToggle = { videoPlayerViewModel.toggleMute() },
                onFullscreenToggle = { videoPlayerViewModel.toggleFullscreen() },
                onCcToggle = { isCcEnabled = !isCcEnabled },
                onQualityClick = { videoPlayerViewModel.toggleQualityPicker() },
                onSelectQuality = { videoPlayerViewModel.setQuality(it) },
                onLikeClick = { videoPlayerViewModel.toggleLike() },
                onDislikeClick = { videoPlayerViewModel.toggleDislike() },
                onShareClick = { showShareSheet = true },
                onRemixClick = { Toast.makeText(context, "Remix with Youth Tube Studio!", Toast.LENGTH_SHORT).show() }
              )

              // Channel Row & Action Buttons
              ChannelActionsRow(
                video = currentVideo,
                isSubscribed = isSubscribed,
                likeStatus = likeStatus,
                likesCount = likesCount,
                onSubscribeToggle = {
                  videoPlayerViewModel.toggleSubscribe()
                  val msg = if (!isSubscribed) "Subscribed to ${currentVideo.channelName}!" else "Unsubscribed"
                  Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                },
                onLikeClick = { videoPlayerViewModel.toggleLike() },
                onDislikeClick = { videoPlayerViewModel.toggleDislike() },
                onShareClick = { showShareSheet = true },
                onDownloadClick = { Toast.makeText(context, "Downloading in 4K HD...", Toast.LENGTH_SHORT).show() }
              )

              // Google AdMob Banner Display
              AdMobBannerView(
                adHeadline = "Google AdMob · Sponsored Video Partner",
                adTagline = "Download Tata Neu & Flipkart for exclusive Youth Tube perks!"
              )

              // Description and Comments Section
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                // Description card
                DescriptionCard(
                  video = currentVideo,
                  modifier = Modifier.weight(1.2f)
                )

                // Comments preview card
                CommentsPreviewCard(
                  video = currentVideo,
                  onClick = { showCommentsDialog = true },
                  modifier = Modifier.weight(0.8f)
                )
              }
            }

            // Right Column: Next Videos Rail
            Column(
              modifier = Modifier
                .width(320.dp)
                .fillMaxHeight()
            ) {
              NextVideosSection(
                nextVideos = nextVideos,
                onVideoSelect = { videoPlayerViewModel.playNextVideo(it) }
              )
            }
          }
        } else {
          // Portrait Stacked Layout for Phone
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // 1. Video Box
            item {
              MainVideoPlayerBox(
                video = currentVideo,
                isPlaying = isPlaying,
                currentTimeText = formattedCurrentTime,
                currentSeconds = currentSeconds,
                isMuted = isMuted,
                isFullscreen = isFullscreen,
                isCcEnabled = isCcEnabled,
                showQualityPicker = showQualityPicker,
                selectedQuality = selectedQuality,
                likeStatus = likeStatus,
                likesCount = likesCount,
                onPlayPauseToggle = { videoPlayerViewModel.togglePlayPause() },
                onSeekFraction = { videoPlayerViewModel.seekToFraction(it) },
                onMuteToggle = { videoPlayerViewModel.toggleMute() },
                onFullscreenToggle = { videoPlayerViewModel.toggleFullscreen() },
                onCcToggle = { isCcEnabled = !isCcEnabled },
                onQualityClick = { videoPlayerViewModel.toggleQualityPicker() },
                onSelectQuality = { videoPlayerViewModel.setQuality(it) },
                onLikeClick = { videoPlayerViewModel.toggleLike() },
                onDislikeClick = { videoPlayerViewModel.toggleDislike() },
                onShareClick = { Toast.makeText(context, "Video link copied to clipboard!", Toast.LENGTH_SHORT).show() },
                onRemixClick = { Toast.makeText(context, "Remix with Youth Tube Studio!", Toast.LENGTH_SHORT).show() }
              )
            }

            // 2. Channel Row
            item {
              Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                ChannelActionsRow(
                  video = currentVideo,
                  isSubscribed = isSubscribed,
                  likeStatus = likeStatus,
                  likesCount = likesCount,
                  onSubscribeToggle = {
                    videoPlayerViewModel.toggleSubscribe()
                    val msg = if (!isSubscribed) "Subscribed to ${currentVideo.channelName}!" else "Unsubscribed"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                  },
                  onLikeClick = { videoPlayerViewModel.toggleLike() },
                  onDislikeClick = { videoPlayerViewModel.toggleDislike() },
                  onShareClick = { showShareSheet = true },
                  onDownloadClick = { Toast.makeText(context, "Downloading in 4K HD...", Toast.LENGTH_SHORT).show() }
                )
              }
            }

            // Google AdMob Banner Display (Mobile In-Feed)
            item {
              AdMobBannerView(
                adHeadline = "Google AdMob · Sponsored Video Partner",
                adTagline = "Download Tata Neu & Flipkart for exclusive Youth Tube perks!"
              )
            }

            // 3. Description & Comments
            item {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                DescriptionCard(video = currentVideo, modifier = Modifier.fillMaxWidth())
                CommentsPreviewCard(video = currentVideo, onClick = { showCommentsDialog = true }, modifier = Modifier.fillMaxWidth())
              }
            }

            // 4. Next Videos Header
            item {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Next Videos",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.sp
                )
                Icon(
                  imageVector = Icons.Default.KeyboardArrowRight,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }
            }

            // 5. Next Video Cards
            items(nextVideos) { item ->
              NextVideoListItem(
                item = item,
                onClick = { videoPlayerViewModel.playNextVideo(item) },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
              )
            }
          }
        }
      }
    }

    // Social Share Bottom Sheet (WhatsApp, Facebook, Instagram, Copy Link)
    if (showShareSheet) {
      SocialShareSheet(
        videoTitle = currentVideo.title,
        videoSubtitle = currentVideo.subtitle,
        videoUrl = "https://youthtube.app/watch?v=${currentVideo.id}",
        onDismiss = { showShareSheet = false }
      )
    }
  }
}

// -------------------------------------------------------------
// TOP BAR (Back, Logo, Search Bar, Cast, Notifications, Profile)
// -------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerTopBar(
  onBackClick: () -> Unit,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  onCastClick: () -> Unit,
  onNotificationClick: () -> Unit,
  onProfileClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(60.dp)
      .background(Color(0xFF0F0F0F))
      .padding(horizontal = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Back button
    IconButton(
      onClick = onBackClick,
      modifier = Modifier.testTag("btn_player_back")
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "Back",
        tint = Color.White,
        modifier = Modifier.size(24.dp)
      )
    }

    Spacer(modifier = Modifier.width(4.dp))

    // Youth Tube Logo
    Image(
      painter = painterResource(id = R.drawable.ic_youth_tube_header_logo),
      contentDescription = "Youth Tube Logo",
      modifier = Modifier
        .height(30.dp)
        .testTag("player_header_logo"),
      contentScale = ContentScale.Fit
    )

    Spacer(modifier = Modifier.width(10.dp))

    // Divider
    Box(
      modifier = Modifier
        .width(1.dp)
        .height(22.dp)
        .background(Color(0xFF333333))
    )

    Spacer(modifier = Modifier.width(12.dp))

    // Search Box
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color(0xFF1E1E1E),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C2C2C)),
      modifier = Modifier
        .weight(1f)
        .height(38.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Search",
          tint = Color(0xFFAAAAAA),
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (searchQuery.isEmpty()) "Search YOUTH TUBE..." else searchQuery,
          color = if (searchQuery.isEmpty()) Color(0xFF888888) else Color.White,
          fontSize = 13.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    Spacer(modifier = Modifier.width(12.dp))

    // Cast Icon
    IconButton(
      onClick = onCastClick,
      modifier = Modifier.size(38.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Cast,
        contentDescription = "Cast Screen",
        tint = Color.White,
        modifier = Modifier.size(22.dp)
      )
    }

    // Notifications with badge 3
    BadgedBox(
      badge = {
        Badge(
          containerColor = Color(0xFFE50914),
          contentColor = Color.White
        ) {
          Text("3", fontWeight = FontWeight.Bold, fontSize = 10.sp)
        }
      },
      modifier = Modifier.padding(horizontal = 4.dp)
    ) {
      IconButton(
        onClick = onNotificationClick,
        modifier = Modifier.size(38.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Notifications,
          contentDescription = "Notifications",
          tint = Color.White,
          modifier = Modifier.size(22.dp)
        )
      }
    }

    Spacer(modifier = Modifier.width(4.dp))

    // Profile Avatar
    Box(
      modifier = Modifier
        .size(34.dp)
        .clip(CircleShape)
        .background(Color(0xFF1E88E5))
        .clickable { onProfileClick() },
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "User Profile",
        tint = Color.White,
        modifier = Modifier.size(22.dp)
      )
    }
  }
}

// -------------------------------------------------------------
// MAIN VIDEO PLAYER BOX WITH OVERLAYS & CONTROLS
// -------------------------------------------------------------

@Composable
fun MainVideoPlayerBox(
  video: VideoDetails,
  isPlaying: Boolean,
  currentTimeText: String,
  currentSeconds: Int,
  isMuted: Boolean,
  isFullscreen: Boolean,
  isCcEnabled: Boolean,
  showQualityPicker: Boolean,
  selectedQuality: String,
  likeStatus: LikeStatus,
  likesCount: String,
  onPlayPauseToggle: () -> Unit,
  onSeekFraction: (Float) -> Unit,
  onMuteToggle: () -> Unit,
  onFullscreenToggle: () -> Unit,
  onCcToggle: () -> Unit,
  onQualityClick: () -> Unit,
  onSelectQuality: (String) -> Unit,
  onLikeClick: () -> Unit,
  onDislikeClick: () -> Unit,
  onShareClick: () -> Unit,
  onRemixClick: () -> Unit
) {
  val progressFraction = if (video.durationSeconds > 0) {
    (currentSeconds.toFloat() / video.durationSeconds.toFloat()).coerceIn(0f, 1f)
  } else 0f

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Black),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    modifier = Modifier
      .fillMaxWidth()
      .height(290.dp)
      .testTag("main_video_player_box")
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      // 1. Video Thumbnail / Frame
      Image(
        painter = painterResource(id = video.thumbnailRes),
        contentDescription = video.title,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )

      // 2. Cinematic Gradient Overlay (Top and Bottom)
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Black.copy(alpha = 0.75f),
                Color.Transparent,
                Color.Transparent,
                Color.Black.copy(alpha = 0.85f)
              )
            )
          )
      )

      // 3. Top Overlay Bar: Title, Subtitle, and 4K Badge
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, top = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = video.title,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Icon(
              imageVector = Icons.Default.KeyboardArrowRight,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
          Text(
            text = video.subtitle,
            color = Color(0xFFE0E0E0),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        // 4K Badge
        if (video.is4K) {
          Surface(
            color = Color.Black.copy(alpha = 0.7f),
            shape = RoundedCornerShape(6.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF444444))
          ) {
            Text(
              text = "4K",
              color = Color.White,
              fontWeight = FontWeight.Black,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      // 4. Center Play/Pause Floating Circular Button
      Box(
        modifier = Modifier
          .align(Alignment.Center)
          .size(62.dp)
          .clip(CircleShape)
          .background(Color.Black.copy(alpha = 0.55f))
          .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape)
          .clickable { onPlayPauseToggle() },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
          contentDescription = if (isPlaying) "Pause" else "Play",
          tint = Color.White,
          modifier = Modifier.size(36.dp)
        )
      }

      // 5. Right Vertical Action Buttons (Overlay)
      Column(
        modifier = Modifier
          .align(Alignment.CenterEnd)
          .padding(end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Like Button
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          IconButton(
            onClick = onLikeClick,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.6f))
          ) {
            Icon(
              imageVector = if (likeStatus == LikeStatus.LIKED) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
              contentDescription = "Like",
              tint = if (likeStatus == LikeStatus.LIKED) Color(0xFF00E676) else Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
          Text(text = likesCount, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }

        // Dislike Button
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          IconButton(
            onClick = onDislikeClick,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.6f))
          ) {
            Icon(
              imageVector = if (likeStatus == LikeStatus.DISLIKED) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
              contentDescription = "Dislike",
              tint = if (likeStatus == LikeStatus.DISLIKED) Color(0xFFFF1744) else Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
          Text(text = "Dislike", color = Color.White, fontSize = 10.sp)
        }

        // Share Button
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          IconButton(
            onClick = onShareClick,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.6f))
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share",
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
          Text(text = "Share", color = Color.White, fontSize = 10.sp)
        }

        // Remix Button
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          IconButton(
            onClick = onRemixClick,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.6f))
          ) {
            Icon(
              imageVector = Icons.Default.Autorenew,
              contentDescription = "Remix",
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
          Text(text = "Remix", color = Color.White, fontSize = 10.sp)
        }
      }

      // 6. Quality Selection Dialog Overlay
      if (showQualityPicker) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xEE1E1E1E),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF444444)),
          shadowElevation = 12.dp,
          modifier = Modifier
            .align(Alignment.Center)
            .padding(end = 50.dp)
            .width(180.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Quality", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
              IconButton(onClick = onQualityClick, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
              }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFF333333))

            val qualities = listOf("Auto", "1080p HD", "720p", "480p", "360p")
            qualities.forEach { q ->
              val isSel = selectedQuality == q
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(6.dp))
                  .clickable { onSelectQuality(q) }
                  .padding(vertical = 4.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, if (isSel) Color(0xFF00E676) else Color.Gray, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  if (isSel) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF00E676)))
                  }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = q,
                  color = if (isSel) Color(0xFF00E676) else Color.White,
                  fontSize = 12.sp,
                  fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                )
              }
            }
          }
        }
      }

      // Live Streaming Kannada & English Subtitles
      if (isCcEnabled) {
        val captionText = when {
          currentSeconds in 0..12 -> "ಜಯ ಭವಾನಿ, ಜಯ ಶಿವಾಜಿ! ಸ್ವರಾಜ್ಯ ಸ್ಥಾಪನೆಯ ಇತಿಹಾಸ."
          currentSeconds in 13..28 -> "ಮಹಾರಾಷ್ಟ್ರ ಮತ್ತು ಕರ್ನಾಟಕದ ಕೋಟೆಗಳ ರಕ್ಷಕ ಛತ್ರಪತಿ ಶಿವಾಜಿ ಮಹಾರಾಜ್."
          currentSeconds in 29..45 -> "Swarajya Lives On! A special documentary on YOUTH TUBE."
          else -> "ಯೂತ್ ಟ್ಯೂಬ್ - ನೈಜ ವಿಡಿಯೋ ಸ್ಟ್ರೀಮಿಂಗ್ 🇮🇳 (Live HD Streaming)"
        }
        Surface(
          color = Color.Black.copy(alpha = 0.8f),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 44.dp)
        ) {
          Text(
            text = captionText,
            color = Color(0xFFFFEB3B),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      // 7. Bottom Player Controls (Red Scrubber & Timestamps)
      Column(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 8.dp)
      ) {
        // Red Progress Scrubber Bar with Streaming Buffer Track
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
            .clickable {
              // Click to seek roughly
              onSeekFraction(0.5f)
            },
          contentAlignment = Alignment.CenterStart
        ) {
          // Track background
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(Color.White.copy(alpha = 0.3f))
          )
          // Buffered Streaming Track (ahead of played position)
          val bufferedFraction = ((progressFraction + 0.28f).coerceAtMost(1f))
          Box(
            modifier = Modifier
              .fillMaxWidth(fraction = bufferedFraction)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(Color.White.copy(alpha = 0.6f))
          )
          // Red Played Track
          Box(
            modifier = Modifier
              .fillMaxWidth(fraction = progressFraction)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(Color(0xFFE50914))
          )
          // Circular Thumb
          Box(
            modifier = Modifier
              .align(Alignment.CenterStart)
              .padding(start = (progressFraction * 260).dp.coerceAtLeast(0.dp))
              .size(12.dp)
              .clip(CircleShape)
              .background(Color(0xFFE50914))
          )
        }

        // Bottom Controls Row: Play, Speaker, Time, CC, Settings, Fullscreen
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Play/Pause icon
          IconButton(onClick = onPlayPauseToggle, modifier = Modifier.size(28.dp)) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isPlaying) "Pause" else "Play",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }

          // Volume/Mute icon
          IconButton(onClick = onMuteToggle, modifier = Modifier.size(28.dp)) {
            Icon(
              imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
              contentDescription = "Mute",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Time text: "0:35 / 12:48"
          Text(
            text = "$currentTimeText / ${video.duration}",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )

          Spacer(modifier = Modifier.weight(1f))

          // CC (Closed Captions)
          Surface(
            color = if (isCcEnabled) Color.White.copy(alpha = 0.25f) else Color.Transparent,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .clickable { onCcToggle() }
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "CC",
              color = if (isCcEnabled) Color.White else Color.Gray,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Quality Gear Settings icon
          IconButton(onClick = onQualityClick, modifier = Modifier.size(28.dp)) {
            Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = "Quality Settings",
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }

          // Fullscreen icon
          IconButton(onClick = onFullscreenToggle, modifier = Modifier.size(28.dp)) {
            Icon(
              imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
              contentDescription = "Fullscreen",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// CHANNEL ROW & ACTION BUTTONS
// -------------------------------------------------------------

@Composable
fun ChannelActionsRow(
  video: VideoDetails,
  isSubscribed: Boolean,
  likeStatus: LikeStatus,
  likesCount: String,
  onSubscribeToggle: () -> Unit,
  onLikeClick: () -> Unit,
  onDislikeClick: () -> Unit,
  onShareClick: () -> Unit,
  onDownloadClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Channel Avatar Emblem
    Image(
      painter = painterResource(id = R.drawable.ic_youth_tube_logo),
      contentDescription = video.channelName,
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
    )

    Spacer(modifier = Modifier.width(10.dp))

    // Channel Name & Subs
    Column {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = video.channelName,
          color = Color.White,
          fontWeight = FontWeight.Black,
          fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "Verified Channel",
          tint = Color(0xFF00B0FF),
          modifier = Modifier.size(14.dp)
        )
      }
      Text(
        text = video.channelSubscribers,
        color = Color(0xFFAAAAAA),
        fontSize = 11.sp
      )
    }

    Spacer(modifier = Modifier.width(14.dp))

    // Subscribe Button
    Button(
      onClick = onSubscribeToggle,
      colors = ButtonDefaults.buttonColors(
        containerColor = if (isSubscribed) Color(0xFF272727) else Color(0xFFE50914)
      ),
      shape = RoundedCornerShape(20.dp),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
      modifier = Modifier.height(36.dp)
    ) {
      Text(
        text = if (isSubscribed) "Subscribed" else "Subscribe",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
      )
    }

    Spacer(modifier = Modifier.weight(1f))

    // Like / Dislike Capsule Pill
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color(0xFF272727),
      modifier = Modifier.height(36.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp)
      ) {
        // Like
        Row(
          modifier = Modifier
            .clickable { onLikeClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (likeStatus == LikeStatus.LIKED) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
            contentDescription = "Like",
            tint = if (likeStatus == LikeStatus.LIKED) Color(0xFF00E676) else Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = likesCount, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Box(
          modifier = Modifier
            .width(1.dp)
            .height(18.dp)
            .background(Color(0xFF444444))
        )

        // Dislike
        IconButton(
          onClick = onDislikeClick,
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = if (likeStatus == LikeStatus.DISLIKED) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
            contentDescription = "Dislike",
            tint = if (likeStatus == LikeStatus.DISLIKED) Color(0xFFFF1744) else Color.White,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Share Pill
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color(0xFF272727),
      modifier = Modifier
        .height(36.dp)
        .clickable { onShareClick() }
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp)
      ) {
        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "Share", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Download Pill
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color(0xFF272727),
      modifier = Modifier
        .height(36.dp)
        .clickable { onDownloadClick() }
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp)
      ) {
        Icon(Icons.Default.Download, contentDescription = "Download", tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "Download", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
      }
    }
  }
}

// -------------------------------------------------------------
// DESCRIPTION & HASHTAGS CARD
// -------------------------------------------------------------

@Composable
fun DescriptionCard(video: VideoDetails, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = Color(0xFF1F1F1F),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(
        text = video.headline,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        video.hashtags.forEach { tag ->
          Text(
            text = tag,
            color = Color(0xFF29B6F6),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// COMMENTS PREVIEW CARD
// -------------------------------------------------------------

@Composable
fun CommentsPreviewCard(video: VideoDetails, onClick: () -> Unit, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = Color(0xFF1F1F1F),
    modifier = modifier.clickable { onClick() }
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Comments",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = video.commentsCount,
            color = Color(0xFFAAAAAA),
            fontSize = 12.sp
          )
        }
        Icon(
          imageVector = Icons.Default.MoreVert,
          contentDescription = "Options",
          tint = Color(0xFFAAAAAA),
          modifier = Modifier.size(16.dp)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        // Comment user avatar
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color(0xFFE50914)),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = video.topCommentText,
          color = Color(0xFFDDDDDD),
          fontSize = 12.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

// -------------------------------------------------------------
// NEXT VIDEOS RAIL SECTION
// -------------------------------------------------------------

@Composable
fun NextVideosSection(
  nextVideos: List<NextVideoItem>,
  onVideoSelect: (NextVideoItem) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(vertical = 4.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Next Videos",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
        Icon(
          imageVector = Icons.Default.KeyboardArrowRight,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(bottom = 16.dp)
    ) {
      items(nextVideos) { item ->
        NextVideoListItem(item = item, onClick = { onVideoSelect(item) })
      }
    }
  }
}

// -------------------------------------------------------------
// NEXT VIDEO CARD ITEM
// -------------------------------------------------------------

@Composable
fun NextVideoListItem(
  item: NextVideoItem,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = Color(0xFF181818),
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Row(
      modifier = Modifier.padding(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Thumbnail with duration tag
      Box(
        modifier = Modifier
          .width(120.dp)
          .height(72.dp)
          .clip(RoundedCornerShape(8.dp))
      ) {
        Image(
          painter = painterResource(id = item.thumbnailRes),
          contentDescription = item.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Duration badge
        Surface(
          color = Color.Black.copy(alpha = 0.8f),
          shape = RoundedCornerShape(4.dp),
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(4.dp)
        ) {
          Text(
            text = item.duration,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Video Info
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.title,
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = item.channel,
            color = Color(0xFFAAAAAA),
            fontSize = 11.sp
          )
          Spacer(modifier = Modifier.width(3.dp))
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Verified",
            tint = Color(0xFF00B0FF),
            modifier = Modifier.size(11.dp)
          )
        }

        Text(
          text = item.views,
          color = Color(0xFF888888),
          fontSize = 10.sp
        )
      }
    }
  }
}
