package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.FlashOn
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.ui.screens.IndiaLanguagesYardModal
import com.example.ui.screens.SettingsScreen
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.DesignStudioScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveVideoScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.SnippetsScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.TelemetryScreen
import com.example.ui.screens.VideoPlayerScreen
import androidx.compose.ui.text.style.TextOverflow
import com.example.ui.components.LoginDialog
import com.example.ui.theme.DevPulseTheme
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.DevPulseViewModel
import com.example.viewmodel.VideoPlayerViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val viewModel: DevPulseViewModel = viewModel()
      val isDarkTheme by viewModel.isDarkTheme.collectAsState()

      DevPulseTheme(darkTheme = isDarkTheme) {
        YouthTubeApp(viewModel = viewModel)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouthTubeApp(viewModel: DevPulseViewModel) {
  val selectedTab by viewModel.selectedTab.collectAsState()
  val isDarkTheme by viewModel.isDarkTheme.collectAsState()
  var showInfoDialog by remember { mutableStateOf(false) }
  var inAdminDashboard by remember { mutableStateOf(false) }
  var inVideoPlayer by remember { mutableStateOf(false) }
  var inPrivacyPolicy by remember { mutableStateOf(false) }
  var inSettings by remember { mutableStateOf(false) }

  val videoPlayerViewModel: VideoPlayerViewModel = viewModel()
  val authViewModel: AuthViewModel = viewModel()
  val currentUser by authViewModel.currentUser.collectAsState()
  val context = LocalContext.current
  var showLoginDialog by remember { mutableStateOf(false) }
  var loginDialogTab by remember { mutableStateOf(0) }
  var showIndiaLanguagesYard by remember { mutableStateOf(false) }

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()

  if (inVideoPlayer) {
    BackHandler { inVideoPlayer = false }
    VideoPlayerScreen(
      onBackClick = { inVideoPlayer = false },
      videoPlayerViewModel = videoPlayerViewModel
    )
    return
  }

  if (inAdminDashboard) {
    BackHandler { inAdminDashboard = false }
    AdminDashboardScreen(onExitAdmin = { inAdminDashboard = false })
    return
  }

  if (inPrivacyPolicy) {
    BackHandler { inPrivacyPolicy = false }
    PrivacyPolicyScreen(onBackClick = { inPrivacyPolicy = false })
    return
  }

  if (inSettings) {
    BackHandler { inSettings = false }
    SettingsScreen(
      currentUser = currentUser,
      currentLanguage = "ಕನ್ನಡ",
      onBackClick = { inSettings = false },
      onOpenPrivacyPolicy = { inPrivacyPolicy = true },
      onOpenLanguageYard = { showIndiaLanguagesYard = true },
      onOpenAdmin = {
        if (currentUser?.isAdmin == true) {
          inAdminDashboard = true
        } else {
          loginDialogTab = 1
          showLoginDialog = true
        }
      },
      onOpenLogin = {
        loginDialogTab = 0
        showLoginDialog = true
      }
    )
    return
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface
      ) {
        // Drawer Header with Youth Tube Emblem & Current User / Admin Status
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE50914))
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Image(
              painter = painterResource(id = R.drawable.ic_youth_tube_logo),
              contentDescription = "Youth Tube",
              modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
            )
            Surface(
              color = Color.Black.copy(alpha = 0.35f),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.clickable {
                loginDialogTab = 0
                showLoginDialog = true
              }
            ) {
              Text(
                text = if (currentUser != null) "Account ⚙" else "Login ➔",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "YOUTH TUBE",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
          )
          if (currentUser != null) {
            Text(
              text = "📧 ${currentUser?.email}",
              color = Color.White.copy(alpha = 0.95f),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Surface(
              color = if (currentUser?.isAdmin == true) Color(0xFF00E676) else Color.White,
              shape = RoundedCornerShape(4.dp),
              modifier = Modifier.padding(top = 4.dp)
            ) {
              Text(
                text = if (currentUser?.isAdmin == true) "SUPER ADMIN" else "CREATOR USER",
                color = Color.Black,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
              )
            }
          } else {
            Text(
              text = "Your Video, Our World! 🇮🇳",
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
          label = { Text("Home Feed") },
          selected = selectedTab == 0,
          onClick = {
            viewModel.selectTab(0)
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.Home, contentDescription = null) },
          colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = Color(0xFFFFEBEE), selectedTextColor = Color(0xFFE50914))
        )

        NavigationDrawerItem(
          label = { Text("Live Video & Studio") },
          selected = selectedTab == 1 || selectedTab == 2,
          onClick = {
            viewModel.selectTab(1)
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.Videocam, contentDescription = null) },
          colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = Color(0xFFFFEBEE), selectedTextColor = Color(0xFFE50914))
        )

        NavigationDrawerItem(
          label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("Admin Dashboard", fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.weight(1f))
              Surface(
                color = Color(0xFF00C853),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "ADMIN",
                  color = Color.White,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
          },
          selected = false,
          onClick = {
            if (currentUser?.isAdmin == true) {
              inAdminDashboard = true
            } else {
              loginDialogTab = 1 // Open Admin Login
              showLoginDialog = true
            }
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.Lock, contentDescription = "Admin Dashboard", tint = Color(0xFFE50914)) },
          colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color(0xFFFFF0F1),
            unselectedTextColor = Color(0xFFE50914)
          )
        )

        NavigationDrawerItem(
          label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("4K Video Player", fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.weight(1f))
              Surface(
                color = Color(0xFFE50914),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "4K HD",
                  color = Color.White,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
          },
          selected = false,
          onClick = {
            inVideoPlayer = true
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Video Player", tint = Color(0xFFE50914)) },
          colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color(0xFFFFEBEE),
            unselectedTextColor = Color(0xFFE50914)
          )
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
          text = "PLATFORM SUITE",
          style = MaterialTheme.typography.labelSmall,
          color = Color.Gray,
          modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )

        NavigationDrawerItem(
          label = { Text("Hardware Telemetry") },
          selected = selectedTab == 3,
          onClick = {
            viewModel.selectTab(3)
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.Speed, contentDescription = null) }
        )

        NavigationDrawerItem(
          label = { Text("Tasks & Sprint") },
          selected = false,
          onClick = {
            viewModel.selectTab(4)
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.Checklist, contentDescription = null) }
        )

        NavigationDrawerItem(
          label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("Privacy Policy · ಗೌಪ್ಯತಾ ನೀತಿ", fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.weight(1f))
              Surface(color = Color(0xFF00C853), shape = RoundedCornerShape(4.dp)) {
                Text(
                  "PLAY READY",
                  color = Color.White,
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
          },
          selected = false,
          onClick = {
            inPrivacyPolicy = true
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.Policy, contentDescription = "Privacy Policy", tint = Color(0xFF00C853)) },
          colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color(0xFFF0FDF4),
            unselectedTextColor = Color(0xFF166534)
          )
        )

        NavigationDrawerItem(
          label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("India All Languages Yard", fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.weight(1f))
              Surface(color = Color(0xFFFF6D00), shape = RoundedCornerShape(4.dp)) {
                Text(
                  "14 ಭಾಷೆಗಳು",
                  color = Color.White,
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
          },
          selected = false,
          onClick = {
            showIndiaLanguagesYard = true
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.Language, contentDescription = "India Languages Yard", tint = Color(0xFFFF6D00)) },
          colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color(0xFFFFF7ED),
            unselectedTextColor = Color(0xFFC2410C)
          )
        )

        NavigationDrawerItem(
          label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("Settings · ಸಂಯೋಜನೆಗಳು", fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.weight(1f))
              Surface(color = Color(0xFFE50914), shape = RoundedCornerShape(4.dp)) {
                Text(
                  "PREFERENCES",
                  color = Color.White,
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
          },
          selected = false,
          onClick = {
            inSettings = true
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color(0xFFE50914)) },
          colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color(0xFFFFEBEE),
            unselectedTextColor = Color(0xFFE50914)
          )
        )

        NavigationDrawerItem(
          label = { Text("About Youth Tube") },
          selected = false,
          onClick = {
            showInfoDialog = true
            coroutineScope.launch { drawerState.close() }
          },
          icon = { Icon(Icons.Default.Info, contentDescription = null) }
        )
      }
    }
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      contentWindowInsets = WindowInsets.safeDrawing,
      topBar = {
        // When on Home tab, HomeScreen renders its dedicated Youth Tube Red Header.
        // On other tabs, render a clean branded TopAppBar.
        if (selectedTab != 0) {
          CenterAlignedTopAppBar(
            title = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                  painter = painterResource(id = R.drawable.ic_youth_tube_logo),
                  contentDescription = "Youth Tube Logo",
                  modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = when (selectedTab) {
                    1 -> "Shorts & Studio"
                    2 -> "Create & Broadcast"
                    3 -> "Subscriptions Live"
                    4 -> "Profile & Tools"
                    else -> "Youth Tube"
                  },
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Black
                )
              }
            },
            actions = {
              IconButton(
                onClick = { viewModel.toggleTheme() },
                modifier = Modifier.testTag("theme_toggle_button")
              ) {
                Icon(
                  imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                  contentDescription = "Toggle Theme"
                )
              }
              IconButton(
                onClick = { showInfoDialog = true },
                modifier = Modifier.testTag("app_info_button")
              ) {
                Icon(Icons.Default.Info, contentDescription = "About Youth Tube")
              }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
              containerColor = MaterialTheme.colorScheme.background
            )
          )
        }
      },
      bottomBar = {
        // 5-destination Bottom Bar matching the uploaded mockup:
        // [Home] [Shorts] [ + Upload ] [Subscriptions] [Profile]
        NavigationBar(
          modifier = Modifier.testTag("bottom_nav_bar"),
          containerColor = Color.White,
          tonalElevation = 8.dp
        ) {
          // 1. Home
          NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { viewModel.selectTab(0) },
            icon = {
              Icon(
                imageVector = if (selectedTab == 0) Icons.Default.Home else Icons.Outlined.Home,
                contentDescription = "Home"
              )
            },
            label = {
              Text(
                "Home",
                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color(0xFFE50914),
              selectedTextColor = Color(0xFFE50914),
              indicatorColor = Color.Transparent,
              unselectedIconColor = Color(0xFF555555),
              unselectedTextColor = Color(0xFF555555)
            ),
            modifier = Modifier.testTag("nav_home")
          )

          // 2. Shorts
          NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { viewModel.selectTab(1) },
            icon = {
              Icon(
                imageVector = Icons.Outlined.FlashOn,
                contentDescription = "Shorts"
              )
            },
            label = {
              Text(
                "Shorts",
                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color(0xFFE50914),
              selectedTextColor = Color(0xFFE50914),
              indicatorColor = Color.Transparent,
              unselectedIconColor = Color(0xFF555555),
              unselectedTextColor = Color(0xFF555555)
            ),
            modifier = Modifier.testTag("nav_shorts")
          )

          // 3. Prominent Red Circular Center Button (+) for Upload / Camera Studio
          NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { viewModel.selectTab(2) },
            icon = {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFE50914)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "Upload",
                  tint = Color.White,
                  modifier = Modifier.size(26.dp)
                )
              }
            },
            label = {
              Text(
                "Upload",
                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedTextColor = Color(0xFFE50914),
              indicatorColor = Color.Transparent,
              unselectedTextColor = Color(0xFF555555)
            ),
            modifier = Modifier.testTag("nav_upload")
          )

          // 4. Subscriptions
          NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { viewModel.selectTab(3) },
            icon = {
              Icon(
                imageVector = if (selectedTab == 3) Icons.Default.Subscriptions else Icons.Outlined.Subscriptions,
                contentDescription = "Subscriptions"
              )
            },
            label = {
              Text(
                "Subscriptions",
                fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color(0xFFE50914),
              selectedTextColor = Color(0xFFE50914),
              indicatorColor = Color.Transparent,
              unselectedIconColor = Color(0xFF555555),
              unselectedTextColor = Color(0xFF555555)
            ),
            modifier = Modifier.testTag("nav_subscriptions")
          )

          // 5. Profile
          NavigationBarItem(
            selected = selectedTab == 4,
            onClick = { viewModel.selectTab(4) },
            icon = {
              Icon(
                imageVector = if (selectedTab == 4) Icons.Default.Person else Icons.Outlined.Person,
                contentDescription = "Profile"
              )
            },
            label = {
              Text(
                "Profile",
                fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color(0xFFE50914),
              selectedTextColor = Color(0xFFE50914),
              indicatorColor = Color.Transparent,
              unselectedIconColor = Color(0xFF555555),
              unselectedTextColor = Color(0xFF555555)
            ),
            modifier = Modifier.testTag("nav_profile")
          )
        }
      }
    ) { paddingValues ->
      Crossfade(
        targetState = selectedTab,
        label = "tabCrossfade",
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
      ) { tab ->
        when (tab) {
          0 -> HomeScreen(
            onNavigateToLiveVideo = { viewModel.selectTab(2) },
            onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
            onOpenAdmin = { inAdminDashboard = true },
            onOpenVideoPlayer = { inVideoPlayer = true },
            onPlayVideo = { video ->
              videoPlayerViewModel.playCustomVideo(
                id = video.id,
                title = video.title,
                duration = video.duration,
                thumbnailRes = video.thumbnailRes,
                subtitle = "${video.category} · ${video.language}",
                views = video.views,
                hashtags = listOf("#YouthTube", "#India", "#${video.category.replace(" ", "").replace("&", "")}")
              )
              inVideoPlayer = true
            },
            onOpenSettings = { inSettings = true }
          )
          1 -> LiveVideoScreen(devPulseViewModel = viewModel) // Shorts mode
          2 -> LiveVideoScreen(devPulseViewModel = viewModel) // Camera studio / Upload
          3 -> LiveVideoScreen(devPulseViewModel = viewModel) // Subscriptions live broadcasts
          4 -> ProfileToolsHub(
            viewModel = viewModel,
            currentUser = currentUser,
            onOpenAdmin = {
              if (currentUser?.isAdmin == true) {
                inAdminDashboard = true
              } else {
                loginDialogTab = 1
                showLoginDialog = true
              }
            },
            onOpenVideoPlayer = { inVideoPlayer = true },
            onOpenLogin = {
              loginDialogTab = 0
              showLoginDialog = true
            },
            onOpenPrivacy = { inPrivacyPolicy = true },
            onOpenLanguageYard = { showIndiaLanguagesYard = true },
            onOpenSettings = { inSettings = true }
          )
        }
      }

      // Login Dialog (Mail ID & Admin Login)
      if (showLoginDialog) {
        LoginDialog(
          authViewModel = authViewModel,
          initialTab = loginDialogTab,
          onDismiss = { showLoginDialog = false },
          onLoginSuccess = {
            if (loginDialogTab == 1) {
              inAdminDashboard = true
            }
          }
        )
      }

      // India All Languages Yard Dialog (from Drawer / Profile)
      if (showIndiaLanguagesYard) {
        IndiaLanguagesYardModal(
          currentSelectedLanguage = "ಕನ್ನಡ",
          onLanguageSelected = { lang ->
            Toast.makeText(
              context,
              "${lang.greeting} Welcome to Youth Tube in ${lang.nativeName} (${lang.englishName})!",
              Toast.LENGTH_LONG
            ).show()
          },
          onSelectAllIndia = {
            Toast.makeText(context, "🇮🇳 Showing Videos from All 14 Indian Languages!", Toast.LENGTH_SHORT).show()
          },
          onDismiss = { showIndiaLanguagesYard = false }
        )
      }

      if (showInfoDialog) {
        AlertDialog(
          onDismissRequest = { showInfoDialog = false },
          title = { Text("About Youth Tube", fontWeight = FontWeight.Bold) },
          text = {
            Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Image(
                painter = painterResource(id = R.drawable.ic_youth_tube_logo),
                contentDescription = "Youth Tube Emblem",
                modifier = Modifier
                  .size(110.dp)
                  .clip(RoundedCornerShape(16.dp))
              )
              Spacer(modifier = Modifier.size(12.dp))
              Text(
                text = "Watch · Create · Share",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE50914)
              )
              Text(
                text = "Your Video, Our World!",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.size(10.dp))
              Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  color = Color(0xFF0066FF),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = "ಕನ್ನಡ",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White
                  )
                }
                Surface(
                  color = MaterialTheme.colorScheme.secondaryContainer,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = "English",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                  )
                }
                Surface(
                  color = MaterialTheme.colorScheme.tertiaryContainer,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = "हिन्दी",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                  )
                }
              }
              Spacer(modifier = Modifier.size(12.dp))
              Text(
                text = "• Youth Tube Home Feed with 12 Categories\n• Live Camera Studio with CameraX & Filters\n• Live Streaming Broadcasts in Kannada, English, Hindi\n• Interactive Chat and Floating Emoji Reactions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          },
          confirmButton = {
            Button(
              onClick = { showInfoDialog = false },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
            ) {
              Text("Close")
            }
          }
        )
      }
    }
  }
}

