package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.FloatingReaction
import com.example.model.LiveChatMessage
import com.example.model.LiveStreamChannel
import com.example.model.LiveVideoFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

class LiveVideoViewModel : ViewModel() {

  // Mode: 0 = Camera Studio, 1 = Live Streams Broadcast
  private val _studioMode = MutableStateFlow(0)
  val studioMode: StateFlow<Int> = _studioMode.asStateFlow()

  // Camera Studio State
  private val _isBackCamera = MutableStateFlow(true)
  val isBackCamera: StateFlow<Boolean> = _isBackCamera.asStateFlow()

  private val _isTorchOn = MutableStateFlow(false)
  val isTorchOn: StateFlow<Boolean> = _isTorchOn.asStateFlow()

  private val _activeFilter = MutableStateFlow(LiveVideoFilter.CLEAN)
  val activeFilter: StateFlow<LiveVideoFilter> = _activeFilter.asStateFlow()

  private val _zoomRatio = MutableStateFlow(0f) // 0.0 to 1.0 linear zoom
  val zoomRatio: StateFlow<Float> = _zoomRatio.asStateFlow()

  private val _showGrid = MutableStateFlow(true)
  val showGrid: StateFlow<Boolean> = _showGrid.asStateFlow()

  private val _isRecording = MutableStateFlow(false)
  val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

  private val _recordingDurationSeconds = MutableStateFlow(0)
  val recordingDurationSeconds: StateFlow<Int> = _recordingDurationSeconds.asStateFlow()

  private val _audioVuLevels = MutableStateFlow(List(16) { 0.2f })
  val audioVuLevels: StateFlow<List<Float>> = _audioVuLevels.asStateFlow()

  private val _cameraFps = MutableStateFlow(60)
  val cameraFps: StateFlow<Int> = _cameraFps.asStateFlow()

  // Live Stream Channels
  // Live Stream Channels with Kannada, English, and Hindi matching the Youth Tube logo
  val channels = listOf(
    LiveStreamChannel(
      id = "kannada_creator_hub",
      title = "ಬೆಂಗಳೂರು ಯುವ ಕ್ರಿಯೇಟರ್ ಲೈವ್ (Bengaluru Youth Creator Hub)",
      creator = "ಕನ್ನಡ Youth Hub",
      category = "ಕನ್ನಡ Creators",
      viewerCount = 35840,
      resolution = "1080p60",
      bitrateKbps = 6200,
      fps = 60,
      streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
      description = "ಯುವ ಟ್ಯೂಬ್ ಲೈವ್ ಸ್ಟ್ರೀಮಿಂಗ್: ಯುವಕರ ಕ್ರಿಯೇಟಿವ್ ಪ್ರಾಜೆಕ್ಟ್ ಮತ್ತು ವ್ಲಾಗ್ಸ್.",
      language = "ಕನ್ನಡ"
    ),
    LiveStreamChannel(
      id = "hindi_spotlight_live",
      title = "भारत युवा लाइव शो (India Youth Spotlight Live)",
      creator = "हिन्दी Youth Tube",
      category = "हिन्दी Spotlight",
      viewerCount = 51200,
      resolution = "4K HDR",
      bitrateKbps = 8400,
      fps = 60,
      streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
      description = "युवा ट्यूब लाइव - आपका वीडियो, हमारी दुनिया! लाइव टॉक शो व म्यूजिक.",
      language = "हिन्दी"
    ),
    LiveStreamChannel(
      id = "orbital_station",
      title = "International Space Orbit 24/7",
      creator = "AstroVision Live",
      category = "Science & Space",
      viewerCount = 28430,
      resolution = "1080p60",
      bitrateKbps = 6400,
      fps = 60,
      streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
      description = "Live high-definition orbital telemetry feed passing over twilight terminator.",
      language = "English"
    ),
    LiveStreamChannel(
      id = "kannada_nature_live",
      title = "ಪಶ್ಚಿಮ ಘಟ್ಟಗಳ ಪ್ರಕೃತಿ ಲೈವ್ (Western Ghats Wildlife 4K)",
      creator = "ಕನ್ನಡ Nature Cam",
      category = "Nature",
      viewerCount = 18920,
      resolution = "1080p60",
      bitrateKbps = 5900,
      fps = 60,
      streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
      description = "ಕರ್ನಾಟಕದ ಹಸಿರು ವನಸಿರಿ ಮತ್ತು ಜಲಪಾತಗಳ ನೇರ ಪ್ರಸಾರ.",
      language = "ಕನ್ನಡ"
    ),
    LiveStreamChannel(
      id = "android_dev_summit",
      title = "Global Tech & Video Creator Summit 2026",
      creator = "Youth Tube Studio",
      category = "Technology",
      viewerCount = 42190,
      resolution = "4K HDR",
      bitrateKbps = 9200,
      fps = 60,
      streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
      description = "Live keynote and technical architecture deep dive into modern Android & CameraX.",
      language = "English"
    )
  )

