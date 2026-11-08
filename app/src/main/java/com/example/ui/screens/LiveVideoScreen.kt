package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewGroup
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.model.FloatingReaction
import com.example.model.LiveChatMessage
import com.example.model.LiveStreamChannel
import com.example.model.LiveVideoFilter
import com.example.viewmodel.DevPulseViewModel
import com.example.viewmodel.LiveVideoViewModel
import java.util.Locale

@Composable
fun LiveVideoScreen(
  devPulseViewModel: DevPulseViewModel,
  videoViewModel: LiveVideoViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val studioMode by videoViewModel.studioMode.collectAsState()

  // Camera permissions
  var hasCameraPermission by remember {
    mutableStateOf(
      ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    )
  }

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { perms ->
    hasCameraPermission = perms[Manifest.permission.CAMERA] == true
  }

  LaunchedEffect(Unit) {
    if (!hasCameraPermission) {
      permissionLauncher.launch(
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
      )
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("live_video_screen")
  ) {
    // Youth Tube Brand Header
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp)
        .testTag("youth_tube_brand_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
      ),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        androidx.compose.foundation.Image(
          painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_youth_tube_logo),
          contentDescription = "Youth Tube Logo",
          modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "YOUTH TUBE",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              color = Color(0xFFFF1744),
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = "LIVE",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }
          Text(
            text = "Watch · Create · Share | Your Video, Our World!",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    // Mode Switcher Tab Row
    TabRow(
      selectedTabIndex = studioMode,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.primary,
      modifier = Modifier.fillMaxWidth().testTag("video_mode_tabs")
    ) {
      Tab(
        selected = studioMode == 0,
        onClick = { videoViewModel.setStudioMode(0) },
        icon = { Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(20.dp)) },
        text = { Text("Camera Studio") },
        modifier = Modifier.testTag("tab_camera_studio")
      )
      Tab(
        selected = studioMode == 1,
        onClick = { videoViewModel.setStudioMode(1) },
        icon = { Icon(Icons.Default.LiveTv, contentDescription = null, modifier = Modifier.size(20.dp)) },
        text = { Text("Live Broadcasts") },
        modifier = Modifier.testTag("tab_live_broadcasts")
      )
    }

    if (studioMode == 0) {
      CameraStudioView(
        viewModel = videoViewModel,
        hasPermission = hasCameraPermission,
        onRequestPermission = {
          permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
        }
      )
    } else {
      LiveBroadcastsView(viewModel = videoViewModel)
    }
  }
}

// -------------------------------------------------------------
// CAMERA STUDIO VIEW
// -------------------------------------------------------------

