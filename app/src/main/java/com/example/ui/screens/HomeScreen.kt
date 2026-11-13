package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.example.model.IndianLanguage
import com.example.model.IndianLanguagesRepository
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.PublishedVideo
import com.example.ui.components.AdMobBannerView
import com.example.ui.components.AdMobRewardedAdDialog
import com.example.ui.components.GoogleAdFeedCard
import com.example.ui.components.GoogleAdPreferencesDialog
import com.example.ui.components.GoogleAdRewardedBanner
import com.example.ui.components.LoginDialog
import com.example.ui.components.SocialShareSheet
import com.example.viewmodel.AdminViewModel
import com.example.viewmodel.AuthViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

data class CategoryItem(
  val id: String,
  val title: String,
  val color: Color,
  val icon: ImageVector
)

data class TrendingVideo(
  val id: String,
  val title: String,
  val duration: String,
  val views: String,
  val age: String,
  val thumbnailRes: Int,
  val category: String,
  val language: String
)

@Composable
fun HomeScreen(
  onNavigateToLiveVideo: () -> Unit,
  onOpenDrawer: () -> Unit = {},
  onOpenAdmin: () -> Unit = {},
  onOpenVideoPlayer: () -> Unit = {},
  onPlayVideo: (TrendingVideo) -> Unit = { onOpenVideoPlayer() },
  onOpenSettings: () -> Unit = {},
  adminViewModel: AdminViewModel = viewModel(),
  authViewModel: AuthViewModel = viewModel(),
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedLanguage by remember { mutableStateOf("ಕನ್ನಡ") }
  var filterByLanguageStrictly by remember { mutableStateOf(false) }
  var selectedCategory by remember { mutableStateOf("all") }
  var selectedFilterCategory by remember { mutableStateOf("All · ಎಲ್ಲಾ") }
  var activeVideoToPlay by remember { mutableStateOf<TrendingVideo?>(null) }
  var videoToShare by remember { mutableStateOf<TrendingVideo?>(null) }
  var showSearchModal by remember { mutableStateOf(false) }
  var showLoginDialog by remember { mutableStateOf(false) }
  var showRewardedAdDialog by remember { mutableStateOf(false) }
  var showIndiaLanguagesYard by remember { mutableStateOf(false) }

  val publishedVideos by adminViewModel.publishedVideos.collectAsState()

  val categories = remember {
    listOf(
      CategoryItem("all", "All\nಎಲ್ಲಾ", Color(0xFFE53935), Icons.Default.Apps),
      CategoryItem("news_live", "News Live", Color(0xFFD50000), Icons.Default.LiveTv),
      CategoryItem("music", "Music", Color(0xFFD81B60), Icons.Default.MusicNote),
      CategoryItem("comedy", "Comedy", Color(0xFFFF6D00), Icons.Default.SentimentVerySatisfied),
      CategoryItem("kitchen", "Kitchen", Color(0xFF5E35B1), Icons.Default.Restaurant),
      CategoryItem("business", "Business", Color(0xFF00695C), Icons.Default.BusinessCenter),
      CategoryItem("shorts", "Shorts", Color(0xFF8E24AA), Icons.Outlined.FlashOn),
      CategoryItem("kids", "Kids Zone", Color(0xFF43A047), Icons.Default.ChildCare),
      CategoryItem("education", "Education", Color(0xFFFB8C00), Icons.Default.MenuBook),
      CategoryItem("devotional", "Devotional", Color(0xFF00ACC1), Icons.Default.VolunteerActivism),
      CategoryItem("travel", "Travel", Color(0xFF2E7D32), Icons.Default.Flight),
      CategoryItem("gaming", "Gaming", Color(0xFF1976D2), Icons.Default.SportsEsports),
      CategoryItem("videos", "Videos", Color(0xFF1E88E5), Icons.Default.Movie),
      CategoryItem("gk", "General\nKnowledge", Color(0xFF7CB342), Icons.Default.Lightbulb),
      CategoryItem("languages", "Languages", Color(0xFFFF8F00), Icons.Default.Language),
      CategoryItem("more", "Settings", Color(0xFF455A64), Icons.Default.Settings)
    )
  }

  // Complete Library of All Youth Tube Videos
  val allVideos = remember {
    listOf(
      TrendingVideo(
        id = "v1",
        title = "Chhatrapati Shivaji Maharaj Biography | ಮಹಾರಾಜರ ಜೀವನ ಗಾಥೆ",
        duration = "12:45",
        views = "4.5M views",
        age = "1 year ago",
        thumbnailRes = R.drawable.thumb_shivaji,
        category = "History & Education",
        language = "ಕನ್ನಡ"
      ),
      TrendingVideo(
        id = "v2",
        title = "Animal Friends for Kids | Jungle Adventure & Rhymes",
        duration = "10:20",
        views = "3.2M views",
        age = "1 year ago",
        thumbnailRes = R.drawable.thumb_animals,
        category = "Kids Zone",
        language = "English"
      ),
      TrendingVideo(
        id = "v3",
        title = "Ganesh Bhakti Songs | Sukhkarta Dukh Harta Aarti",
        duration = "15:30",
        views = "5.8M views",
        age = "1 year ago",
        thumbnailRes = R.drawable.thumb_ganesh,
        category = "Devotional",
        language = "ಕನ್ನಡ / हिन्दी"
      ),
      TrendingVideo(
        id = "v4",
        title = "Krishna Stories in Kannada | ಬಾಲ ಕೃಷ್ಣನ ರೋಚಕ ಕಥೆಗಳು",
        duration = "10:15",
        views = "3.8M views",
        age = "10 months ago",
        thumbnailRes = R.drawable.thumb_next_krishna,
        category = "Devotional",
        language = "ಕನ್ನಡ"
      ),
      TrendingVideo(
        id = "v5",
        title = "Learning is Fun | Children Educational Songs & ABC",
        duration = "08:20",
        views = "2.9M views",
        age = "8 months ago",
        thumbnailRes = R.drawable.thumb_next_learning,
        category = "Kids Zone",
        language = "English"
      ),
      TrendingVideo(
        id = "v6",
        title = "Kashi Darshan | Sacred Varanasi Ghats & Travel Guide",
        duration = "14:32",
        views = "5.1M views",
        age = "6 months ago",
        thumbnailRes = R.drawable.thumb_next_kashi,
        category = "Travel",
        language = "ಕನ್ನಡ / हिन्दी"
      ),
      TrendingVideo(
        id = "v7",
        title = "Crispy Masala Dosa Recipe | ಹೋಟೆಲ್ ಶೈಲಿಯ ಮಸಾಲ ದೋಸೆ",
        duration = "07:18",
        views = "1.8M views",
        age = "5 months ago",
        thumbnailRes = R.drawable.thumb_next_dosa,
        category = "Cooking",
        language = "ಕನ್ನಡ"
      ),
      TrendingVideo(
        id = "v8",
        title = "Chamundeshwari Devi Darshana | Mysuru Temple Yatra",
        duration = "12:17",
        views = "6.4M views",
        age = "4 months ago",
        thumbnailRes = R.drawable.thumb_next_chamundeshwari,
        category = "Devotional",
        language = "ಕನ್ನಡ"
      ),
      TrendingVideo(
        id = "v9",
        title = "Kids Learn & Grow Together | ಕುಟುಂಬ ಮತ್ತು ಮಕ್ಕಳ ಕಥೆಗಳು",
        duration = "06:45",
        views = "1.2M views",
        age = "3 months ago",
        thumbnailRes = R.drawable.hero_banner_kids,
        category = "Kids Zone",
        language = "ಕನ್ನಡ / English"
      ),
      TrendingVideo(
        id = "v10",
        title = "Swarajya Lives On! | ಜಯ ಭವಾನಿ, ಜಯ ಶಿವಾಜಿ ಮಹಾರಾಜ್!",
        duration = "09:30",
        views = "4.1M views",
        age = "2 months ago",
        thumbnailRes = R.drawable.player_hero_shivaji,
        category = "History & Education",
        language = "ಕನ್ನಡ / मराठी"
      ),
      TrendingVideo(
        id = "v11",
        title = "Tirupati Balaji Darshanam | శ్రీ వేంకటేశ్వర స్వాಮಿ భక్తి గీతాలు",
        duration = "11:20",
        views = "7.2M views",
        age = "2 months ago",
        thumbnailRes = R.drawable.thumb_ganesh,
        category = "Devotional",
        language = "తెలుగు"
      ),
      TrendingVideo(
        id = "v12",
        title = "Brihadeeswarar Temple Secrets | தஞ்சை பெரிய கோவில் வரலாறு",
        duration = "13:45",
        views = "4.9M views",
        age = "1 month ago",
        thumbnailRes = R.drawable.thumb_next_kashi,
        category = "History & Education",
        language = "தமிழ்"
      ),
      TrendingVideo(
        id = "v13",
        title = "God's Own Country Kerala Tour | കേരള സൗന്ദര്യം ಮತ್ತು ಪ್ರವಾಸ",
        duration = "09:50",
        views = "3.6M views",
        age = "3 weeks ago",
        thumbnailRes = R.drawable.thumb_animals,
        category = "Travel",
        language = "മലയാളം"
      ),
      TrendingVideo(
        id = "v14",
        title = "Raigad Fort Trek & Powada | किल्ले रायगड आणि शिवकालीन इतिहास",
        duration = "14:10",
        views = "6.1M views",
        age = "2 weeks ago",
        thumbnailRes = R.drawable.player_hero_shivaji,
        category = "History & Education",
        language = "मराठी"
      ),
      TrendingVideo(
        id = "v15",
        title = "Rabindra Sangeet & Shantiniketan | আনন্দলোকে মঙ্গলালোকে",
        duration = "08:15",
        views = "2.4M views",
        age = "1 week ago",
        thumbnailRes = R.drawable.thumb_next_learning,
        category = "Music",
        language = "বাংলা"
      ),
      TrendingVideo(
        id = "v16",
        title = "Golden Temple Amritsar | ਸ਼੍ਰੀ ਹਰਿਮੰਦਰ ਸਾਹਿਬ ਦਰਸ਼ਨ",
        duration = "10:30",
        views = "5.3M views",
        age = "5 days ago",
        thumbnailRes = R.drawable.thumb_next_chamundeshwari,
        category = "Devotional",
        language = "ਪੰਜਾਬੀ"
      ),
      TrendingVideo(
        id = "v17",
        title = "Mahishasura Mardini Stotram | संस्कृत स्तोत्र धारा",
        duration = "16:00",
        views = "8.9M views",
        age = "3 days ago",
        thumbnailRes = R.drawable.thumb_next_krishna,
        category = "Devotional",
        language = "संस्कृतम्"
      ),
      TrendingVideo(
        id = "v18",
        title = "Karnataka 24x7 News Live | ಬೆಂಗಳೂರು & ರಾಜ್ಯದ ಬ್ರೇಕಿಂಗ್ ನ್ಯೂಸ್",
        duration = "LIVE",
        views = "68K watching",
        age = "Streaming Now",
        thumbnailRes = R.drawable.thumb_next_kashi,
        category = "News Live",
        language = "ಕನ್ನಡ"
      ),
      TrendingVideo(
        id = "v19",
        title = "India 24x7 National & Headline News Live | आज की ताजा खबरें लाइव",
        duration = "LIVE",
        views = "92K watching",
        age = "Streaming Now",
        thumbnailRes = R.drawable.player_hero_shivaji,
        category = "News Live",
        language = "हिन्दी / English"
      ),
      TrendingVideo(
        id = "v20",
        title = "Kannada & Youth Superhit Music Jukebox | ಟಾಪ್ ಯೂತ್ ಟ್ಯೂಬ್ ಹಾಡುಗಳು",
        duration = "22:40",
        views = "5.4M views",
        age = "2 days ago",
        thumbnailRes = R.drawable.thumb_ganesh,
        category = "Music",
        language = "ಕನ್ನಡ / हिन्दी"
      ),
      TrendingVideo(
        id = "v21",
        title = "Namma Bengaluru Stand-Up Comedy | ನಗೆ ಹಬ್ಬ ಕಾಮಿಡಿ ಸ್ಪೆಷಲ್",
        duration = "14:25",
        views = "3.9M views",
        age = "4 days ago",
        thumbnailRes = R.drawable.hero_banner_kids,
        category = "Comedy",
        language = "ಕನ್ನಡ"
      ),
      TrendingVideo(
        id = "v22",
        title = "Great Indian Family Comedy Nights | देसी कॉमेडी और मस्ती शो",
        duration = "18:10",
        views = "4.7M views",
        age = "1 week ago",
        thumbnailRes = R.drawable.thumb_animals,
        category = "Comedy",
        language = "हिन्दी / English"
      ),
      TrendingVideo(
        id = "v23",
        title = "Karnataka Kitchen Special: Ragi Mudde & Nati Style Sambar | ಹಳ್ಳಿ ಅಡುಗೆ ಮನೆ",
        duration = "09:45",
        views = "2.6M views",
        age = "5 days ago",
        thumbnailRes = R.drawable.thumb_next_dosa,
        category = "Kitchen",
        language = "ಕನ್ನಡ"
      ),
      TrendingVideo(
        id = "v24",
        title = "10-Min Royal Paneer Masala Kitchen Recipe | झटपट किचन रेसिपी",
        duration = "11:05",
        views = "3.1M views",
        age = "1 week ago",
        thumbnailRes = R.drawable.thumb_next_dosa,
        category = "Kitchen",
        language = "हिन्दी / English"
      ),
      TrendingVideo(
        id = "v25",
        title = "Startup India & Small Business Guide 2026 | ಸ್ವಂತ ಉದ್ಯಮ ಮತ್ತು ವ್ಯಾಪಾರ",
        duration = "15:50",
        views = "2.8M views",
        age = "3 days ago",
        thumbnailRes = R.drawable.thumb_next_learning,
        category = "Business",
        language = "ಕನ್ನಡ / English"
      ),
      TrendingVideo(
        id = "v26",
        title = "Stock Market, MSME & Digital Business Masterclass | व्यापार और निवेश",
        duration = "19:20",
        views = "4.2M views",
        age = "6 days ago",
        thumbnailRes = R.drawable.thumb_shivaji,
        category = "Business",
        language = "हिन्दी / ગુજરાતી"
      )
    )
  }

  val trendingVideos = remember { listOf(allVideos[17], allVideos[0], allVideos[19], allVideos[20], allVideos[24]) }

  val filterCategories = remember {
    listOf(
      "All · ಎಲ್ಲಾ",
      "News Live · ನ್ಯೂಸ್ ಲೈವ್",
      "Music · ಸಂಗೀತ",
      "Comedy · ಹಾಸ್ಯ",
      "Kitchen · ಅಡುಗೆ ಮನೆ",
      "Business · ವ್ಯಾಪಾರ",
      "Kids Zone · ಮಕ್ಕಳು",
      "Devotional · ಭಕ್ತಿ",
      "History & Education · ಶಿಕ್ಷಣ",
      "Travel · ಪ್ರವಾಸ"
    )
  }

  val displayedVideos = remember(selectedFilterCategory, selectedLanguage, filterByLanguageStrictly, allVideos) {
    val matchesLang: (TrendingVideo) -> Boolean = { video ->
      selectedLanguage == "All India" || selectedLanguage == "All" || selectedLanguage.isBlank() ||
        video.language.contains(selectedLanguage, ignoreCase = true) ||
        (selectedLanguage == "Kannada" && video.language.contains("ಕನ್ನಡ")) ||
        (selectedLanguage == "Hindi" && video.language.contains("हिन्दी")) ||
        (selectedLanguage == "Telugu" && video.language.contains("తెలుగు")) ||
        (selectedLanguage == "Tamil" && video.language.contains("தமிழ்")) ||
        (selectedLanguage == "Malayalam" && video.language.contains("മലയാളം")) ||
        (selectedLanguage == "Marathi" && video.language.contains("मराठी")) ||
        (selectedLanguage == "Bengali" && video.language.contains("বাংলা")) ||
        (selectedLanguage == "Punjabi" && video.language.contains("ਪੰਜਾਬੀ")) ||
        (selectedLanguage == "Sanskrit" && video.language.contains("संस्कृतम्")) ||
        (selectedLanguage == "Gujarati" && video.language.contains("ગુજરાતી"))
    }

    val categoryFiltered = if (selectedFilterCategory.startsWith("All")) {
      allVideos
    } else {
      val key = selectedFilterCategory.substringBefore(" ·").trim()
      allVideos.filter { video ->
        when {
          key.equals("Kitchen", ignoreCase = true) ->
            video.category.equals("Kitchen", ignoreCase = true) || video.category.equals("Cooking", ignoreCase = true)
          else -> video.category.contains(key, ignoreCase = true)
        }
      }
    }

    if (filterByLanguageStrictly && selectedLanguage != "All India" && selectedLanguage != "All") {
      val strictMatches = categoryFiltered.filter(matchesLang)
      if (strictMatches.isNotEmpty()) strictMatches else categoryFiltered
    } else {
      categoryFiltered.sortedByDescending { if (matchesLang(it)) 1 else 0 }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8F9FA))
      .testTag("home_screen_root")
  ) {
    // 1. Red Header matching uploaded mockup with India Languages Yard
    YouthTubeRedHeader(
      selectedLanguage = selectedLanguage,
      onLanguageSelect = {
        selectedLanguage = it
        filterByLanguageStrictly = true
        Toast.makeText(context, "Language set to $it", Toast.LENGTH_SHORT).show()
      },
      onMenuClick = onOpenDrawer,
      onSearchClick = { showSearchModal = true },
      onNotificationClick = {
        Toast.makeText(context, "2 new notifications from Youth Tube!", Toast.LENGTH_SHORT).show()
      },
      onProfileClick = { showLoginDialog = true },
      onOpenLanguageYard = { showIndiaLanguagesYard = true },
      onSettingsClick = onOpenSettings
    )

    // 2. Main Scrollable Content
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("home_scrollable_feed"),
      contentPadding = PaddingValues(bottom = 80.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Hero Banner Carousel
      item {
        HeroCarouselSection()
      }

      // India All Languages Yard Banner Section (14 Official Indian Languages)
      item {
        IndiaLanguagesYardHomeBanner(
          selectedLanguage = selectedLanguage,
          onLanguageSelect = { lang ->
            selectedLanguage = lang.nativeName
            filterByLanguageStrictly = true
            Toast.makeText(
              context,
              "${lang.greeting} Welcome to Youth Tube in ${lang.nativeName} (${lang.englishName})!",
              Toast.LENGTH_SHORT
            ).show()
          },
          onOpenYard = { showIndiaLanguagesYard = true }
        )
      }

      // Top Google AdMob Live Banner Ad
      item {
        AdMobBannerView(
          adHeadline = "Google AdMob · Pixel 9 Pro with Gemini Live",
          adTagline = "Next-gen AI smartphone engineered by Google. Special India festival offers!",
          adCta = "LEARN MORE"
        )
      }

      // 16 Category Grid (4 columns x 4 rows): News Live, Music, Comedy, Kitchen, Business, All & more
      item {
        CategoryGridSection(
          categories = categories,
          selectedCategoryId = selectedCategory,
          onCategoryClick = { cat ->
            selectedCategory = cat.id
            when (cat.id) {
              "all", "videos" -> {
                selectedFilterCategory = "All · ಎಲ್ಲಾ"
                filterByLanguageStrictly = false
              }
              "news_live" -> selectedFilterCategory = "News Live · ನ್ಯೂಸ್ ಲೈವ್"
              "music" -> selectedFilterCategory = "Music · ಸಂಗೀತ"
              "comedy" -> selectedFilterCategory = "Comedy · ಹಾಸ್ಯ"
              "kitchen" -> selectedFilterCategory = "Kitchen · ಅಡುಗೆ ಮನೆ"
              "business" -> selectedFilterCategory = "Business · ವ್ಯಾಪಾರ"
              "kids" -> selectedFilterCategory = "Kids Zone · ಮಕ್ಕಳು"
              "devotional" -> selectedFilterCategory = "Devotional · ಭಕ್ತಿ"
              "education" -> selectedFilterCategory = "History & Education · ಶಿಕ್ಷಣ"
              "travel" -> selectedFilterCategory = "Travel · ಪ್ರವಾಸ"
              "shorts" -> onNavigateToLiveVideo()
              "languages" -> showIndiaLanguagesYard = true
              "more" -> onOpenSettings()
              else -> Toast.makeText(context, "Browsing ${cat.title.replace("\n", " ")} on Youth Tube", Toast.LENGTH_SHORT).show()
            }
          }
        )
      }

      // Trending Videos Row
      item {
        TrendingVideosSection(
          videos = trendingVideos,
          onVideoClick = { video ->
            onPlayVideo(video)
          },
          onSeeAllClick = {
            selectedCategory = "all"
            selectedFilterCategory = "All · ಎಲ್ಲಾ"
            filterByLanguageStrictly = false
            Toast.makeText(context, "Showing All Videos below!", Toast.LENGTH_SHORT).show()
          }
        )
      }

      // -----------------------------------------------------------
      // ALL YOUTUBE VIDEOS & GOOGLE ADS SECTION
      // -----------------------------------------------------------
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp)
        ) {
          // Category Filter Chips
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = Modifier.fillMaxWidth().testTag("video_filter_chips_row")
          ) {
            items(filterCategories) { catLabel ->
              val isSelected = selectedFilterCategory == catLabel
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) Color(0xFFE50914) else Color.White,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) Color(0xFFE50914) else Color(0xFFCBD5E1)
                ),
                shadowElevation = if (isSelected) 3.dp else 1.dp,
                modifier = Modifier
                  .clickable {
                    selectedFilterCategory = catLabel
                    if (catLabel.startsWith("All")) {
                      selectedCategory = "all"
                      filterByLanguageStrictly = false
                    }
                  }
                  .testTag("filter_chip_$catLabel")
              ) {
                Text(
                  text = catLabel,
                  color = if (isSelected) Color.White else Color(0xFF1E293B),
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 12.sp,
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Section Header: All Videos · ಎಲ್ಲಾ ವೀಡಿಯೊಗಳು
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .width(4.dp)
                  .height(20.dp)
                  .clip(RoundedCornerShape(2.dp))
                  .background(Color(0xFFE50914))
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "All Videos · ಎಲ್ಲಾ ವೀಡಿಯೊಗಳು",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1E2430)
              )
            }

            Surface(
              color = Color(0xFFFFF0F1),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "${displayedVideos.size} Videos",
                color = Color(0xFFE50914),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }
      }

      // In-Feed All Videos with Interspersed Google Ads
      items(displayedVideos.size) { index ->
        val video = displayedVideos[index]

        Column {
          AllVideoFeedCard(
            video = video,
            onPlay = { onPlayVideo(video) },
            onShare = { videoToShare = video }
          )

          // 1. Google Native Ad Card after 1st video
          if (index == 0) {
            Spacer(modifier = Modifier.height(8.dp))
            GoogleAdFeedCard(
              sponsorName = "Google Pixel 9 Pro",
              headline = "Engineered by Google. Built with Gemini AI Live.",
              description = "Get world-class 8K video, 30x Super Res Zoom & Google Gemini Live AI built-in. Special festival discounts in India!",
              rating = "4.8 ★",
              reviewsCount = "1.4M",
              installCount = "10M+ users",
              ctaText = "EXPLORE PIXEL · ಅನ್ವೇಷಿಸಿ",
              sponsorInitial = "G",
              sponsorColor = Color(0xFF4285F4)
            )
          }

          // 2. Google Ad Rewarded VIP Pass after 2nd video
          if (index == 2) {
            Spacer(modifier = Modifier.height(8.dp))
            GoogleAdRewardedBanner(
              onWatchAdClick = { showRewardedAdDialog = true }
            )
          }

          // 3. Google Native Ad Card after 5th video
          if (index == 5) {
            Spacer(modifier = Modifier.height(8.dp))
            GoogleAdFeedCard(
              sponsorName = "Tata Neu Super App",
              headline = "Tata Neu - Shopping, Flights, Hotels & UPI",
              description = "Earn 5% NeuCoins on grocery, fashion, electronics, and food orders with fast delivery across Karnataka & India.",
              rating = "4.6 ★",
              reviewsCount = "850K",
              installCount = "50M+ downloads",
              ctaText = "INSTALL APP · ಸ್ಥಾಪಿಸಿ",
              sponsorInitial = "T",
              sponsorColor = Color(0xFF8E24AA)
            )
          }
        }
      }

      // Approved & Published by Admin Section
      if (publishedVideos.isNotEmpty()) {
        item {
          ApprovedPublishedSection(
            videos = publishedVideos,
            onVideoClick = { onOpenVideoPlayer() }
          )
        }
      }

      // Quick Creator Spotlight Card
      item {
        CreatorSpotlightCard(onLaunchStudio = onNavigateToLiveVideo)
      }
    }
  }

  // Google AdMob Rewarded Ad Countdown Dialog
  if (showRewardedAdDialog) {
    AdMobRewardedAdDialog(
      onDismiss = { showRewardedAdDialog = false },
      onRewardEarned = {
        Toast.makeText(context, "🎉 30 Minutes Ad-Free VIP Pass Activated! ಜಾಹೀರಾತು ರಹಿತ ವೀಕ್ಷಣೆ ಸಕ್ರಿಯವಾಗಿದೆ!", Toast.LENGTH_LONG).show()
      }
    )
  }

  // Active Video Player Dialog
  activeVideoToPlay?.let { video ->
    VideoPlaybackDialog(
      video = video,
      onDismiss = { activeVideoToPlay = null },
      onShare = {
        videoToShare = video
        activeVideoToPlay = null
      }
    )
  }

  // Social Share Sheet for WhatsApp, Facebook, Instagram
  videoToShare?.let { video ->
    SocialShareSheet(
      videoTitle = video.title,
      videoSubtitle = "Category: ${video.category} · ${video.language}",
      videoUrl = "https://youthtube.app/watch?v=${video.id}",
      onDismiss = { videoToShare = null }
    )
  }

  // Login Dialog (Mail ID Login & Admin Login)
  if (showLoginDialog) {
    LoginDialog(
      authViewModel = authViewModel,
      initialTab = 0,
      onDismiss = { showLoginDialog = false }
    )
  }

  // India All Languages Yard Modal (14 Official Indian Languages)
  if (showIndiaLanguagesYard) {
    IndiaLanguagesYardModal(
      currentSelectedLanguage = selectedLanguage,
      onLanguageSelected = { lang ->
        selectedLanguage = lang.nativeName
        filterByLanguageStrictly = true
        Toast.makeText(
          context,
          "${lang.greeting} Welcome to Youth Tube in ${lang.nativeName} (${lang.englishName})!",
          Toast.LENGTH_LONG
        ).show()
      },
      onSelectAllIndia = {
        selectedLanguage = "All India"
        filterByLanguageStrictly = false
        Toast.makeText(context, "🇮🇳 Showing Videos from All 14 Indian Languages!", Toast.LENGTH_SHORT).show()
      },
      onDismiss = { showIndiaLanguagesYard = false }
    )
  }
}

