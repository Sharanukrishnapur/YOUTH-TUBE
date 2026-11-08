package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun AdMobBannerView(
  modifier: Modifier = Modifier,
  adHeadline: String = "Tata Neu - Shopping, Travel & UPI",
  adTagline: String = "Download India's Super App. Exclusive rewards!",
  adCta: String = "INSTALL",
  adUnitId: String = "ca-app-pub-3940256099942544/6300978111"
) {
  val context = LocalContext.current
  var isDismissed by remember { mutableStateOf(false) }

  if (isDismissed) return

  Surface(
    shape = RoundedCornerShape(10.dp),
    color = Color(0xFF1E293B),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .testTag("admob_banner_view")
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      // Top Ad Header row: "Ad" badge, Test Ad label, Close
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Yellow "Ad" Badge matching AdMob standard
          Surface(
            color = Color(0xFFFFB300),
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = "Ad · ಜಾಹೀರಾತು",
              color = Color.Black,
              fontWeight = FontWeight.Black,
              fontSize = 9.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Text(
            text = "Google AdMob Test Ad (320x50)",
            color = Color(0xFF94A3B8),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
          )
        }

        IconButton(
          onClick = { isDismissed = true },
          modifier = Modifier.size(20.dp)
        ) {
          Icon(Icons.Default.Close, contentDescription = "Close Ad", tint = Color(0xFF94A3B8), modifier = Modifier.size(14.dp))
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Main Ad Content Row: Icon, Headline/Desc, CTA Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Ad App Icon
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
              Brush.linearGradient(
                colors = listOf(Color(0xFFE50914), Color(0xFFFF6D00))
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "T",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Headline & Ratings
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = adHeadline,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Row {
              repeat(5) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(11.dp))
              }
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "4.6 ★ (2M+)",
              color = Color(0xFFCBD5E1),
              fontSize = 10.sp
            )
          }
          Text(
            text = adTagline,
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Install / CTA Button
        Button(
          onClick = {
            Toast.makeText(context, "Google AdMob Click Registered! Opening Sponsor...", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
          shape = RoundedCornerShape(8.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          modifier = Modifier.height(34.dp)
        ) {
          Text(
            text = adCta,
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}

// Rewarded Ad Simulation Dialog
@Composable
fun AdMobRewardedAdDialog(
  onDismiss: () -> Unit,
  onRewardEarned: () -> Unit
) {
  val context = LocalContext.current
  var secondsLeft by remember { mutableStateOf(5) }
  var isCompleted by remember { mutableStateOf(false) }

  androidx.compose.runtime.LaunchedEffect(Unit) {
    while (secondsLeft > 0) {
      kotlinx.coroutines.delay(1000)
      secondsLeft--
    }
    isCompleted = true
  }

  Dialog(onDismissRequest = {}) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
      modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Surface(
          color = Color(0xFFFFB300),
          shape = RoundedCornerShape(4.dp)
        ) {
          Text(
            text = "Google AdMob Rewarded Ad · ಜಾಹೀರಾತು",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = if (isCompleted) "🎉 Reward Unlocked! ಬಹುಮಾನ ಲಭಿಸಿದೆ!" else "Reward in $secondsLeft seconds...",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Watch this test ad to earn 30 minutes of Ad-Free Youth Tube HD streaming!",
          color = Color(0xFF94A3B8),
          fontSize = 12.sp,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (isCompleted) {
          Button(
            onClick = {
              Toast.makeText(context, "30 Min Ad-Free Streaming Activated!", Toast.LENGTH_SHORT).show()
              onRewardEarned()
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Claim Reward & Continue (ಮುಂದುವರಿಯಿರಿ)", fontWeight = FontWeight.Bold)
          }
        } else {
          Button(
            onClick = {},
            enabled = false,
            colors = ButtonDefaults.buttonColors(disabledContainerColor = Color(0xFF334155)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Watching Video ($secondsLeft s)", color = Color.Gray)
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// IN-FEED NATIVE GOOGLE AD CARD (YOUTUBE FEED SPONSORED CARD)
// -------------------------------------------------------------

@Composable
fun GoogleAdFeedCard(
  modifier: Modifier = Modifier,
  sponsorName: String = "Google Pixel 9 Pro",
  headline: String = "Engineered by Google. Built with Gemini AI.",
  description: String = "Get unmatched photography, best-in-class zoom & Gemini Live. Exclusive festival offers available now!",
  rating: String = "4.8 ★",
  reviewsCount: String = "1.4M reviews",
  installCount: String = "10M+ users",
  ctaText: String = "EXPLORE NOW · ಅನ್ವೇಷಿಸಿ",
  thumbnailRes: Int? = null,
  sponsorInitial: String = "G",
  sponsorColor: Color = Color(0xFF4285F4),
  adUnitId: String = "ca-app-pub-3940256099942544/2247696110"
) {
  val context = LocalContext.current
  var showInfoDialog by remember { mutableStateOf(false) }
  var isDismissed by remember { mutableStateOf(false) }

  if (isDismissed) return

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .testTag("google_ad_feed_card"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Top Sponsor Attribution Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFF1F5F9))
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Yellow "Ad" Badge matching Google Ads standard
          Surface(
            color = Color(0xFFFFB300),
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = "Ad · ಜಾಹೀರಾತು",
              color = Color.Black,
              fontWeight = FontWeight.Black,
              fontSize = 9.sp,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Text(
            text = "Google Ads · ಪ್ರಾಯೋಜಿತ",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF475569)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = { showInfoDialog = true },
            modifier = Modifier.size(24.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = "Why this Ad?",
              tint = Color(0xFF64748B),
              modifier = Modifier.size(16.dp)
            )
          }
          IconButton(
            onClick = { isDismissed = true },
            modifier = Modifier.size(24.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Dismiss Ad",
              tint = Color(0xFF64748B),
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Ad Preview Media Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp)
          .background(
            Brush.linearGradient(
              colors = listOf(sponsorColor.copy(alpha = 0.85f), Color(0xFF1E293B))
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(16.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = Color.White,
            modifier = Modifier.size(48.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                text = sponsorInitial,
                color = sponsorColor,
                fontWeight = FontWeight.Black,
                fontSize = 26.sp
              )
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = sponsorName,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          Text(
            text = "Official Google Certified Partner",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 11.sp
          )
        }
      }

      // Ad Details & Actions
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = headline,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFF0F172A),
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              repeat(5) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = Color(0xFFFFB300),
                  modifier = Modifier.size(13.dp)
                )
              }
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "$rating ($reviewsCount) • $installCount",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = description,
          fontSize = 12.sp,
          color = Color(0xFF334155),
          lineHeight = 16.sp,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Large CTA Button
        Button(
          onClick = {
            Toast.makeText(context, "Google Ads: Opening $sponsorName offer...", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .testTag("btn_google_ad_cta")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = ctaText,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.Default.OpenInNew,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }

  if (showInfoDialog) {
    GoogleAdPreferencesDialog(
      adUnitId = adUnitId,
      sponsorName = sponsorName,
      onDismiss = { showInfoDialog = false }
    )
  }
}

// -------------------------------------------------------------
// GOOGLE AD REWARDED BANNER (EARN AD-FREE STREAMING)
// -------------------------------------------------------------

@Composable
fun GoogleAdRewardedBanner(
  onWatchAdClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = Color(0xFF0F172A),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .testTag("google_ad_rewarded_banner")
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(Color(0xFFFFB300)),
        contentAlignment = Alignment.Center
      ) {
        Text("🎁", fontSize = 22.sp)
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            color = Color(0xFFE50914),
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = "VIP PASS",
              color = Color.White,
              fontWeight = FontWeight.Black,
              fontSize = 9.sp,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Ad-Free Streaming",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
        Text(
          text = "Watch 1 Google Test Ad to enjoy 30 mins zero ads!",
          color = Color(0xFF94A3B8),
          fontSize = 11.sp
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Button(
        onClick = onWatchAdClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
        shape = RoundedCornerShape(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Text("Watch", fontWeight = FontWeight.Bold, fontSize = 12.sp)
      }
    }
  }
}

// -------------------------------------------------------------
// GOOGLE AD PREFERENCES & INFO DIALOG
// -------------------------------------------------------------

@Composable
fun GoogleAdPreferencesDialog(
  adUnitId: String,
  sponsorName: String,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var personalizedAds by remember { mutableStateOf(true) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              color = Color(0xFFFFB300),
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = "Ad · ಮಾಹಿತಿ",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "About Google Ads",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = Color(0xFF0F172A)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Advertiser: $sponsorName",
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          color = Color(0xFF1E293B)
        )
        Text(
          text = "AdMob Unit: $adUnitId",
          fontSize = 11.sp,
          color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Google Ads on Youth Tube are delivered to support free streaming for all creators and families in India. Ads follow Google Play Developer & AdMob policies.",
          fontSize = 12.sp,
          color = Color(0xFF475569),
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Personalized Ads", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1E293B))
            Text("Based on your interests", fontSize = 10.sp, color = Color.Gray)
          }
          androidx.compose.material3.Switch(
            checked = personalizedAds,
            onCheckedChange = { personalizedAds = it }
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            Toast.makeText(context, "Preferences saved for Google Ads", Toast.LENGTH_SHORT).show()
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Done · ಮುಕ್ತಾಯ", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