@Composable
fun CameraStudioView(
  viewModel: LiveVideoViewModel,
  hasPermission: Boolean,
  onRequestPermission: () -> Unit
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  val isBackCamera by viewModel.isBackCamera.collectAsState()
  val isTorchOn by viewModel.isTorchOn.collectAsState()
  val activeFilter by viewModel.activeFilter.collectAsState()
  val zoomRatio by viewModel.zoomRatio.collectAsState()
  val showGrid by viewModel.showGrid.collectAsState()
  val isRecording by viewModel.isRecording.collectAsState()
  val recordSeconds by viewModel.recordingDurationSeconds.collectAsState()
  val vuLevels by viewModel.audioVuLevels.collectAsState()
  val fps by viewModel.cameraFps.collectAsState()

  var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
  var cameraHardwareAvailable by remember { mutableStateOf(true) }

  // Update torch on camera
  LaunchedEffect(isTorchOn, cameraControl) {
    try {
      cameraControl?.enableTorch(isTorchOn)
    } catch (_: Exception) {}
  }

  // Update zoom
  LaunchedEffect(zoomRatio, cameraControl) {
    try {
      cameraControl?.setLinearZoom(zoomRatio)
    } catch (_: Exception) {}
  }

  Column(modifier = Modifier.fillMaxSize()) {
    // Top Camera Viewport Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .background(Color.Black)
    ) {
      if (hasPermission && cameraHardwareAvailable) {
        AndroidView(
          factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
              layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
              )
              scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
              try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                  it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = if (isBackCamera) {
                  CameraSelector.DEFAULT_BACK_CAMERA
                } else {
                  CameraSelector.DEFAULT_FRONT_CAMERA
                }

                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                  lifecycleOwner,
                  cameraSelector,
                  preview
                )
                cameraControl = camera.cameraControl
              } catch (e: Exception) {
                // If emulator doesn't have camera hardware, seamlessly fall back to simulation
                cameraHardwareAvailable = false
              }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
          },
          modifier = Modifier.fillMaxSize().testTag("camerax_preview_view")
        )
      } else {
        // Fallback synthetic optics simulator
        SyntheticCameraFeed(
          filter = activeFilter,
          fps = fps,
          onRequestPermission = onRequestPermission,
          hasPermission = hasPermission
        )
      }

      // Filter Overlays (Cyberpunk, Night Vision, Noir, VHS, Grid)
      CameraFilterCanvas(
        filter = activeFilter,
        showGrid = showGrid,
        modifier = Modifier.fillMaxSize()
      )

      // HUD Header: REC status, Resolution, FPS, Active Filter Badge
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp)
          .align(Alignment.TopCenter),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Recording pill
        if (isRecording) {
          RecordingBadge(durationSeconds = recordSeconds)
        } else {
          Surface(
            color = Color.Black.copy(alpha = 0.65f),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF00E5A3))
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("STANDBY", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
          }
        }

        // Telemetry tags
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          HudChip("1080p")
          HudChip("$fps FPS")
          HudChip(activeFilter.badge, Color(0xFF00D2FF))
        }
      }

      // Right action rail: Camera flip, Torch, Grid toggle
      Column(
        modifier = Modifier
          .align(Alignment.CenterEnd)
          .padding(end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        CameraRailButton(
          icon = Icons.Default.Cameraswitch,
          description = "Flip Camera",
          isActive = false,
          onClick = { viewModel.toggleCameraFacing() },
          testTag = "btn_flip_camera"
        )
        CameraRailButton(
          icon = Icons.Default.FlashOn,
          description = "Torch",
          isActive = isTorchOn,
          onClick = { viewModel.toggleTorch() },
          testTag = "btn_toggle_torch"
        )
        CameraRailButton(
          icon = Icons.Default.GridOn,
          description = "Grid",
          isActive = showGrid,
          onClick = { viewModel.toggleGrid() },
          testTag = "btn_toggle_grid"
        )
      }

      // Bottom Audio VU Meter inside Viewport
      AudioVuMeterBar(
        levels = vuLevels,
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .padding(horizontal = 16.dp, vertical = 8.dp)
      )
    }

    // Bottom Studio Control Deck
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // 1. Zoom Slider Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("1x", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Slider(
          value = zoomRatio,
          onValueChange = { viewModel.setZoom(it) },
          modifier = Modifier.weight(1f).padding(horizontal = 8.dp).testTag("camera_zoom_slider"),
          colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary
          )
        )
        Text("5x", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      // 2. Filter Selector Chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(LiveVideoFilter.values()) { filter ->
          FilterChip(
            selected = activeFilter == filter,
            onClick = { viewModel.setFilter(filter) },
            label = { Text(filter.label) },
            modifier = Modifier.testTag("filter_chip_${filter.name.lowercase()}"),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
            )
          )
        }
      }

      // 3. Shutter / Record Control Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Shutter snap button
        IconButton(
          onClick = {
            // Snapshot haptic
          },
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .testTag("btn_camera_snapshot")
        ) {
          Icon(Icons.Default.RadioButtonChecked, contentDescription = "Snap Photo", tint = MaterialTheme.colorScheme.onSurface)
        }

        // Master Record Button
        Box(
          modifier = Modifier
            .size(68.dp)
            .clip(CircleShape)
            .border(3.dp, Color.White, CircleShape)
            .padding(4.dp)
            .clip(CircleShape)
            .background(if (isRecording) Color(0xFFFF5252) else Color(0xFFFF1744))
            .clickable { viewModel.toggleRecording() }
            .testTag("btn_master_record"),
          contentAlignment = Alignment.Center
        ) {
          if (isRecording) {
            Box(
              modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
            )
          } else {
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.White)
            )
          }
        }

        // Flash Torch indicator
        IconButton(
          onClick = { viewModel.toggleTorch() },
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (isTorchOn) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
            .testTag("btn_bottom_torch")
        ) {
          Icon(
            Icons.Default.FlashOn,
            contentDescription = "Torch",
            tint = if (isTorchOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// LIVE BROADCASTS & STREAMING VIEW
// -------------------------------------------------------------

@Composable
fun LiveBroadcastsView(viewModel: LiveVideoViewModel) {
  val channels = viewModel.channels
  val selectedChannel by viewModel.selectedChannel.collectAsState()
  val isPlaying by viewModel.isStreamPlaying.collectAsState()
  val isMuted by viewModel.isStreamMuted.collectAsState()
  val viewerCount by viewModel.liveViewerCount.collectAsState()
  val chatMessages by viewModel.chatMessages.collectAsState()
  val floatingReactions by viewModel.floatingReactions.collectAsState()

  var inputMessage by remember { mutableStateOf("") }
  val chatListState = rememberLazyListState()

  LaunchedEffect(chatMessages.size) {
    if (chatMessages.isNotEmpty()) {
      chatListState.animateScrollToItem(chatMessages.size - 1)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("live_broadcasts_column")
  ) {
    // 1. Live Video Player Screen
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(230.dp)
        .background(Color.Black)
    ) {
      // Native Video Player
      AndroidView(
        factory = { ctx ->
          VideoView(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
            )
            setVideoURI(Uri.parse(selectedChannel.streamUrl))
            setOnPreparedListener { mp ->
              mp.isLooping = true
              mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
              if (isPlaying) mp.start()
              if (isMuted) mp.setVolume(0f, 0f) else mp.setVolume(1f, 1f)
            }
          }
        },
        update = { videoView ->
          if (isPlaying) {
            if (!videoView.isPlaying) videoView.start()
          } else {
            if (videoView.isPlaying) videoView.pause()
          }
        },
        modifier = Modifier.fillMaxSize().testTag("live_video_player")
      )

      // Top Overlay: Live Tag, Viewers, Quality
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp)
          .align(Alignment.TopCenter),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            color = Color(0xFFFF1744),
            shape = RoundedCornerShape(6.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(Color.White)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("LIVE", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
            }
          }

          Surface(
            color = Color.Black.copy(alpha = 0.6f),
            shape = RoundedCornerShape(6.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${viewerCount / 1000}.${(viewerCount % 1000) / 100}K",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          HudChip(selectedChannel.resolution, Color(0xFF00E5A3))
          HudChip("${selectedChannel.bitrateKbps} Kbps")
        }
      }

      // Floating Reactions Overlay
      FloatingReactionsContainer(reactions = floatingReactions)

      // Bottom Player Controls (Play/Pause, Mute)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .background(
            Brush.verticalGradient(
              listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
            )
          )
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = { viewModel.toggleStreamPlayback() },
            modifier = Modifier.size(32.dp).testTag("btn_toggle_play")
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = "Playback",
              tint = Color.White
            )
          }
          IconButton(
            onClick = { viewModel.toggleStreamMute() },
            modifier = Modifier.size(32.dp).testTag("btn_toggle_mute")
          ) {
            Icon(
              imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
              contentDescription = "Mute",
              tint = Color.White
            )
          }
          Text(
            text = selectedChannel.title,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            modifier = Modifier.padding(start = 6.dp)
          )
        }

        IconButton(
          onClick = {},
          modifier = Modifier.size(32.dp).testTag("btn_fullscreen")
        ) {
          Icon(Icons.Default.Fullscreen, contentDescription = "Fullscreen", tint = Color.White)
        }
      }
    }

    // 2. Stream Channel Switcher
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val filteredChannels = remember(channels, selectedLanguage) {
      if (selectedLanguage == "All") channels else channels.filter { it.language == selectedLanguage }
    }

    Column(modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Live Channels",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          listOf("All", "ಕನ್ನಡ", "English", "हिन्दी").forEach { lang ->
            val isSelected = selectedLanguage == lang
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clickable { viewModel.setLanguageFilter(lang) }
                .testTag("lang_chip_$lang")
            ) {
              Text(
                text = lang,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(6.dp))
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth().testTag("live_channels_row")
      ) {
        items(filteredChannels, key = { it.id }) { ch ->
          val isSelected = ch.id == selectedChannel.id
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier
              .clickable { viewModel.selectChannel(ch) }
              .testTag("channel_card_${ch.id}")
          ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = ch.title,
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                  maxLines = 1
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = ch.language,
                    fontSize = 9.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
              Text(
                text = "${ch.creator} · ${ch.resolution} · ${ch.viewerCount / 1000}K live",
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    // 3. Live Stream Interactive Chat Feed
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(12.dp)
        .testTag("live_chat_container"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Live Stream Chat",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Real-time viewer reactions",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Chat list
        LazyColumn(
          state = chatListState,
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .testTag("chat_messages_list"),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(chatMessages, key = { it.id }) { msg ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.Top
            ) {
              Text(
                text = msg.author,
                color = DevPulseViewModel.parseColorSafe(msg.authorColorHex, MaterialTheme.colorScheme.primary),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
              )
              if (msg.isVerified) {
                Spacer(modifier = Modifier.width(4.dp))
                Text("✓", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp)
              }
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = msg.message,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }

        // Floating Reaction Quick Buttons
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          listOf("❤️", "🔥", "🚀", "💡", "👏").forEach { emoji ->
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { viewModel.triggerReaction(emoji) }
                .testTag("reaction_$emoji"),
              contentAlignment = Alignment.Center
            ) {
              Text(text = emoji, fontSize = 16.sp)
            }
          }
        }

        // Chat input row
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = inputMessage,
            onValueChange = { inputMessage = it },
            placeholder = { Text("Send a chat message...", fontSize = 12.sp) },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.weight(1f).testTag("chat_input_field")
          )
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(
            onClick = {
              if (inputMessage.isNotBlank()) {
                viewModel.sendChatMessage(inputMessage)
                inputMessage = ""
              }
            },
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary)
              .testTag("btn_send_chat")
          ) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// HELPER COMPOSABLES & CAMERA HUD
// -------------------------------------------------------------

@Composable
fun SyntheticCameraFeed(
  filter: LiveVideoFilter,
  fps: Int,
  hasPermission: Boolean,
  onRequestPermission: () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "synthFeed")
  val scanY by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "scanLine"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF070B14))
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height

      // Background geometric optical grid
      val gridStep = 40.dp.toPx()
      for (x in 0..(w / gridStep).toInt()) {
        drawLine(
          color = Color(0x1A00D2FF),
          start = Offset(x * gridStep, 0f),
          end = Offset(x * gridStep, h),
          strokeWidth = 1f
        )
      }
      for (y in 0..(h / gridStep).toInt()) {
        drawLine(
          color = Color(0x1A00D2FF),
          start = Offset(0f, y * gridStep),
          end = Offset(w, y * gridStep),
          strokeWidth = 1f
        )
      }

      // Moving optical scanning radar line
      val scanPos = scanY * h
      drawLine(
        color = Color(0x8000D2FF),
        start = Offset(0f, scanPos),
        end = Offset(w, scanPos),
        strokeWidth = 2.dp.toPx()
      )

      // Center crosshair optics
      val cx = w / 2f
      val cy = h / 2f
      val crossSize = 36.dp.toPx()
      drawLine(Color(0xFF00D2FF), Offset(cx - crossSize, cy), Offset(cx + crossSize, cy), 1.5f)
      drawLine(Color(0xFF00D2FF), Offset(cx, cy - crossSize), Offset(cx, cy + crossSize), 1.5f)
      drawCircle(Color(0xFF00D2FF), radius = 24.dp.toPx(), center = Offset(cx, cy), style = Stroke(1.5f))
      drawCircle(Color(0x3300D2FF), radius = 64.dp.toPx(), center = Offset(cx, cy), style = Stroke(1f))
    }

    Column(
      modifier = Modifier
        .align(Alignment.Center)
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = "OPTICAL SYNTHETIC SIMULATOR",
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = Color(0xFF00D2FF)
      )
      Text(
        text = if (!hasPermission) "Camera Permission Required for Live Hardware" else "Active Test Signal: 1080p @ ${fps}FPS",
        fontFamily = FontFamily.Monospace,
        fontSize = 11.sp,
        color = Color.White.copy(alpha = 0.7f)
      )

      if (!hasPermission) {
        Spacer(modifier = Modifier.height(4.dp))
        Button(
          onClick = onRequestPermission,
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D2FF)),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Text("Grant Camera Permission", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      }
    }
  }
}