// -------------------------------------------------------------
// RED APP HEADER WITH LANGUAGE PILL & INDIA YARD
// -------------------------------------------------------------

@Composable
fun YouthTubeRedHeader(
  selectedLanguage: String,
  onLanguageSelect: (String) -> Unit,
  onMenuClick: () -> Unit,
  onSearchClick: () -> Unit,
  onNotificationClick: () -> Unit,
  onProfileClick: () -> Unit = {},
  onOpenLanguageYard: () -> Unit = {},
  onSettingsClick: () -> Unit = {}
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(
        Brush.verticalGradient(
          colors = listOf(Color(0xFFE50914), Color(0xFFD32F2F))
        )
      )
      .padding(top = 10.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
  ) {
    // Top Row: Menu, Logo, Search, Notification Bell, User Profile
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onMenuClick,
        modifier = Modifier.testTag("btn_drawer_menu")
      ) {
        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White, modifier = Modifier.size(28.dp))
      }

      // Youth Tube Header Logo
      Image(
        painter = painterResource(id = R.drawable.ic_youth_tube_header_logo),
        contentDescription = "Youth Tube Logo",
        modifier = Modifier
          .height(38.dp)
          .testTag("header_logo_image"),
        contentScale = ContentScale.Fit
      )

      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
          onClick = onSearchClick,
          modifier = Modifier.size(38.dp).testTag("btn_header_search")
        ) {
          Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White, modifier = Modifier.size(24.dp))
        }

        BadgedBox(
          badge = {
            Badge(
              containerColor = Color(0xFFFF1744),
              contentColor = Color.White
            ) {
              Text("2", fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
          },
          modifier = Modifier.padding(end = 4.dp)
        ) {
          IconButton(
            onClick = onNotificationClick,
            modifier = Modifier.size(38.dp).testTag("btn_header_notifications")
          ) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White, modifier = Modifier.size(24.dp))
          }
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Profile Avatar / Mail ID Login
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { onProfileClick() }
            .testTag("btn_header_login"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Login / Profile",
            tint = Color(0xFFE50914),
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Settings Icon Button
        IconButton(
          onClick = onSettingsClick,
          modifier = Modifier.size(38.dp).testTag("btn_header_settings")
        ) {
          Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Language Selector Capsule matching mockup: ಕನ್ನಡ | English | हिन्दी | 🇮🇳 Yard (14)
    Box(
      modifier = Modifier.fillMaxWidth(),
      contentAlignment = Alignment.Center
    ) {
      Surface(
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.testTag("language_capsule")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(3.dp)
        ) {
          LanguagePillItem(
            text = "ಕನ್ನಡ",
            isSelected = selectedLanguage == "ಕನ್ನಡ",
            onClick = { onLanguageSelect("ಕನ್ನಡ") }
          )

          Text(
            text = "|",
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 4.dp),
            fontWeight = FontWeight.Bold
          )

          LanguagePillItem(
            text = "English",
            isSelected = selectedLanguage == "English",
            onClick = { onLanguageSelect("English") }
          )

          Text(
            text = "|",
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 4.dp),
            fontWeight = FontWeight.Bold
          )

          LanguagePillItem(
            text = "हिन्दी",
            isSelected = selectedLanguage == "हिन्दी",
            onClick = { onLanguageSelect("हिन्दी") }
          )

          // If another Indian language is currently selected, display it in the capsule
          if (selectedLanguage !in listOf("ಕನ್ನಡ", "English", "हिन्दी", "All India")) {
            Text(
              text = "|",
              color = Color.LightGray,
              modifier = Modifier.padding(horizontal = 4.dp),
              fontWeight = FontWeight.Bold
            )
            LanguagePillItem(
              text = selectedLanguage,
              isSelected = true,
              onClick = { onLanguageSelect(selectedLanguage) }
            )
          }

          Text(
            text = "|",
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 4.dp),
            fontWeight = FontWeight.Bold
          )

          // Special Yard Pill
          Surface(
            color = if (selectedLanguage == "All India") Color(0xFF138808) else Color(0xFFFF6D00),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
              .clickable { onOpenLanguageYard() }
              .testTag("btn_capsule_yard")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = "🇮🇳 Yard (14) ➔",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 11.sp
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Clickable India All Languages Subtitle Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onOpenLanguageYard() }
        .testTag("header_india_yard_bar"),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Surface(
        color = Color.White.copy(alpha = 0.18f),
        shape = RoundedCornerShape(14.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "🇮🇳", fontSize = 12.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "India All Languages Yard · ಭಾರತದ ಎಲ್ಲಾ ಭಾಷೆಗಳ ತಾಣ (14 ಭಾಷೆಗಳು) ➔",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        }
      }
    }
  }
}

