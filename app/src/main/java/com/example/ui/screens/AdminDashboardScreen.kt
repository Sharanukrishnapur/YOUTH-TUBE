package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.model.AdminMetric
import com.example.model.CategoryShare
import com.example.model.ModerationItem
import com.example.model.RecentActivity
import com.example.ui.components.AdMobRewardedAdDialog
import com.example.model.PublishedVideo
import com.example.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
  onExitAdmin: () -> Unit,
  viewModel: AdminViewModel = viewModel(),
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  val activeNav by viewModel.activeNavItem.collectAsState()
  val selectedLanguage by viewModel.selectedLanguage.collectAsState()
  val selectedTimeframe by viewModel.selectedTimeframe.collectAsState()
  val moderationQueue by viewModel.moderationQueue.collectAsState()
  val publishedVideos by viewModel.publishedVideos.collectAsState()

  val homeBannerAds by viewModel.homeBannerAds.collectAsState()
  val videoAds by viewModel.videoAds.collectAsState()
  val shortsAds by viewModel.shortsAds.collectAsState()

  var showRewardedAdDialog by remember { mutableStateOf(false) }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      AdminSidebarDrawerContent(
        activeNav = activeNav,
        onSelectNav = { item ->
          viewModel.selectNavItem(item)
          scope.launch { drawerState.close() }
          if (item == "Logout") {
            onExitAdmin()
          } else {
            Toast.makeText(context, "Navigating to $item", Toast.LENGTH_SHORT).show()
          }
        }
      )
    }
  ) {
    Scaffold(
      modifier = modifier
        .fillMaxSize()
        .testTag("admin_dashboard_scaffold")
    ) { padding ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(Color(0xFFF0F4F8))
          .padding(padding)
      ) {
        // 1. Top Red Admin Bar matching mockup
        AdminTopRedBar(
          selectedLanguage = selectedLanguage,
          onLanguageSelect = { viewModel.setLanguage(it) },
          onOpenDrawer = { scope.launch { drawerState.open() } },
          onExitAdmin = onExitAdmin
        )

        // 2. Main Admin Dashboard Scrollable Canvas
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .testTag("admin_dashboard_scroll_canvas"),
          contentPadding = PaddingValues(12.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Welcome Hero Banner with Indian Flag & Mission
          item {
            AdminWelcomeHeroCard()
          }

          // 5 Stat Metric Cards
          item {
            AdminMetricsRow()
          }

          // Middle Row: Analytics Graph (Views/Likes/Subs)
          item {
            AnalyticsOverviewCard(
              timeframe = selectedTimeframe,
              onTimeframeChange = { viewModel.setTimeframe(it) }
            )
          }

          // Middle Row: Top Categories Donut Chart & Recent Activity
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              TopCategoriesDonutCard(
                shares = viewModel.categoryShares,
                modifier = Modifier.weight(1f)
              )
              RecentActivitiesCard(
                activities = viewModel.recentActivities,
                modifier = Modifier.weight(1f)
              )
            }
          }

          // Bottom Operations: Video Moderation Queue & Publishing
          item {
            VideoModerationCard(
              items = moderationQueue,
              publishedVideos = publishedVideos,
              onApprove = {
                viewModel.approveModeration(it)
                Toast.makeText(context, "ವಿಡಿಯೋ ಅನುಮೋದಿಸಲಾಗಿದೆ ಮತ್ತು ಯಶಸ್ವಿಯಾಗಿ ಪ್ರಕಟಿಸಲಾಗಿದೆ! (Video Approved & Published!)", Toast.LENGTH_SHORT).show()
              },
              onReject = {
                viewModel.rejectModeration(it)
                Toast.makeText(context, "ವಿಡಿಯೋ ತಿರಸ್ಕರಿಸಲಾಗಿದೆ (Video Flagged & Rejected)", Toast.LENGTH_SHORT).show()
              }
            )
          }

          // Bottom Operations: Ad Management & Kids Zone Control
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              AdManagementCard(
                homeBanner = homeBannerAds,
                videoAds = videoAds,
                shortsAds = shortsAds,
                impressions = viewModel.adImpressions,
                clicks = viewModel.adClicks,
                revenue = viewModel.adRevenue,
                onToggleHome = { viewModel.toggleHomeBannerAds() },
                onToggleVideo = { viewModel.toggleVideoAds() },
                onToggleShorts = { viewModel.toggleShortsAds() },
                onTestRewardedAd = { showRewardedAdDialog = true },
                modifier = Modifier.weight(1f)
              )
              KidsZoneControlCard(modifier = Modifier.weight(1f))
            }
          }

          item {
            Spacer(modifier = Modifier.height(30.dp))
          }
        }
      }
    }

    if (showRewardedAdDialog) {
      AdMobRewardedAdDialog(
        onDismiss = { showRewardedAdDialog = false },
        onRewardEarned = {
          Toast.makeText(context, "AdMob Rewarded Test Ad Success!", Toast.LENGTH_SHORT).show()
        }
      )
    }
  }
}