@Composable
fun CameraFilterCanvas(
  filter: LiveVideoFilter,
  showGrid: Boolean,
  modifier: Modifier = Modifier
) {
  Canvas(modifier = modifier) {
    val w = size.width
    val h = size.height

    // 1. Rule of Thirds Grid
    if (showGrid) {
      val x1 = w / 3f
      val x2 = (w / 3f) * 2f
      val y1 = h / 3f
      val y2 = (h / 3f) * 2f

      drawLine(Color.White.copy(alpha = 0.25f), Offset(x1, 0f), Offset(x1, h), 1f)
      drawLine(Color.White.copy(alpha = 0.25f), Offset(x2, 0f), Offset(x2, h), 1f)
      drawLine(Color.White.copy(alpha = 0.25f), Offset(0f, y1), Offset(w, y1), 1f)
      drawLine(Color.White.copy(alpha = 0.25f), Offset(0f, y2), Offset(w, y2), 1f)
    }

    // 2. Filter-specific overlays
    when (filter) {
      LiveVideoFilter.CYBERPUNK -> {
        // Cyan and violet corner brackets
        val cornerLen = 28.dp.toPx()
        val cColor = Color(0xFF00D2FF)
        // Top-left
        drawLine(cColor, Offset(16f, 16f), Offset(16f + cornerLen, 16f), 3f)
        drawLine(cColor, Offset(16f, 16f), Offset(16f, 16f + cornerLen), 3f)
        // Top-right
        drawLine(cColor, Offset(w - 16f, 16f), Offset(w - 16f - cornerLen, 16f), 3f)
        drawLine(cColor, Offset(w - 16f, 16f), Offset(w - 16f, 16f + cornerLen), 3f)
        // Bottom-left
        drawLine(cColor, Offset(16f, h - 16f), Offset(16f + cornerLen, h - 16f), 3f)
        drawLine(cColor, Offset(16f, h - 16f), Offset(16f, h - 16f - cornerLen), 3f)
        // Bottom-right
        drawLine(cColor, Offset(w - 16f, h - 16f), Offset(w - 16f - cornerLen, h - 16f), 3f)
        drawLine(cColor, Offset(w - 16f, h - 16f), Offset(w - 16f, h - 16f - cornerLen), 3f)
      }
      LiveVideoFilter.NIGHT_VISION -> {
        drawRect(Color(0x3300E5A3))
      }
      LiveVideoFilter.NOIR -> {
        drawRect(Color(0x26000000))
      }
      LiveVideoFilter.RETRO_VHS -> {
        // Subtle scanlines
        var y = 0f
        while (y < h) {
          drawLine(Color.Black.copy(alpha = 0.15f), Offset(0f, y), Offset(w, y), 1f)
          y += 6f
        }
      }
      LiveVideoFilter.CLEAN -> {
        // Minimalist pro marks
      }
    }
  }
}