/**
 * In-feed Banner for India All Languages Yard with horizontal scroll of all 14 official Indian languages.
 */
@Composable
fun IndiaLanguagesYardHomeBanner(
  selectedLanguage: String,
  onLanguageSelect: (IndianLanguage) -> Unit,
  onOpenYard: () -> Unit
) {
  val languages = remember { IndianLanguagesRepository.allLanguages }

  Surface(
    shape = RoundedCornerShape(16.dp),
    color = Color.White,
    border = BorderStroke(1.dp, Color(0xFFFFD180)),
    shadowElevation = 2.dp,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp)
      .testTag("india_languages_yard_banner")
  ) {
    Column(
      modifier = Modifier.padding(12.dp)
    ) {
      // Header Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = "🇮🇳", fontSize = 20.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "India All Languages Yard",
              fontWeight = FontWeight.Black,
              fontSize = 14.sp,
              color = Color(0xFF1E2430)
            )
            Text(
              text = "ಭಾರತದ ಎಲ್ಲಾ ಭಾಷೆಗಳ ತಾಣ · 14 Official Languages",
              fontSize = 11.sp,
              color = Color(0xFFD32F2F),
              fontWeight = FontWeight.Bold
            )
          }
        }

        Surface(
          color = Color(0xFFFFF0F1),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .clickable { onOpenYard() }
            .testTag("btn_view_all_14_languages")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "All 14 ➔",
              color = Color(0xFFE50914),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Horizontal Scroll of all 14 Languages
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 2.dp)
      ) {
        items(languages) { lang ->
          val isSelected = selectedLanguage == lang.nativeName || selectedLanguage == lang.englishName
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) lang.accentColor else Color(0xFFF8FAFC),
            border = BorderStroke(
              width = if (isSelected) 1.5.dp else 1.dp,
              color = if (isSelected) lang.accentColor else Color(0xFFCBD5E1)
            ),
            modifier = Modifier
              .clickable { onLanguageSelect(lang) }
              .testTag("yard_chip_${lang.code}")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text(
                text = lang.nativeName,
                color = if (isSelected) Color.White else Color(0xFF1E293B),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "· ${lang.region.take(6)}",
                color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF64748B),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun LanguagePillItem(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(20.dp))
      .background(if (isSelected) Color(0xFF0066FF) else Color.Transparent)
      .clickable { onClick() }
      .padding(horizontal = 14.dp, vertical = 6.dp)
      .testTag("lang_tab_$text"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      color = if (isSelected) Color.White else Color(0xFF333333),
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
      fontSize = 13.sp
    )
  }
}

