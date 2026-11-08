package com.example.data

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import com.example.model.CodeSnippet
import com.example.model.DevTask
import com.example.model.SystemTelemetry
import com.example.model.TaskPriority
import com.example.model.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class DevPulseRepository(private val context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("devpulse_storage", Context.MODE_PRIVATE)

  private val _tasks = MutableStateFlow<List<DevTask>>(emptyList())
  val tasks: StateFlow<List<DevTask>> = _tasks.asStateFlow()

  private val _snippets = MutableStateFlow<List<CodeSnippet>>(emptyList())
  val snippets: StateFlow<List<CodeSnippet>> = _snippets.asStateFlow()

  private val _scratchpad = MutableStateFlow("")
  val scratchpad: StateFlow<String> = _scratchpad.asStateFlow()

  init {
    loadTasks()
    loadSnippets()
    _scratchpad.value = prefs.getString("scratchpad_content", DEFAULT_SCRATCHPAD) ?: DEFAULT_SCRATCHPAD
  }

  private fun loadTasks() {
    val jsonString = prefs.getString("saved_tasks", null)
    if (jsonString.isNullOrBlank()) {
      val defaultTasks = listOf(
        DevTask(
          id = UUID.randomUUID().toString(),
          title = "Migrate Architecture to Edge-to-Edge",
          description = "Implement enableEdgeToEdge() with WindowInsets.safeDrawing and navigationBars padding.",
          category = "Compose",
          priority = TaskPriority.HIGH,
          status = TaskStatus.DONE
        ),
        DevTask(
          id = UUID.randomUUID().toString(),
          title = "Optimize Compose Recomposition Loops",
          description = "Replace derived state recalculations with remember and derivedStateOf.",
          category = "Performance",
          priority = TaskPriority.CRITICAL,
          status = TaskStatus.IN_PROGRESS
        ),
        DevTask(
          id = UUID.randomUUID().toString(),
          title = "Audit Material 3 Contrast & Dynamic Colors",
          description = "Verify all primary, secondary, and container tokens meet WCAG 2.1 AA 4.5:1 ratio.",
          category = "UI/UX",
          priority = TaskPriority.MEDIUM,
          status = TaskStatus.REVIEW
        ),
        DevTask(
          id = UUID.randomUUID().toString(),
          title = "Setup Offline-First Room / Cache Layer",
          description = "Configure local entities, TypeConverters, and Flow queries for seamless offline use.",
          category = "Database",
          priority = TaskPriority.HIGH,
          status = TaskStatus.BACKLOG
        ),
        DevTask(
          id = UUID.randomUUID().toString(),
          title = "Profile Memory Leaks with Telemetry",
          description = "Monitor live RAM allocations and background sensor listeners across screen rotations.",
          category = "DevOps",
          priority = TaskPriority.LOW,
          status = TaskStatus.BACKLOG
        )
      )
      saveTasks(defaultTasks)
    } else {
      try {
        val array = JSONArray(jsonString)
        val loaded = mutableListOf<DevTask>()
        for (i in 0 until array.length()) {
          val obj = array.getJSONObject(i)
          loaded.add(
            DevTask(
              id = obj.getString("id"),
              title = obj.getString("title"),
              description = obj.optString("description", ""),
              category = obj.optString("category", "General"),
              priority = TaskPriority.valueOf(obj.optString("priority", "MEDIUM")),
              status = TaskStatus.valueOf(obj.optString("status", "BACKLOG")),
              createdAtMillis = obj.optLong("createdAtMillis", System.currentTimeMillis())
            )
          )
        }
        _tasks.value = loaded
      } catch (e: Exception) {
        _tasks.value = emptyList()
      }
    }
  }

  fun addTask(title: String, description: String, category: String, priority: TaskPriority) {
    val newTask = DevTask(
      id = UUID.randomUUID().toString(),
      title = title,
      description = description,
      category = category,
      priority = priority,
      status = TaskStatus.BACKLOG
    )
    val updated = listOf(newTask) + _tasks.value
    saveTasks(updated)
  }

  fun updateTaskStatus(id: String, newStatus: TaskStatus) {
    val updated = _tasks.value.map {
      if (it.id == id) it.copy(status = newStatus) else it
    }
    saveTasks(updated)
  }

  fun deleteTask(id: String) {
    val updated = _tasks.value.filterNot { it.id == id }
    saveTasks(updated)
  }

  private fun saveTasks(list: List<DevTask>) {
    _tasks.value = list
    val array = JSONArray()
    list.forEach { task ->
      val obj = JSONObject().apply {
        put("id", task.id)
        put("title", task.title)
        put("description", task.description)
        put("category", task.category)
        put("priority", task.priority.name)
        put("status", task.status.name)
        put("createdAtMillis", task.createdAtMillis)
      }
      array.put(obj)
    }
    prefs.edit().putString("saved_tasks", array.toString()).apply()
  }

  fun saveScratchpad(text: String) {
    _scratchpad.value = text
    prefs.edit().putString("scratchpad_content", text).apply()
  }

  private fun loadSnippets() {
    _snippets.value = listOf(
      CodeSnippet(
        id = "compose_state",
        title = "Compose State & ViewModel",
        category = "Compose",
        language = "Kotlin",
        description = "Recommended pattern for M3 UI with StateFlow and Lifecycle collection.",
        code = """
@Composable
fun MyFeatureScreen(
  viewModel: MyViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = { TopAppBar(title = { Text("DevPulse Feature") }) }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      items(uiState.items, key = { it.id }) { item ->
        ItemCard(item = item)
      }
    }
  }
}
        """.trimIndent()
      ),
      CodeSnippet(
        id = "edge_to_edge",
        title = "Edge-to-Edge & Window Insets",
        category = "Architecture",
        language = "Kotlin",
        description = "Standard Android 15/16 edge-to-edge window insets configuration.",
        code = """
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  enableEdgeToEdge()
  setContent {
    AppTheme {
      Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
      ) { innerPadding ->
        NavHost(
          modifier = Modifier.padding(innerPadding)
        )
      }
    }
  }
}
        """.trimIndent()
      ),
      CodeSnippet(
        id = "flow_debouncer",
        title = "Search Flow Debouncer",
        category = "Coroutines",
        language = "Kotlin",
        description = "Debounce search queries with distinctUntilChanged and flatMapLatest.",
        code = """
val searchResults = searchQuery
  .debounce(300L)
  .distinctUntilChanged()
  .flatMapLatest { query ->
    if (query.isBlank()) flowOf(emptyList())
    else repository.searchStream(query)
  }
  .stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )
        """.trimIndent()
      ),
      CodeSnippet(
        id = "retrofit_client",
        title = "Retrofit + OkHttp Client",
        category = "Network",
        language = "Kotlin",
        description = "Clean Retrofit HTTP client with Moshi converter and logging.",
        code = """
val okHttpClient = OkHttpClient.Builder()
  .connectTimeout(15, TimeUnit.SECONDS)
  .readTimeout(15, TimeUnit.SECONDS)
  .addInterceptor(HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  })
  .build()

val api = Retrofit.Builder()
  .baseUrl("https://api.example.com/v1/")
  .client(okHttpClient)
  .addConverterFactory(MoshiConverterFactory.create())
  .build()
  .create(ApiService::class.java)
        """.trimIndent()
      ),
      CodeSnippet(
        id = "adb_essentials",
        title = "Essential ADB Commands",
        category = "Tools",
        language = "Shell",
        description = "Daily terminal commands for Android debugging and performance inspection.",
        code = """
# Inspect layout hierarchy & overdraw
adb shell setprop debug.layout true

# Dump memory info for package
adb shell dumpsys meminfo com.aistudio.devpulse.qrxvbn

# Force battery status
adb shell dumpsys battery set level 85
adb shell dumpsys battery set status 2 # charging

# Reset battery simulation
adb shell dumpsys battery reset
        """.trimIndent()
      )
    )
  }

  fun readTelemetry(): SystemTelemetry {
    // 1. Battery Telemetry
    val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus: Intent? = context.registerReceiver(null, ifilter)
    val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    val batteryPct = if (level >= 0 && scale > 0) ((level / scale.toFloat()) * 100).toInt() else 100
    val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
        status == BatteryManager.BATTERY_STATUS_FULL
    val chargePlug = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
    val powerSource = when (chargePlug) {
      BatteryManager.BATTERY_PLUGGED_USB -> "USB Cable"
      BatteryManager.BATTERY_PLUGGED_AC -> "AC Wall Charger"
      BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless Dock"
      else -> if (isCharging) "Charging" else "On Battery"
    }

    // 2. RAM / Memory Telemetry
    val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    val memInfo = ActivityManager.MemoryInfo()
    actManager?.getMemoryInfo(memInfo)
    val totalRamMb = memInfo.totalMem / (1024 * 1024)
    val availRamMb = memInfo.availMem / (1024 * 1024)
    val usedRamMb = (totalRamMb - availRamMb).coerceAtLeast(0)
    val ramPercent = if (totalRamMb > 0) ((usedRamMb.toFloat() / totalRamMb) * 100).toInt() else 0

    // 3. Network Status
    val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNet = connManager?.activeNetwork
    val caps = connManager?.getNetworkCapabilities(activeNet)
    val netStatus = when {
      caps == null -> "Offline"
      caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi (High Speed)"
      caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular LTE / 5G"
      caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
      else -> "Connected (Other)"
    }

    // 4. Sensors
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    val allSensors = sensorManager?.getSensorList(Sensor.TYPE_ALL) ?: emptyList()
    val sensorNames = allSensors.take(12).map { it.name }

    // 5. Display metrics
    val dm = context.resources.displayMetrics
    val widthDp = (dm.widthPixels / dm.density).toInt()
    val heightDp = (dm.heightPixels / dm.density).toInt()
    val dpi = dm.densityDpi

    return SystemTelemetry(
      batteryLevel = batteryPct,
      isCharging = isCharging,
      powerSource = powerSource,
      ramUsedMb = usedRamMb,
      ramTotalMb = totalRamMb,
      ramPercent = ramPercent,
      deviceModel = Build.MODEL ?: "Android Device",
      manufacturer = (Build.MANUFACTURER ?: "Generic").replaceFirstChar { it.uppercase() },
      androidVersion = Build.VERSION.RELEASE ?: "14",
      apiLevel = Build.VERSION.SDK_INT,
      screenWidthDp = widthDp,
      screenHeightDp = heightDp,
      screenDensityDpi = dpi,
      networkStatus = netStatus,
      sensorCount = allSensors.size,
      availableSensors = sensorNames,
      refreshedAtMillis = System.currentTimeMillis()
    )
  }

  companion object {
    private const val DEFAULT_SCRATCHPAD = """// DevPulse Mobile Scratchpad
// Quick notes, API payloads, or snippet testing

data class ApiResponse<T>(
  val success: Boolean,
  val payload: T?,
  val message: String? = null
)

fun main() {
  println("DevPulse ready for native Android execution.")
}
"""
  }
}