@Composable
fun AudioVuMeterBar(
  levels: List<Float>,
  modifier: Modifier = Modifier
) {
  Surface(
    color = Color.Black.copy(alpha = 0.6f),
    shape = RoundedCornerShape(8.dp),
    modifier = modifier
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text("VU", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
      levels.forEach { level ->
        val barColor = when {
          level > 0.8f -> Color(0xFFFF5252)
          level > 0.5f -> Color(0xFFFFD600)
          else -> Color(0xFF00E5A3)
        }
        Box(
          modifier = Modifier
            .weight(1f)
            .height(14.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White.copy(alpha = 0.1f)),
          contentAlignment = Alignment.BottomCenter
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight(level.coerceIn(0.1f, 1f))
              .clip(RoundedCornerShape(2.dp))
              .background(barColor)
          )
        }
      }
    }
  }
}

@Composable
fun FloatingReactionsContainer(reactions: List<FloatingReaction>) {
  Box(modifier = Modifier.fillMaxSize()) {
    reactions.forEach { reaction ->
      val animY = remember { Animatable(1f) }
      val animAlpha = remember { Animatable(1f) }

      LaunchedEffect(reaction.id) {
        animY.animateTo(0.15f, animationSpec = tween(1800, easing = LinearEasing))
      }
      LaunchedEffect(reaction.id) {
        animAlpha.animateTo(0f, animationSpec = tween(1800, delayMillis = 600, easing = LinearEasing))
      }

      Text(
        text = reaction.emoji,
        fontSize = 24.sp,
        modifier = Modifier
          .align(Alignment.BottomStart)
          .offset {
            IntOffset(
              x = (reaction.xOffsetRatio * 800).toInt(),
              y = -(animY.value * 500).toInt()
            )
          }
          .alpha(animAlpha.value)
      )
    }
  }
}