// -------------------------------------------------------------
// HERO FEATURE CAROUSEL BANNER
// -------------------------------------------------------------

@Composable
fun HeroCarouselSection() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 4.dp)
  ) {
    ElevatedCard(
      modifier = Modifier
        .fillMaxWidth()
        .height(175.dp)
        .testTag("hero_banner_card"),
      shape = RoundedCornerShape(16.dp),
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
      Image(
        painter = painterResource(id = R.drawable.hero_banner_kids),
        contentDescription = "Learn · Enjoy · Grow Together - A Safe & Fun Platform for All Ages",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Dots pagination indicator: ● ○ ○ ○ ○
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(8.dp)
          .clip(CircleShape)
          .background(Color(0xFFE50914))
      )
      repeat(4) {
        Spacer(modifier = Modifier.width(6.dp))
        Box(
          modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
        )
      }
    }
  }
}

// -------------------------------------------------------------
// 12 CATEGORY GRID (4 COLUMNS X 3 ROWS)
// -------------------------------------------------------------

@Composable
fun CategoryGridSection(
  categories: List<CategoryItem>,
  selectedCategoryId: String,
  onCategoryClick: (CategoryItem) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 10.dp)
      .testTag("category_grid_section")
  ) {
    val rows = categories.chunked(4)
    rows.forEachIndexed { rowIndex, rowItems ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        rowItems.forEach { cat ->
          CategoryTile(
            item = cat,
            isSelected = selectedCategoryId == cat.id,
            modifier = Modifier.weight(1f),
            onClick = { onCategoryClick(cat) }
          )
        }
      }
    }
  }
}

