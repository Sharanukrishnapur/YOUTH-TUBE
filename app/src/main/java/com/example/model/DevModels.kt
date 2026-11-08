package com.example.model

enum class TaskPriority(val label: String) {
  CRITICAL("Critical"),
  HIGH("High"),
  MEDIUM("Medium"),
  LOW("Low")
}

enum class TaskStatus(val label: String) {
  BACKLOG("Backlog"),
  IN_PROGRESS("In Progress"),
  REVIEW("Review"),
  DONE("Done")
}

data class DevTask(
  val id: String,
  val title: String,
  val description: String,
  val category: String,
  val priority: TaskPriority,
  val status: TaskStatus,
  val createdAtMillis: Long = System.currentTimeMillis()
)

data class CodeSnippet(
  val id: String,
  val title: String,
  val category: String,
  val description: String,
  val language: String,
  val code: String
)

data class SystemTelemetry(
  val batteryLevel: Int = -1,
  val isCharging: Boolean = false,
  val powerSource: String = "Unknown",
  val ramUsedMb: Long = 0,
  val ramTotalMb: Long = 0,
  val ramPercent: Int = 0,
  val deviceModel: String = "",
  val manufacturer: String = "",
  val androidVersion: String = "",
  val apiLevel: Int = 0,
  val screenWidthDp: Int = 0,
  val screenHeightDp: Int = 0,
  val screenDensityDpi: Int = 0,
  val networkStatus: String = "Unknown",
  val sensorCount: Int = 0,
  val availableSensors: List<String> = emptyList(),
  val refreshedAtMillis: Long = System.currentTimeMillis()
)

data class ContrastCheckResult(
  val ratio: Float,
  val aaNormal: Boolean,
  val aaLarge: Boolean,
  val aaaNormal: Boolean,
  val aaaLarge: Boolean
)
