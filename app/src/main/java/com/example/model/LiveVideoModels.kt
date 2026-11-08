package com.example.model

enum class LiveVideoFilter(val label: String, val badge: String) {
  CLEAN("Clean RAW", "RAW"),
  CYBERPUNK("Cyberpunk", "NEON"),
  NIGHT_VISION("Night Vision", "NVG"),
  NOIR("Noir Cinema", "B&W"),
  RETRO_VHS("Retro VHS", "VHS")
}

data class LiveStreamChannel(
  val id: String,
  val title: String,
  val creator: String,
  val category: String,
  val viewerCount: Int,
  val resolution: String,
  val bitrateKbps: Int,
  val fps: Int,
  val streamUrl: String,
  val description: String,
  val language: String = "English",
  val isLive: Boolean = true
)

data class LiveChatMessage(
  val id: String,
  val author: String,
  val message: String,
  val timestamp: String,
  val isVerified: Boolean = false,
  val authorColorHex: String = "#00D2FF"
)

data class FloatingReaction(
  val id: Long,
  val emoji: String,
  val xOffsetRatio: Float
)