@Composable
fun CategoryTile(
  item: CategoryItem,
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  ElevatedCard(
    modifier = modifier
      .height(84.dp)
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .testTag("category_${item.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.elevatedCardColors(containerColor = item.color),
    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(4.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = item.icon,
        contentDescription = item.title,
        tint = Color.White,
        modifier = Modifier.size(28.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = item.title,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        maxLines = 2,
        lineHeight = 12.sp,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
    }
  }
}

// -------------------------------------------------------------
// TRENDING VIDEOS SECTION
// -------------------------------------------------------------

@Composable
fun TrendingVideosSection(
  videos: List<TrendingVideo>,
  onVideoClick: (TrendingVideo) -> Unit,
  onSeeAllClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 8.dp)
      .testTag("trending_videos_section")
  ) {
    // Header with Red Vertical Bar | Trending Videos  See All >
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .width(4.dp)
            .height(20.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color(0xFFE50914))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Trending Videos",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Black,
          color = Color(0xFF1E2430)
        )
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clickable { onSeeAllClick() }
          .padding(4.dp)
          .testTag("btn_see_all_trending")
      ) {
        Text(
          text = "See All",
          color = Color(0xFFE50914),
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
          text = ">",
          color = Color(0xFFE50914),
          fontWeight = FontWeight.Black,
          fontSize = 13.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // Horizontal Scrolling Video Cards
    LazyRow(
      contentPadding = PaddingValues(horizontal = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxWidth().testTag("trending_videos_row")
    ) {
      items(videos, key = { it.id }) { video ->
        TrendingVideoCard(
          video = video,
          onClick = { onVideoClick(video) }
        )
      }
    }
  }
}

@Composable
fun TrendingVideoCard(
  video: TrendingVideo,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(220.dp)
      .clip(RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .testTag("video_card_${video.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Thumbnail Box with Duration Badge
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
      ) {
        Image(
          painter = painterResource(id = video.thumbnailRes),
          contentDescription = video.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Duration Badge
        Surface(
          color = Color.Black.copy(alpha = 0.85f),
          shape = RoundedCornerShape(4.dp),
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(6.dp)
        ) {
          Text(
            text = video.duration,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      // Title & Metadata
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = video.title,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color(0xFF1E2430)
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${video.views} · ${video.age}",
            color = Color(0xFF757575),
            fontSize = 11.sp
          )
        }

        Icon(
          imageVector = Icons.Default.MoreVert,
          contentDescription = "Options",
          tint = Color(0xFF757575),
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

// -------------------------------------------------------------
// CREATOR SPOTLIGHT CARD
// -------------------------------------------------------------

@Composable
fun CreatorSpotlightCard(onLaunchStudio: () -> Unit) {
  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .testTag("creator_spotlight_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.elevatedCardColors(
      containerColor = Color(0xFF001637)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(Color(0xFFE50914)),
        contentAlignment = Alignment.Center
      ) {
        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Youth Tube Live Studio",
          color = Color.White,
          fontWeight = FontWeight.Black,
          fontSize = 15.sp
        )
        Text(
          text = "Broadcast your talent to millions in Kannada, English & Hindi!",
          color = Color(0xFFB0BEC5),
          fontSize = 11.sp
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Surface(
        color = Color(0xFFE50914),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
          .clip(RoundedCornerShape(18.dp))
          .clickable { onLaunchStudio() }
          .testTag("btn_go_live_spotlight")
      ) {
        Text(
          text = "Go Live",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
      }
    }
  }
}

// -------------------------------------------------------------
// VIDEO PLAYBACK DIALOG
// -------------------------------------------------------------

@Composable
fun VideoPlaybackDialog(
  video: TrendingVideo,
  onDismiss: () -> Unit,
  onShare: () -> Unit = {}
) {
  androidx.compose.material3.AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFFE50914))
        Spacer(modifier = Modifier.width(8.dp))
        Text(video.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
        ) {
          Image(
            painter = painterResource(id = video.thumbnailRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.65f))
              .align(Alignment.Center),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(36.dp))
          }
        }
        Text(
          text = "Category: ${video.category} · Language: ${video.language}",
          style = MaterialTheme.typography.bodySmall,
          color = Color.Gray
        )
        Text(
          text = "Streaming in Full HD with stereo audio on Youth Tube platform.",
          style = MaterialTheme.typography.bodyMedium
        )
      }
    },
    confirmButton = {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(
          onClick = onShare,
          modifier = Modifier.testTag("btn_share_from_playback_dialog")
        ) {
          Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Share")
        }
        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
        ) {
          Text("Close")
        }
      }
    }
  )
}

