package com.example.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DevPulseRepository
import com.example.model.CodeSnippet
import com.example.model.ContrastCheckResult
import com.example.model.DevTask
import com.example.model.SystemTelemetry
import com.example.model.TaskPriority
import com.example.model.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.pow

class DevPulseViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = DevPulseRepository(application.applicationContext)

  val allTasks: StateFlow<List<DevTask>> = repository.tasks
  val snippets: StateFlow<List<CodeSnippet>> = repository.snippets

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedStatusFilter = MutableStateFlow<TaskStatus?>(null)
  val selectedStatusFilter: StateFlow<TaskStatus?> = _selectedStatusFilter.asStateFlow()

  private val _selectedPriorityFilter = MutableStateFlow<TaskPriority?>(null)
  val selectedPriorityFilter: StateFlow<TaskPriority?> = _selectedPriorityFilter.asStateFlow()

  val filteredTasks: StateFlow<List<DevTask>> = combine(
    allTasks,
    searchQuery,
    selectedStatusFilter,
    selectedPriorityFilter
  ) { tasks, query, status, priority ->
    tasks.filter { task ->
      val matchesQuery = query.isBlank() ||
          task.title.contains(query, ignoreCase = true) ||
          task.description.contains(query, ignoreCase = true) ||
          task.category.contains(query, ignoreCase = true)
      val matchesStatus = status == null || task.status == status
      val matchesPriority = priority == null || task.priority == priority
      matchesQuery && matchesStatus && matchesPriority
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val scratchpadText: StateFlow<String> = repository.scratchpad

  private val _telemetry = MutableStateFlow(repository.readTelemetry())
  val telemetry: StateFlow<SystemTelemetry> = _telemetry.asStateFlow()

  private val _selectedTab = MutableStateFlow(0)
  val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

  private val _isDarkTheme = MutableStateFlow(true)
  val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

  // Design Studio State
  private val _designForegroundColor = MutableStateFlow("#00D2FF")
  val designForegroundColor: StateFlow<String> = _designForegroundColor.asStateFlow()

  private val _designBackgroundColor = MutableStateFlow("#0B0F19")
  val designBackgroundColor: StateFlow<String> = _designBackgroundColor.asStateFlow()

  val contrastResult: StateFlow<ContrastCheckResult> = combine(
    _designForegroundColor,
    _designBackgroundColor
  ) { fg, bg ->
    calculateContrast(fg, bg)
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    calculateContrast("#00D2FF", "#0B0F19")
  )

  fun selectTab(tabIndex: Int) {
    _selectedTab.value = tabIndex
  }

  fun toggleTheme() {
    _isDarkTheme.value = !_isDarkTheme.value
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setStatusFilter(status: TaskStatus?) {
    _selectedStatusFilter.value = if (_selectedStatusFilter.value == status) null else status
  }

  fun setPriorityFilter(priority: TaskPriority?) {
    _selectedPriorityFilter.value = if (_selectedPriorityFilter.value == priority) null else priority
  }

  fun addTask(title: String, description: String, category: String, priority: TaskPriority) {
    repository.addTask(title.trim(), description.trim(), category.trim(), priority)
  }

  fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
    repository.updateTaskStatus(taskId, newStatus)
  }

  fun deleteTask(taskId: String) {
    repository.deleteTask(taskId)
  }

  fun updateScratchpad(newText: String) {
    repository.saveScratchpad(newText)
  }

  fun appendSnippetToScratchpad(snippet: CodeSnippet) {
    val current = repository.scratchpad.value
    val updated = if (current.isBlank()) {
      snippet.code
    } else {
      "$current\n\n// --- ${snippet.title} (${snippet.category}) ---\n${snippet.code}"
    }
    repository.saveScratchpad(updated)
  }

  fun refreshTelemetry() {
    viewModelScope.launch {
      _telemetry.value = repository.readTelemetry()
    }
  }

  fun setDesignColors(fgHex: String, bgHex: String) {
    _designForegroundColor.value = fgHex
    _designBackgroundColor.value = bgHex
  }

  companion object {
    fun parseColorSafe(hexString: String, defaultColor: Color): Color {
      return try {
        val clean = hexString.trim().removePrefix("#")
        val colorInt = when (clean.length) {
          6 -> java.lang.Long.parseLong("FF$clean", 16)
          8 -> java.lang.Long.parseLong(clean, 16)
          3 -> {
            val r = clean[0]
            val g = clean[1]
            val b = clean[2]
            java.lang.Long.parseLong("FF$r$r$g$g$b$b", 16)
          }
          else -> return defaultColor
        }
        Color(colorInt)
      } catch (e: Exception) {
        defaultColor
      }
    }

    private fun calculateContrast(fgHex: String, bgHex: String): ContrastCheckResult {
      val c1 = parseColorSafe(fgHex, Color(0xFF00D2FF))
      val c2 = parseColorSafe(bgHex, Color(0xFF0B0F19))

      val l1 = relativeLuminance(c1)
      val l2 = relativeLuminance(c2)

      val lighter = maxOf(l1, l2)
      val darker = minOf(l1, l2)
      val ratio = (lighter + 0.05f) / (darker + 0.05f)

      return ContrastCheckResult(
        ratio = ratio,
        aaNormal = ratio >= 4.5f,
        aaLarge = ratio >= 3.0f,
        aaaNormal = ratio >= 7.0f,
        aaaLarge = ratio >= 4.5f
      )
    }

    private fun relativeLuminance(color: Color): Float {
      fun channelLuminance(value: Float): Float {
        return if (value <= 0.03928f) {
          value / 12.92f
        } else {
          ((value + 0.055f) / 1.055f).pow(2.4f)
        }
      }
      val r = channelLuminance(color.red)
      val g = channelLuminance(color.green)
      val b = channelLuminance(color.blue)
      return 0.2126f * r + 0.7152f * g + 0.0722f * b
    }
  }
}