  private val _selectedLanguage = MutableStateFlow("All")
  val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

  fun setLanguageFilter(lang: String) {
    _selectedLanguage.value = lang
    val matching = if (lang == "All") channels else channels.filter { it.language == lang }
    if (matching.isNotEmpty() && matching.none { it.id == _selectedChannel.value.id }) {
      _selectedChannel.value = matching.first()
    }
  }

  private val _selectedChannel = MutableStateFlow(channels[0])
  val selectedChannel: StateFlow<LiveStreamChannel> = _selectedChannel.asStateFlow()

  private val _isStreamPlaying = MutableStateFlow(true)
  val isStreamPlaying: StateFlow<Boolean> = _isStreamPlaying.asStateFlow()

  private val _isStreamMuted = MutableStateFlow(false)
  val isStreamMuted: StateFlow<Boolean> = _isStreamMuted.asStateFlow()

  private val _liveViewerCount = MutableStateFlow(channels[0].viewerCount)
  val liveViewerCount: StateFlow<Int> = _liveViewerCount.asStateFlow()

  private val _chatMessages = MutableStateFlow<List<LiveChatMessage>>(emptyList())
  val chatMessages: StateFlow<List<LiveChatMessage>> = _chatMessages.asStateFlow()

  private val _floatingReactions = MutableStateFlow<List<FloatingReaction>>(emptyList())
  val floatingReactions: StateFlow<List<FloatingReaction>> = _floatingReactions.asStateFlow()

  init {
    initDefaultChat()
    startTelemetryLoop()
    startChatSimulationLoop()
  }

  fun setStudioMode(mode: Int) {
    _studioMode.value = mode
  }

  fun toggleCameraFacing() {
    _isBackCamera.value = !_isBackCamera.value
  }

  fun toggleTorch() {
    _isTorchOn.value = !_isTorchOn.value
  }

  fun setFilter(filter: LiveVideoFilter) {
    _activeFilter.value = filter
  }

  fun setZoom(zoom: Float) {
    _zoomRatio.value = zoom.coerceIn(0f, 1f)
  }

  fun toggleGrid() {
    _showGrid.value = !_showGrid.value
  }

  fun toggleRecording() {
    val nextState = !_isRecording.value
    _isRecording.value = nextState
    if (nextState) {
      _recordingDurationSeconds.value = 0
    }
  }

  fun selectChannel(channel: LiveStreamChannel) {
    _selectedChannel.value = channel
    _liveViewerCount.value = channel.viewerCount
    _isStreamPlaying.value = true
  }

  fun toggleStreamPlayback() {
    _isStreamPlaying.value = !_isStreamPlaying.value
  }

  fun toggleStreamMute() {
    _isStreamMuted.value = !_isStreamMuted.value
  }