// -------------------------------------------------------------
// APPROVED & PUBLISHED VIDEOS ROW (Admin Moderation Workflow)
// -------------------------------------------------------------

@Composable
fun ApprovedPublishedSection(
  videos: List<PublishedVideo>,
  onVideoClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          color = Color(0xFF00C853),
          shape = RoundedCornerShape(4.dp)
        ) {
          Text(
            text = "LIVE",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 9.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "ಅನುಮೋದಿಸಿ ಪ್ರಕಟಿಸಲಾದ ವಿಡಿಯೋಗಳು",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = Color(0xFF1E293B)
        )
      }
      Text(
        text = "Admin Published ✔",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF00C853)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(videos) { item ->
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.White,
          shadowElevation = 2.dp,
          modifier = Modifier
            .width(180.dp)
            .clickable { onVideoClick() }
        ) {
          Column {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
            ) {
              Image(
                painter = painterResource(id = item.thumbnailRes),
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
              Surface(
                color = Color(0xFF00C853),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                  .align(Alignment.TopStart)
                  .padding(6.dp)
              ) {
                Text(
                  text = "Approved ✔",
                  color = Color.White,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
            Column(modifier = Modifier.padding(8.dp)) {
              Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "${item.category} · ${item.publishedTime}",
                fontSize = 10.sp,
                color = Color.Gray
              )
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// ALL YOUTUBE VIDEOS FEED CARD (Full-Width Card with Actions)
// -------------------------------------------------------------

@Composable
fun AllVideoFeedCard(
  video: TrendingVideo,
  onPlay: () -> Unit,
  onShare: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .clip(RoundedCornerShape(16.dp))
      .clickable { onPlay() }
      .testTag("all_video_card_${video.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // 16:9 Thumbnail Box with Duration Badge and 4K Pill
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
      ) {
        Image(
          painter = painterResource(id = video.thumbnailRes),
          contentDescription = video.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Gradient Scrim at bottom
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .align(Alignment.BottomCenter)
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
              )
            )
        )

        // Top Left LIVE or 4K UHD Badge
        Surface(
          color = if (video.duration == "LIVE") Color(0xFFD50000) else Color(0xFFE50914),
          shape = RoundedCornerShape(4.dp),
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(10.dp)
        ) {
          Text(
            text = if (video.duration == "LIVE") "● LIVE NEWS" else "4K UHD",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        // Bottom Right Duration Badge
        Surface(
          color = if (video.duration == "LIVE") Color(0xFFD50000) else Color.Black.copy(alpha = 0.85f),
          shape = RoundedCornerShape(4.dp),
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(10.dp)
        ) {
          Text(
            text = if (video.duration == "LIVE") "LIVE 24x7" else video.duration,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        // Center Play Circle Overlay
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f))
            .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            .align(Alignment.Center),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play Video",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
          )
        }
      }

      // Metadata & Channel Row
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.Top
        ) {
          // Channel Avatar
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(
                  colors = listOf(Color(0xFFE50914), Color(0xFFFF5252))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "YT",
              color = Color.White,
              fontWeight = FontWeight.Black,
              fontSize = 15.sp
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = video.title,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              lineHeight = 19.sp,
              color = Color(0xFF1E2430),
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "YOUTH TUBE",
                color = Color(0xFF64748B),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
              Spacer(modifier = Modifier.width(4.dp))
              Surface(
                color = Color(0xFF1A73E8),
                shape = CircleShape,
                modifier = Modifier.size(13.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text("✓", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
              }
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "•  ${video.views}  •  ${video.age}",
                color = Color(0xFF64748B),
                fontSize = 11.sp
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Surface(
                color = Color(0xFFFFEBEE),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = video.category,
                  color = Color(0xFFE50914),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
              Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = video.language,
                  color = Color(0xFF2E7D32),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Actions Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Button(
            onClick = onPlay,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            modifier = Modifier.height(34.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Watch · ವೀಕ್ಷಿಸಿ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }

          OutlinedButton(
            onClick = onShare,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.height(34.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF1E2430), modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Share · ಹಂಚಿಕೊಳ್ಳಿ", color = Color(0xFF1E2430), fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
            }
          }

          IconButton(
            onClick = {
              Toast.makeText(context, "Saved to Watch Later (ಉಳಿಸಲಾಗಿದೆ)!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.size(34.dp)
          ) {
            Icon(Icons.Default.MoreVert, contentDescription = "More Options", tint = Color.Gray)
          }
        }
      }
    }
  }
}