// -------------------------------------------------------------
// PROFILE & TOOLS HUB (Tab 4)
// -------------------------------------------------------------

@Composable
fun ProfileToolsHub(
  viewModel: DevPulseViewModel,
  currentUser: com.example.viewmodel.UserProfile? = null,
  onOpenAdmin: () -> Unit = {},
  onOpenVideoPlayer: () -> Unit = {},
  onOpenLogin: () -> Unit = {},
  onOpenPrivacy: () -> Unit = {},
  onOpenLanguageYard: () -> Unit = {},
  onOpenSettings: () -> Unit = {}
) {
  var activeSubTab by remember { mutableStateOf(0) }

  Column(modifier = Modifier.fillMaxSize()) {
    // User Profile / Mail ID Login Banner
    Surface(
      color = Color(0xFF0F172A),
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 4.dp)
        .clickable { onOpenLogin() }
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFE50914)),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = currentUser?.displayName ?: "Login with Mail ID",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
          Text(
            text = currentUser?.email ?: "ಇಮೇಲ್ ಮೂಲಕ ಲಾಗಿನ್ ಮಾಡಿ · Sign In",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
        Surface(color = Color(0xFF2563EB), shape = RoundedCornerShape(6.dp)) {
          Text(
            text = if (currentUser != null) "Manage" else "Login",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }
    }
    // 4K Video Player Quick Launch Card
    Surface(
      color = Color(0xFF141414),
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE50914).copy(alpha = 0.5f)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 4.dp)
        .clickable { onOpenVideoPlayer() }
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFFE50914), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text("4K Video Player", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text("Shivaji Maharaj, Krishna Stories & HD Playback", color = Color(0xFFAAAAAA), fontSize = 11.sp)
        }
        Surface(color = Color(0xFFE50914), shape = RoundedCornerShape(6.dp)) {
          Text("Watch", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
        }
      }
    }

    // Admin Dashboard Quick Launch Card
    Surface(
      color = Color(0xFF1E293B),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 4.dp)
        .clickable { onOpenAdmin() }
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text("YOUTH TUBE Admin Dashboard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text("Moderation, AdMob, Metrics & Made for India", color = Color(0xFF94A3B8), fontSize = 11.sp)
        }
        Surface(color = Color(0xFFE50914), shape = RoundedCornerShape(6.dp)) {
          Text("Open", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
        }
      }
    }

    // Google Play Privacy Policy & Data Safety Card
    Surface(
      color = Color(0xFF0F172A),
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 4.dp)
        .clickable { onOpenPrivacy() }
        .testTag("btn_profile_privacy_policy")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Policy, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text("Privacy Policy · ಗೌಪ್ಯತಾ ನೀತಿ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text("Camera, Mic & Google Play Console Disclosures", color = Color(0xFF94A3B8), fontSize = 11.sp)
        }
        Surface(color = Color(0xFF00C853), shape = RoundedCornerShape(6.dp)) {
          Text("Read", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
        }
      }
    }

    // India All Languages Yard Quick Launch Card
    Surface(
      color = Color(0xFF1E1B18),
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF6D00).copy(alpha = 0.5f)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 4.dp)
        .clickable { onOpenLanguageYard() }
        .testTag("btn_profile_language_yard")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Language, contentDescription = null, tint = Color(0xFFFF6D00), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text("India All Languages Yard · 14 ಭಾಷೆಗಳು", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text("ಕನ್ನಡ, हिन्दी, English, తెలుగు, தமிழ் & 14 Official Languages", color = Color(0xFFFED7AA), fontSize = 11.sp)
        }
        Surface(color = Color(0xFFFF6D00), shape = RoundedCornerShape(6.dp)) {
          Text("Explore ➔", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
        }
      }
    }

    // Settings & Preferences Quick Launch Card
    Surface(
      color = Color(0xFF1E2430),
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE50914).copy(alpha = 0.5f)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 4.dp)
        .clickable { onOpenSettings() }
        .testTag("btn_profile_settings")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFFE50914), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text("Settings · ಸಂಯೋಜನೆಗಳು", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text("Video Quality, Autoplay, Downloads & Notifications", color = Color(0xFFCBD5E1), fontSize = 11.sp)
        }
        Surface(color = Color(0xFFE50914), shape = RoundedCornerShape(6.dp)) {
          Text("Configure", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
        }
      }
    }

    // Sub-tab switcher in Profile
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Surface(
        color = if (activeSubTab == 0) Color(0xFFE50914) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .clickable { activeSubTab = 0 }
          .padding(4.dp)
      ) {
        Text(
          text = "Telemetry & Hardware",
          color = if (activeSubTab == 0) Color.White else MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
      }

      Surface(
        color = if (activeSubTab == 1) Color(0xFFE50914) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .clickable { activeSubTab = 1 }
          .padding(4.dp)
      ) {
        Text(
          text = "Creator Tasks",
          color = if (activeSubTab == 1) Color.White else MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
      }

      Surface(
        color = if (activeSubTab == 2) Color(0xFFE50914) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .clickable { activeSubTab = 2 }
          .padding(4.dp)
      ) {
        Text(
          text = "Design & Theme",
          color = if (activeSubTab == 2) Color.White else MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
      }
    }

    when (activeSubTab) {
      0 -> TelemetryScreen(viewModel = viewModel)
      1 -> TasksScreen(viewModel = viewModel)
      2 -> DesignStudioScreen(viewModel = viewModel)
    }
  }
}