@Composable
fun RecordingBadge(durationSeconds: Int) {
  val mins = durationSeconds / 60
  val secs = durationSeconds % 60
  val timeText = String.format(Locale.US, "%02d:%02d", mins, secs)

  val infiniteTransition = rememberInfiniteTransition(label = "recDot")
  val dotAlpha by infiniteTransition.animateFloat(
    initialValue = 0.2f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(600, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "dotAlpha"
  )

  Surface(
    color = Color.Black.copy(alpha = 0.75f),
    shape = RoundedCornerShape(8.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF1744))
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Box(
        modifier = Modifier
          .size(8.dp)
          .clip(CircleShape)
          .background(Color(0xFFFF1744).copy(alpha = dotAlpha))
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text("REC $timeText", color = Color(0xFFFF1744), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
    }
  }
}

@Composable
fun HudChip(text: String, accentColor: Color = Color.White) {
  Surface(
    color = Color.Black.copy(alpha = 0.65f),
    shape = RoundedCornerShape(6.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
  ) {
    Text(
      text = text,
      color = accentColor,
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
    )
  }
}

@Composable
fun CameraRailButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  description: String,
  isActive: Boolean,
  onClick: () -> Unit,
  testTag: String
) {
  IconButton(
    onClick = onClick,
    modifier = Modifier
      .size(42.dp)
      .clip(CircleShape)
      .background(if (isActive) Color(0xFF00D2FF).copy(alpha = 0.35f) else Color.Black.copy(alpha = 0.6f))
      .border(1.dp, if (isActive) Color(0xFF00D2FF) else Color.White.copy(alpha = 0.2f), CircleShape)
      .testTag(testTag)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = description,
      tint = if (isActive) Color(0xFF00D2FF) else Color.White,
      modifier = Modifier.size(20.dp)
    )
  }
}