// -------------------------------------------------------------
// TOP RED ADMIN BAR
// -------------------------------------------------------------

@Composable
fun AdminTopRedBar(
  selectedLanguage: String,
  onLanguageSelect: (String) -> Unit,
  onOpenDrawer: () -> Unit,
  onExitAdmin: () -> Unit
) {
  var langDropdownOpen by remember { mutableStateOf(false) }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFFE50914))
      .padding(horizontal = 12.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Left: Drawer Hamburger & Youth Tube Logo
    Row(verticalAlignment = Alignment.CenterVertically) {
      IconButton(
        onClick = onOpenDrawer,
        modifier = Modifier.testTag("admin_btn_drawer")
      ) {
        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White, modifier = Modifier.size(26.dp))
      }

      Image(
        painter = painterResource(id = R.drawable.ic_youth_tube_header_logo),
        contentDescription = "Youth Tube",
        modifier = Modifier.height(30.dp),
        contentScale = ContentScale.Fit
      )
    }

    // Center: Language Dropdowns
    Box {
      Surface(
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
          .clickable { langDropdownOpen = true }
          .testTag("admin_language_dropdown")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Icon(Icons.Default.Language, contentDescription = null, tint = Color(0xFF0066FF), modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "$selectedLanguage ▾", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
        }
      }

      DropdownMenu(
        expanded = langDropdownOpen,
        onDismissRequest = { langDropdownOpen = false }
      ) {
        listOf("ಕನ್ನಡ", "English", "हिन्दी").forEach { lang ->
          DropdownMenuItem(
            text = { Text(lang, fontWeight = if (lang == selectedLanguage) FontWeight.Bold else FontWeight.Normal) },
            onClick = {
              onLanguageSelect(lang)
              langDropdownOpen = false
            }
          )
        }
      }
    }

    // Right: Admin Badge, Bell & Profile
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Admin Panel Verified Badge
      Surface(
        color = Color(0xFF00C853),
        shape = RoundedCornerShape(14.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Admin Panel", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }

      // Bell notification
      BadgedBox(
        badge = {
          Badge(containerColor = Color.White, contentColor = Color(0xFFE50914)) {
            Text("3", fontWeight = FontWeight.Bold, fontSize = 9.sp)
          }
        }
      ) {
        Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = Color.White, modifier = Modifier.size(22.dp))
      }

      // Exit / Back button
      Surface(
        color = Color.Black.copy(alpha = 0.25f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .clickable { onExitAdmin() }
          .testTag("admin_btn_exit")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(Icons.Default.ExitToApp, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("App", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// WELCOME HERO CARD
// -------------------------------------------------------------

@Composable
fun AdminWelcomeHeroCard() {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("admin_welcome_hero_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBDEFB))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        // Clapperboard icon badge
        Box(
          modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color(0xFF007BFF)),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = "Welcome to YOUTH TUBE Admin Dashboard!",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0A192F)
          )
          Text(
            text = "Manage • Monitor • Grow",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1565C0)
          )
        }
      }

      // Indian Flag Mission Graphic
      Image(
        painter = painterResource(id = R.drawable.admin_welcome_flag),
        contentDescription = "Safe & Responsible Video Platform",
        modifier = Modifier
          .height(44.dp)
          .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Fit
      )
    }
  }
}