  fun sendChatMessage(text: String) {
    if (text.isBlank()) return
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val userMsg = LiveChatMessage(
      id = UUID.randomUUID().toString(),
      author = "You (Dev)",
      message = text.trim(),
      timestamp = time,
      isVerified = true,
      authorColorHex = "#00D2FF"
    )
    _chatMessages.value = (_chatMessages.value + userMsg).takeLast(40)
  }

  fun triggerReaction(emoji: String) {
    val reaction = FloatingReaction(
      id = System.currentTimeMillis() + Random.nextLong(1000),
      emoji = emoji,
      xOffsetRatio = Random.nextFloat() * 0.7f + 0.15f
    )
    _floatingReactions.value = (_floatingReactions.value + reaction).takeLast(15)

    // Remove old reactions after 2 seconds
    viewModelScope.launch {
      delay(2000)
      _floatingReactions.value = _floatingReactions.value.filterNot { it.id == reaction.id }
    }
  }

  private fun startTelemetryLoop() {
    viewModelScope.launch {
      while (isActive) {
        delay(150)
        // VU meter level fluctuations
        _audioVuLevels.value = List(16) {
          if (_isRecording.value || _studioMode.value == 0) {
            Random.nextFloat() * 0.85f + 0.1f
          } else {
            Random.nextFloat() * 0.4f + 0.05f
          }
        }

        // Recording timer
        if (_isRecording.value) {
          // Increment duration every 1 second roughly
        }
      }
    }

    // Timer coroutine
    viewModelScope.launch {
      while (isActive) {
        delay(1000)
        if (_isRecording.value) {
          _recordingDurationSeconds.value += 1
        }
        // Small organic fluctuation in viewer counts
        val delta = Random.nextInt(-12, 18)
        _liveViewerCount.value = (_liveViewerCount.value + delta).coerceAtLeast(100)
        _cameraFps.value = if (Random.nextFloat() > 0.85f) 59 else 60
      }
    }
  }

  private fun initDefaultChat() {
    _chatMessages.value = listOf(
      LiveChatMessage("1", "Elena_Dev", "Framerate and clarity look pristine! 🔥", "10:24", true, "#B388FF"),
      LiveChatMessage("2", "Marcus_K", "Is CameraX 1.5 using zero-shutter-lag pipeline?", "10:25", false, "#00E5A3"),
      LiveChatMessage("3", "Dev_Alex", "The latency is sub-second here, awesome stream quality 🚀", "10:25", false, "#FF9100"),
      LiveChatMessage("4", "Sara_Code", "Checking live feed on Android 16 emulator!", "10:26", true, "#00D2FF")
    )
  }

  private fun startChatSimulationLoop() {
    val simulatedNames = listOf("Taro_Tokyo", "CodeNinja", "PixelMaster", "AndroidFan99", "DevPro_Max", "Chloe_UX", "Viktor_B")
    val simulatedQuotes = listOf(
      "Audio bitrate is very clear 🎧",
      "Notice how smooth the dynamic zoom transition is!",
      "Does this support 60fps hardware encoding?",
      "Super responsive UI layout!",
      "Stream health diagnostics are solid 🟢",
      "Loving the cyberpunk filter overlay ⚡",
      "Running on native Jetpack Compose!"
    )
    val colors = listOf("#00D2FF", "#B388FF", "#00E5A3", "#FF9100", "#FF5252", "#EBDDFF")

    viewModelScope.launch {
      while (isActive) {
        delay(Random.nextLong(3500, 7000))
        if (_studioMode.value == 1) { // Only add when in live stream mode
          val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
          val newMsg = LiveChatMessage(
            id = UUID.randomUUID().toString(),
            author = simulatedNames.random(),
            message = simulatedQuotes.random(),
            timestamp = time,
            isVerified = Random.nextBoolean(),
            authorColorHex = colors.random()
          )
          _chatMessages.value = (_chatMessages.value + newMsg).takeLast(40)
        }
      }
    }
  }
}
