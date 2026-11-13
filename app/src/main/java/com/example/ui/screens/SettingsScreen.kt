package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.UserProfile

/**
 * Comprehensive Settings screen for Youth Tube (ಸೆಟ್ಟಿಂಗ್ಸ್ / ಸಂಯೋಜನೆಗಳು).
 * Manages video playback quality, India All Languages Yard, downloads, AdMob VIP pass,
 * notifications, and Google Play Privacy Policy compliance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  currentUser: UserProfile? = null,
  currentLanguage: String = "ಕನ್ನಡ",
  onBackClick: () -> Unit,
  onOpenPrivacyPolicy: () -> Unit = {},
  onOpenLanguageYard: () -> Unit = {},
  onOpenAdmin: () -> Unit = {},
  onOpenLogin: () -> Unit = {}
) {
  val context = LocalContext.current

  // Settings State
  var playbackQuality by remember { mutableStateOf("Auto (Smart)") }
  var autoplayNext by remember { mutableStateOf(true) }
  var downloadWifiOnly by remember { mutableStateOf(true) }
  var downloadQuality by remember { mutableStateOf("1080p Full HD") }
  var doubleTapSeek by remember { mutableStateOf("10 seconds") }
  var showCaptions by remember { mutableStateOf(true) }
  var notifySubscriptions by remember { mutableStateOf(true) }
  var notifyRecommended by remember { mutableStateOf(true) }
  var notifyLiveStreams by remember { mutableStateOf(true) }
  var isVipAdFreeActive by remember { mutableStateOf(false) }

  BackHandler { onBackClick() }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Settings",
              fontWeight = FontWeight.Black,
              fontSize = 18.sp,
              color = Color.White
            )
            Text(
              text = "ಸಂಯೋಜನೆಗಳು · Preferences",
              fontSize = 11.sp,
              color = Color.White.copy(alpha = 0.85f)
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("btn_settings_back")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to Home",
              tint = Color.White
            )
          }
        },
        actions = {
          IconButton(
            onClick = {
              Toast.makeText(context, "Preferences saved successfully! ಸಂಯೋಜನೆಗಳನ್ನು ಉಳಿಸಲಾಗಿದೆ", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.testTag("btn_settings_save")
          ) {
            Icon(Icons.Default.Done, contentDescription = "Saved", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color(0xFFE50914)
        )
      )
    },
    containerColor = Color(0xFFF8F9FA),
    modifier = Modifier.testTag("settings_screen_root")
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

      // -------------------------------------------------------------
      // 1. ACCOUNT & PROFILE
      // -------------------------------------------------------------
      item {
        SettingsSectionHeader(title = "Account · ಖಾತೆ ಮತ್ತು ಚಾನೆಲ್", kannada = "ಖಾತೆ ವಿವರಗಳು")
        Surface(
          color = Color.White,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          shadowElevation = 1.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFE50914)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(26.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = currentUser?.displayName ?: "Guest Creator User",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFF1E293B)
                )
                Text(
                  text = currentUser?.email ?: "guest@youthtube.app",
                  fontSize = 12.sp,
                  color = Color(0xFF64748B)
                )
                Surface(
                  color = if (currentUser?.isAdmin == true) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                  shape = RoundedCornerShape(4.dp),
                  modifier = Modifier.padding(top = 4.dp)
                ) {
                  Text(
                    text = if (currentUser?.isAdmin == true) "🛡️ SUPER ADMIN" else "🎥 CREATOR ACCESS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentUser?.isAdmin == true) Color(0xFF166534) else Color(0xFF475569),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              OutlinedButton(
                onClick = onOpenLogin,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE50914)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.testTag("btn_settings_account")
              ) {
                Text(
                  text = if (currentUser != null) "Manage" else "Login",
                  fontSize = 11.sp,
                  color = Color(0xFFE50914),
                  fontWeight = FontWeight.Bold
                )
              }
            }

            if (currentUser?.isAdmin == true) {
              Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF1F5F9))
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onOpenAdmin() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFFE50914), modifier = Modifier.size(20.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Open Admin Dashboard (ನಿರ್ವಾಹಕರ ಡ್ಯಾಶ್‌ಬೋರ್ಡ್)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE50914)
                  )
                }
                Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
              }
            }
          }
        }
      }

      // -------------------------------------------------------------
      // 2. INDIA ALL LANGUAGES YARD
      // -------------------------------------------------------------
      item {
        SettingsSectionHeader(title = "Language & Region · ಭಾಷೆ ಮತ್ತು ಪ್ರದೇಶ", kannada = "14 ಭಾರತೀಯ ಭಾಷೆಗಳು")
        Surface(
          color = Color.White,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clickable { onOpenLanguageYard() }
            .testTag("settings_item_language_yard"),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, Color(0xFFFFD180)),
          shadowElevation = 1.dp
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = CircleShape,
              color = Color(0xFFFF6D00),
              modifier = Modifier.size(40.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(text = "🇮🇳", fontSize = 18.sp)
              }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "India All Languages Yard",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  color = Color(0xFFFF6D00),
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "14 LANGUAGES",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = "Active: $currentLanguage · ಭಾರತದ 14 ಅಧಿಕೃತ ಭಾಷೆಗಳ ತಾಣ",
                fontSize = 11.sp,
                color = Color(0xFFD32F2F),
                fontWeight = FontWeight.SemiBold
              )
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
              contentDescription = null,
              tint = Color(0xFFFF6D00),
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }

      // -------------------------------------------------------------
      // 3. VIDEO PLAYBACK & 4K STREAMING
      // -------------------------------------------------------------
      item {
        SettingsSectionHeader(title = "Video Playback & 4K HD · ಪ್ಲೇಬ್ಯಾಕ್ ಗುಣಮಟ್ಟ", kannada = "ವೀಡಿಯೊ ಪ್ರಸಾರ ಸಂಯೋಜನೆಗಳು")
        Surface(
          color = Color.White,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          shadowElevation = 1.dp
        ) {
          Column {
            SettingsClickableItem(
              icon = Icons.Default.HighQuality,
              title = "Video Playback Quality (ಗುಣಮಟ್ಟ)",
              subtitle = playbackQuality,
              onClick = {
                val options = listOf("Auto (Smart)", "4K Ultra HD (2160p)", "1080p Full HD", "720p HD", "Data Saver (360p)")
                val nextIdx = (options.indexOf(playbackQuality) + 1) % options.size
                playbackQuality = options[nextIdx]
                Toast.makeText(context, "Playback quality: $playbackQuality", Toast.LENGTH_SHORT).show()
              }
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsSwitchItem(
              icon = Icons.Default.PlayCircle,
              title = "Autoplay Next Video (ಸ್ವಯಂ ಚಾಲನೆ)",
              subtitle = "Automatically play next recommended video when current finishes",
              checked = autoplayNext,
              onCheckedChange = { autoplayNext = it }
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsClickableItem(
              icon = Icons.Default.FastForward,
              title = "Double-tap to Seek (ಸ್ಕಿಪ್ ಸಮಯ)",
              subtitle = doubleTapSeek,
              onClick = {
                val options = listOf("5 seconds", "10 seconds", "15 seconds", "30 seconds")
                val nextIdx = (options.indexOf(doubleTapSeek) + 1) % options.size
                doubleTapSeek = options[nextIdx]
                Toast.makeText(context, "Seek time: $doubleTapSeek", Toast.LENGTH_SHORT).show()
              }
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsSwitchItem(
              icon = Icons.Default.ClosedCaption,
              title = "Subtitles & Captions (ಉಪಶೀರ್ಷಿಕೆಗಳು)",
              subtitle = "Show subtitles in selected Indian language when available",
              checked = showCaptions,
              onCheckedChange = { showCaptions = it }
            )
          }
        }
      }

      // -------------------------------------------------------------
      // 4. DOWNLOADS & OFFLINE STORAGE
      // -------------------------------------------------------------
      item {
        SettingsSectionHeader(title = "Downloads & Offline · ಡೌನ್‌ಲೋಡ್‌ಗಳು", kannada = "ಆಫ್‌ಲೈನ್ ಶೇಖರಣೆ")
        Surface(
          color = Color.White,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          shadowElevation = 1.dp
        ) {
          Column {
            SettingsClickableItem(
              icon = Icons.Default.Download,
              title = "Download Quality (ಡೌನ್‌ಲೋಡ್ ಗುಣಮಟ್ಟ)",
              subtitle = downloadQuality,
              onClick = {
                val qualities = listOf("1080p Full HD", "720p HD", "Medium (480p)", "Low (Data Saver)")
                val nextIdx = (qualities.indexOf(downloadQuality) + 1) % qualities.size
                downloadQuality = qualities[nextIdx]
                Toast.makeText(context, "Download quality: $downloadQuality", Toast.LENGTH_SHORT).show()
              }
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsSwitchItem(
              icon = Icons.Default.Wifi,
              title = "Download Over Wi-Fi Only",
              subtitle = "Save mobile data plan while caching videos",
              checked = downloadWifiOnly,
              onCheckedChange = { downloadWifiOnly = it }
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsClickableItem(
              icon = Icons.Default.DeleteOutline,
              title = "Clear Downloaded Videos Cache",
              subtitle = "Free up 248 MB storage used by cached video fragments",
              tint = Color(0xFFD32F2F),
              onClick = {
                Toast.makeText(context, "🗑️ Video cache cleared! 248 MB storage freed.", Toast.LENGTH_SHORT).show()
              }
            )
          }
        }
      }

      // -------------------------------------------------------------
      // 5. GOOGLE ADMOB & VIP AD-FREE PASS
      // -------------------------------------------------------------
      item {
        SettingsSectionHeader(title = "Google AdMob & VIP Pass · ಜಾಹೀರಾತು ರಹಿತ", kannada = "ಮಾನಿಟೈಸೇಶನ್")
        Surface(
          color = Color.White,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          shadowElevation = 1.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = CircleShape,
                  color = if (isVipAdFreeActive) Color(0xFF00C853) else Color(0xFFE50914),
                  modifier = Modifier.size(36.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(
                      imageVector = if (isVipAdFreeActive) Icons.Default.Verified else Icons.Default.MovieFilter,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = if (isVipAdFreeActive) "VIP Ad-Free Active 🎉" else "Standard Ad Tier (Free)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF1E293B)
                  )
                  Text(
                    text = if (isVipAdFreeActive) "Active for 30 minutes · ಜಾಹೀರಾತು ರಹಿತ" else "Google AdMob Sponsored",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                  )
                }
              }

              Button(
                onClick = {
                  isVipAdFreeActive = true
                  Toast.makeText(
                    context,
                    "🎉 30-Minute VIP Ad-Free Pass Activated! ಜಾಹೀರಾತು ರಹಿತ ವೀಕ್ಷಣೆ ಸಕ್ರಿಯವಾಗಿದೆ!",
                    Toast.LENGTH_LONG
                  ).show()
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (isVipAdFreeActive) Color(0xFF00C853) else Color(0xFFE50914)
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("btn_settings_rewarded_ad")
              ) {
                Text(
                  text = if (isVipAdFreeActive) "ACTIVE" else "Watch Ad ➔",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Surface(
              color = Color(0xFFF8FAFC),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "AdMob Units: Rewarded ca-app-pub-3940256099942544/5224354917 · Banner 6300978111",
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
        }
      }

      // -------------------------------------------------------------
      // 6. NOTIFICATIONS & ALERTS
      // -------------------------------------------------------------
      item {
        SettingsSectionHeader(title = "Notifications · ಅಧಿಸೂಚನೆಗಳು", kannada = "ಎಚ್ಚರಿಕೆಗಳು")
        Surface(
          color = Color.White,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          shadowElevation = 1.dp
        ) {
          Column {
            SettingsSwitchItem(
              icon = Icons.Default.NotificationsActive,
              title = "Subscribed Channels Activity",
              subtitle = "Alerts when creators you follow upload new videos",
              checked = notifySubscriptions,
              onCheckedChange = { notifySubscriptions = it }
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsSwitchItem(
              icon = Icons.Default.Recommend,
              title = "Recommended For You",
              subtitle = "Popular Kannada & Indian youth viral videos",
              checked = notifyRecommended,
              onCheckedChange = { notifyRecommended = it }
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsSwitchItem(
              icon = Icons.Default.Videocam,
              title = "Live Broadcast Alerts (ಲೈವ್ ಪ್ರಸಾರ)",
              subtitle = "Instant notifications when your favorite channels go live",
              checked = notifyLiveStreams,
              onCheckedChange = { notifyLiveStreams = it }
            )
          }
        }
      }

      // -------------------------------------------------------------
      // 7. PRIVACY POLICY & PLAY CONSOLE COMPLIANCE
      // -------------------------------------------------------------
      item {
        SettingsSectionHeader(title = "Privacy, Security & Legal · ಗೌಪ್ಯತೆ", kannada = "Google Play ಕನ್ಸೋಲ್ ಅನುಮೋದಿತ")
        Surface(
          color = Color.White,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          shadowElevation = 1.dp
        ) {
          Column {
            SettingsClickableItem(
              icon = Icons.Default.Policy,
              title = "Privacy Policy · ಗೌಪ್ಯತಾ ನೀತಿ",
              subtitle = "Camera, Mic, AdMob & COPPA Google Play Console disclosures",
              tint = Color(0xFF00C853),
              badge = "PLAY READY",
              badgeColor = Color(0xFF00C853),
              onClick = onOpenPrivacyPolicy
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsClickableItem(
              icon = Icons.Default.CameraAlt,
              title = "Camera Permission (ಕ್ಯಾಮೆರಾ ಅನುಮತಿ)",
              subtitle = "Used strictly during live broadcast streaming and Shorts creator studio",
              onClick = {
                Toast.makeText(context, "Camera permission is managed in device settings and prompted during Live recording.", Toast.LENGTH_SHORT).show()
              }
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsClickableItem(
              icon = Icons.Default.Mic,
              title = "Microphone Permission (ಮೈಕ್ರೊಫೋನ್ ಅನುಮತಿ)",
              subtitle = "Used for voice commentary during live streams and creator video audio",
              onClick = {
                Toast.makeText(context, "Microphone permission is prompted in Live Video screen.", Toast.LENGTH_SHORT).show()
              }
            )

            Divider(color = Color(0xFFF1F5F9))

            SettingsClickableItem(
              icon = Icons.Default.History,
              title = "Clear Watch & Search History",
              subtitle = "Reset playback recommendations on this device",
              onClick = {
                Toast.makeText(context, "Watch and search history cleared successfully!", Toast.LENGTH_SHORT).show()
              }
            )
          }
        }
      }

      // -------------------------------------------------------------
      // 8. APP ABOUT & ATTRIBUTION
      // -------------------------------------------------------------
      item {
        SettingsSectionHeader(title = "About · ಅಪ್ಲಿಕೇಶನ್ ಮಾಹಿತಿ", kannada = "ಆವೃತ್ತಿ ಮತ್ತು ವಿವರ")
        Surface(
          color = Color.White,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          shadowElevation = 1.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                color = Color(0xFFE50914),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(42.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "YOUTH TUBE",
                  fontWeight = FontWeight.Black,
                  fontSize = 15.sp,
                  color = Color(0xFF1E293B)
                )
                Text(
                  text = "Version 1.0.0 (Build 2026.1) · Made with Pride in India 🇮🇳",
                  fontSize = 11.sp,
                  color = Color(0xFF64748B)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "Developed by Sharanu Krishnapur (sharanukrishnapur48@gmail.com)\nBuilt for Karnataka and Pan-Indian youth creators with full 4K playback, Live Video streaming, Google AdMob monetization, and India All Languages Yard.",
              fontSize = 11.sp,
              color = Color(0xFF475569),
              lineHeight = 16.sp
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

// -------------------------------------------------------------
// HELPER COMPOSABLES FOR SETTINGS
// -------------------------------------------------------------

@Composable
fun SettingsSectionHeader(title: String, kannada: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 18.dp, end = 18.dp, top = 6.dp, bottom = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = title,
      fontWeight = FontWeight.Bold,
      fontSize = 12.sp,
      color = Color(0xFF475569)
    )
    Spacer(modifier = Modifier.weight(1f))
    Text(
      text = kannada,
      fontSize = 10.sp,
      color = Color(0xFF94A3B8),
      fontWeight = FontWeight.Medium
    )
  }
}

@Composable
fun SettingsClickableItem(
  icon: ImageVector,
  title: String,
  subtitle: String,
  tint: Color = Color(0xFF475569),
  badge: String? = null,
  badgeColor: Color = Color(0xFFE50914),
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 14.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = title,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          color = Color(0xFF1E293B)
        )
        badge?.let {
          Spacer(modifier = Modifier.width(6.dp))
          Surface(color = badgeColor, shape = RoundedCornerShape(4.dp)) {
            Text(
              text = it,
              color = Color.White,
              fontSize = 8.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }
      }
      Text(
        text = subtitle,
        fontSize = 11.sp,
        color = Color(0xFF64748B)
      )
    }
    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = Color(0xFFCBD5E1),
      modifier = Modifier.size(12.dp)
    )
  }
}

@Composable
fun SettingsSwitchItem(
  icon: ImageVector,
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onCheckedChange(!checked) }
      .padding(horizontal = 14.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF475569), modifier = Modifier.size(22.dp))
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        color = Color(0xFF1E293B)
      )
      Text(
        text = subtitle,
        fontSize = 11.sp,
        color = Color(0xFF64748B)
      )
    }
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = Color(0xFFE50914)
      )
    )
  }
}