// -------------------------------------------------------------
// 5 METRIC CARDS ROW
// -------------------------------------------------------------

@Composable
fun AdminMetricsRow() {
  val metrics = listOf(
    AdminMetric("users", "Total Users", "1,25,680", "↑ 12%", true, Color(0xFF007BFF), Icons.Default.Group),
    AdminMetric("videos", "Total Videos", "2,48,730", "↑ 18%", true, Color(0xFF00C853), Icons.Default.PlayArrow),
    AdminMetric("views", "Total Views", "12.6M", "↑ 20%", true, Color(0xFF7B1FA2), Icons.Default.Visibility),
    AdminMetric("likes", "Total Likes", "8.9M", "↑ 15%", true, Color(0xFFFF6D00), Icons.Default.ThumbUp),
    AdminMetric("comments", "Total Comments", "3.1M", "↑ 14%", true, Color(0xFF00897B), Icons.Default.ChatBubble)
  )

  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("admin_metrics_row")
  ) {
    items(metrics) { m ->
      Card(
        modifier = Modifier
          .width(180.dp)
          .height(105.dp)
          .testTag("admin_metric_${m.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = m.color)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Icon(imageVector = m.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))

          Column {
            Text(text = m.title, color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              Text(text = m.value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
              Text(text = m.growthDelta, color = Color(0xFFB9F6CA), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// ANALYTICS OVERVIEW CARD (LINE GRAPH)
// -------------------------------------------------------------

@Composable
fun AnalyticsOverviewCard(
  timeframe: String,
  onTimeframeChange: (String) -> Unit
) {
  var tfDropdownOpen by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("analytics_overview_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Card Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.ShowChart, contentDescription = null, tint = Color(0xFF007BFF))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Analytics Overview", fontWeight = FontWeight.Black, fontSize = 15.sp, color = Color(0xFF0A192F))
        }

        // Timeframe selector
        Box {
          Surface(
            color = Color(0xFF0D253F),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.clickable { tfDropdownOpen = true }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(text = timeframe, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
          }

          DropdownMenu(expanded = tfDropdownOpen, onDismissRequest = { tfDropdownOpen = false }) {
            listOf("Last 7 Days", "Last 30 Days", "Last 90 Days").forEach { tf ->
              DropdownMenuItem(
                text = { Text(tf) },
                onClick = {
                  onTimeframeChange(tf)
                  tfDropdownOpen = false
                }
              )
            }
          }
        }
      }

      // Legend Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        LegendDot(Color(0xFF007BFF), "Views")
        Spacer(modifier = Modifier.width(10.dp))
        LegendDot(Color(0xFFFF1744), "Likes")
        Spacer(modifier = Modifier.width(10.dp))
        LegendDot(Color(0xFF00C853), "Subscribers")
      }

      // Multi-line Canvas Graph with Spline Curves & Fill
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(160.dp)
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          // Horizontal grid lines
          val step = h / 3f
          for (i in 0..3) {
            val y = i * step
            drawLine(Color(0xFFE2E8F0), Offset(0f, y), Offset(w, y), strokeWidth = 1f)
          }

          // Points for Views (blue)
          val viewPoints = listOf(
            Offset(0.05f * w, 0.65f * h),
            Offset(0.28f * w, 0.40f * h),
            Offset(0.50f * w, 0.48f * h),
            Offset(0.72f * w, 0.22f * h),
            Offset(0.95f * w, 0.12f * h)
          )

          // Points for Likes (red)
          val likePoints = listOf(
            Offset(0.05f * w, 0.78f * h),
            Offset(0.28f * w, 0.65f * h),
            Offset(0.50f * w, 0.70f * h),
            Offset(0.72f * w, 0.48f * h),
            Offset(0.95f * w, 0.38f * h)
          )

          // Points for Subs (green)
          val subPoints = listOf(
            Offset(0.05f * w, 0.90f * h),
            Offset(0.28f * w, 0.82f * h),
            Offset(0.50f * w, 0.84f * h),
            Offset(0.72f * w, 0.68f * h),
            Offset(0.95f * w, 0.62f * h)
          )

          fun drawSmoothCurve(points: List<Offset>, color: Color, fillGradient: Boolean) {
            val path = Path()
            path.moveTo(points.first().x, points.first().y)
            for (i in 0 until points.size - 1) {
              val p0 = points[i]
              val p1 = points[i + 1]
              val cx = (p0.x + p1.x) / 2f
              path.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
            }

            if (fillGradient) {
              val fillPath = Path()
              fillPath.addPath(path)
              fillPath.lineTo(points.last().x, h)
              fillPath.lineTo(points.first().x, h)
              fillPath.close()

              drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                  colors = listOf(color.copy(alpha = 0.2f), Color.Transparent),
                  startY = 0f,
                  endY = h
                )
              )
            }

            drawPath(path = path, color = color, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))
            points.forEach { pt ->
              drawCircle(Color.White, radius = 4.dp.toPx(), center = pt)
              drawCircle(color, radius = 3.dp.toPx(), center = pt)
            }
          }

          drawSmoothCurve(viewPoints, Color(0xFF007BFF), fillGradient = true)
          drawSmoothCurve(likePoints, Color(0xFFFF1744), fillGradient = false)
          drawSmoothCurve(subPoints, Color(0xFF00C853), fillGradient = false)
        }
      }

      // X-Axis Date Labels
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        listOf("Oct 25", "Oct 31", "Nov 06", "Nov 12", "Nov 18").forEach { d ->
          Text(text = d, color = Color(0xFF718096), fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TOP CATEGORIES (DONUT CHART) CARD
// -------------------------------------------------------------

@Composable
fun TopCategoriesDonutCard(
  shares: List<CategoryShare>,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.testTag("top_categories_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "Top Categories", fontWeight = FontWeight.Black, fontSize = 14.sp, color = Color(0xFF0A192F))
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Donut Chart Canvas
        Box(
          modifier = Modifier.size(110.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            val total = shares.sumOf { it.percentage }.toFloat()
            val strokeWidth = 22.dp.toPx()

            shares.forEach { s ->
              val sweepAngle = (s.percentage / total) * 360f
              drawArc(
                color = s.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle - 3f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
              )
              startAngle += sweepAngle
            }
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "35%", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color(0xFF0A192F))
            Text(text = "Kids", fontSize = 11.sp, color = Color(0xFF718096), fontWeight = FontWeight.Bold)
          }
        }

        // Legend list
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          shares.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(item.color)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = item.name, fontSize = 11.sp, color = Color(0xFF4A5568))
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "${item.percentage}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0A192F))
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// RECENT ACTIVITIES CARD
// -------------------------------------------------------------

@Composable
fun RecentActivitiesCard(
  activities: List<RecentActivity>,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.testTag("recent_activities_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Campaign, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Recent Activities", fontWeight = FontWeight.Black, fontSize = 14.sp, color = Color(0xFF0A192F))
        }
        Text(text = "View All →", fontSize = 11.sp, color = Color(0xFF007BFF), fontWeight = FontWeight.Bold)
      }

      activities.forEach { act ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            val icon = when (act.iconType) {
              "upload" -> Icons.Default.Upload
              "report" -> Icons.Default.Warning
              "subscriber" -> Icons.Default.Person
              else -> Icons.Default.ChatBubble
            }
            val iconColor = when (act.iconType) {
              "upload" -> Color(0xFF007BFF)
              "report" -> Color(0xFFFF1744)
              "subscriber" -> Color(0xFF00C853)
              else -> Color(0xFF7B1FA2)
            }

            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(text = act.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0A192F), maxLines = 1)
              Text(text = act.subtitle, fontSize = 10.sp, color = Color(0xFF718096), maxLines = 1)
            }
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(text = act.timeAgo, fontSize = 9.sp, color = Color(0xFFA0AEC0))
            if (act.badgeText != null && act.badgeColor != null) {
              Surface(
                color = act.badgeColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = act.badgeText,
                  color = act.badgeColor,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
            }
          }
        }
        HorizontalDivider(color = Color(0xFFF1F5F9))
      }
    }
  }
}

// -------------------------------------------------------------
// VIDEO MODERATION CARD (WITH REAL APPROVE/REJECT ACTIONS)
// -------------------------------------------------------------

@Composable
fun VideoModerationCard(
  items: List<ModerationItem>,
  publishedVideos: List<PublishedVideo> = emptyList(),
  onApprove: (String) -> Unit,
  onReject: (String) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("video_moderation_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF007BFF))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "ವಿಡಿಯೋ ಅನುಮೋದನೆ ಮತ್ತು ಪ್ರಕಟಣೆ", fontWeight = FontWeight.Black, fontSize = 14.sp, color = Color(0xFF0A192F))
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            color = Color(0xFFFF1744),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = "${items.size} Pending",
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Text(text = "View All →", fontSize = 12.sp, color = Color(0xFF007BFF), fontWeight = FontWeight.Bold)
      }

      if (items.isEmpty()) {
        Text("ಎಲ್ಲಾ ವಿಡಿಯೋಗಳು ಪರಿಶೀಲಿಸಲಾಗಿದೆ! (All submissions reviewed)", color = Color(0xFF00C853), fontSize = 12.sp, fontWeight = FontWeight.Bold)
      } else {
        items.forEach { item ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
              Image(
                painter = painterResource(id = item.thumbnailRes),
                contentDescription = null,
                modifier = Modifier
                  .size(46.dp)
                  .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0A192F), maxLines = 1)
                Text(text = "Category: ${item.category} · ${item.status}", fontSize = 10.sp, color = Color(0xFFFF9800), fontWeight = FontWeight.SemiBold)
              }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Button(
                onClick = { onApprove(item.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
              ) {
                Text("Approve ✔", fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }

              Button(
                onClick = { onReject(item.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1744)),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
              ) {
                Text("Reject ✕", fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
          HorizontalDivider(color = Color(0xFFF1F5F9))
        }
      }

      // Published Section Preview
      if (publishedVideos.isNotEmpty()) {
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(color = Color(0xFF00C853), shape = RoundedCornerShape(4.dp)) {
            Text("LIVE", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Published to Platform (${publishedVideos.size} Videos)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF00C853))
        }
        publishedVideos.take(2).forEach { pub ->
          Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00C853), modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = pub.title, fontSize = 11.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Text(text = pub.status, fontSize = 10.sp, color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// AD MANAGEMENT CARD
// -------------------------------------------------------------

@Composable
fun AdManagementCard(
  homeBanner: Boolean,
  videoAds: Boolean,
  shortsAds: Boolean,
  impressions: String = "48,210",
  clicks: String = "1,150",
  revenue: String = "₹14,850.50",
  onToggleHome: () -> Unit,
  onToggleVideo: () -> Unit,
  onToggleShorts: () -> Unit,
  onTestRewardedAd: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.testTag("ad_management_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Campaign, contentDescription = null, tint = Color(0xFF007BFF), modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Google AdMob ಜಾಹೀರಾತು", fontWeight = FontWeight.Black, fontSize = 13.sp, color = Color(0xFF0A192F))
        }
        Text("Active ✔", fontSize = 10.sp, color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
      }

      // AdMob Performance Stats
      Row(
        modifier = Modifier.fillMaxWidth().background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp)).padding(6.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Impressions", fontSize = 9.sp, color = Color.Gray)
          Text(impressions, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Clicks", fontSize = 9.sp, color = Color.Gray)
          Text(clicks, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Revenue", fontSize = 9.sp, color = Color.Gray)
          Text(revenue, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C853))
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Google AdMob Logo
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color(0xFFFFF3E0)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Campaign, contentDescription = "AdMob", tint = Color(0xFFFF9800), modifier = Modifier.size(18.dp))
          }
          Text("AdMob", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        // Toggles
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
          AdToggleRow("Home Banner", homeBanner, onToggleHome)
          AdToggleRow("Video Ads", videoAds, onToggleVideo)
          AdToggleRow("Shorts Ads", shortsAds, onToggleShorts)
        }
      }

      Button(
        onClick = onTestRewardedAd,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.fillMaxWidth().height(28.dp)
      ) {
        Text("Test Rewarded Ad (ಜಾಹೀರಾತು ಪರೀಕ್ಷಿಸಿ)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun AdToggleRow(label: String, active: Boolean, onToggle: () -> Unit) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.clickable { onToggle() }
  ) {
    Icon(
      imageVector = if (active) Icons.Default.CheckCircle else Icons.Default.Close,
      contentDescription = null,
      tint = if (active) Color(0xFF00C853) else Color.Gray,
      modifier = Modifier.size(14.dp)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = label, fontSize = 10.sp, color = Color(0xFF2D3748), fontWeight = FontWeight.SemiBold)
  }
}

// -------------------------------------------------------------
// KIDS ZONE CONTROL CARD
// -------------------------------------------------------------

@Composable
fun KidsZoneControlCard(modifier: Modifier = Modifier) {
  Card(
    modifier = modifier.testTag("kids_zone_control_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.ChildCare, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Kids Zone Control", fontWeight = FontWeight.Black, fontSize = 13.sp, color = Color(0xFF1B5E20))
      }

      Image(
        painter = painterResource(id = R.drawable.admin_kids_safe),
        contentDescription = "Safe for Kids",
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Fit
      )
    }
  }
}

// -------------------------------------------------------------
// SIDEBAR DRAWER CONTENT (ADMIN NAVIGATION MENU)
// -------------------------------------------------------------

@Composable
fun AdminSidebarDrawerContent(
  activeNav: String,
  onSelectNav: (String) -> Unit
) {
  ModalDrawerSheet(
    modifier = Modifier.width(280.dp),
    drawerContainerColor = Color(0xFF0A192F)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
      // Header with Youth Tube Logo
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFF06101E))
          .padding(16.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_youth_tube_header_logo),
          contentDescription = "Youth Tube Logo",
          modifier = Modifier.height(34.dp),
          contentScale = ContentScale.Fit
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      val menuItems = listOf(
        "Dashboard" to Icons.Default.Dashboard,
        "User Management" to Icons.Default.Group,
        "Video Management" to Icons.Default.VideoLibrary,
        "Ads Management" to Icons.Default.Campaign,
        "Channel Management" to Icons.Default.Movie,
        "Reports & Analytics" to Icons.Default.ShowChart,
        "Kids Zone Control" to Icons.Default.ChildCare,
        "Parental Controls" to Icons.Default.Shield,
        "Community Guidelines" to Icons.Default.Policy,
        "Grievance & Support" to Icons.Default.HelpOutline,
        "Settings" to Icons.Default.Settings,
        "Logout" to Icons.Default.ExitToApp
      )

      menuItems.forEach { (name, icon) ->
        val isSelected = activeNav == name
        Surface(
          color = if (isSelected) Color(0xFFE50914) else Color.Transparent,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .clickable { onSelectNav(name) }
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)
          ) {
            Icon(
              imageVector = icon,
              contentDescription = name,
              tint = if (isSelected) Color.White else Color(0xFFB0BEC5),
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = name,
              color = if (isSelected) Color.White else Color(0xFFCFD8DC),
              fontSize = 13.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      // Bottom "Made for India" Tricolor Ribbon
      Image(
        painter = painterResource(id = R.drawable.admin_tricolor_ribbon),
        contentDescription = "Made for India",
        modifier = Modifier
          .fillMaxWidth()
          .height(55.dp)
          .padding(bottom = 6.dp),
        contentScale = ContentScale.Fit
      )
    }
  }
}

@Composable
fun LegendDot(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = label, fontSize = 10.sp, color = Color(0xFF4A5568), fontWeight = FontWeight.SemiBold)
  }
}
