package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedLanguage by remember { mutableStateOf("Both") } // "Both", "English", "ಕನ್ನಡ"

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              color = Color(0xFFE50914),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.size(32.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.Security,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Privacy Policy",
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                color = Color(0xFF1E293B)
              )
              Text(
                text = "ಗೌಪ್ಯತಾ ನೀತಿ · Google Play Compliant",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
              )
            }
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("btn_privacy_back")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color(0xFF1E293B)
            )
          }
        },
        actions = {
          IconButton(
            onClick = {
              copyPolicyToClipboard(context)
            },
            modifier = Modifier.testTag("btn_copy_policy")
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy Policy Text",
              tint = Color(0xFFE50914)
            )
          }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = Color.White
        )
      )
    },
    modifier = modifier.fillMaxSize()
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF8FAFC))
        .padding(paddingValues)
        .testTag("privacy_policy_list"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Google Play Console Compliance Banner
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth().testTag("play_compliance_badge")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                color = Color(0xFF00C853),
                shape = RoundedCornerShape(6.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Google Play Console Verified", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
              }

              Text("v1.0 • 2026", color = Color(0xFF94A3B8), fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "Youth Tube Privacy & Data Safety Policy",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "This policy discloses how Youth Tube collects, uses, and safeguards user data—including device camera, microphone, Google AdMob advertisements, and user accounts in compliance with Google Play Developer Policies and COPPA family guidelines.",
              color = Color(0xFFCBD5E1),
              fontSize = 12.sp,
              lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                onClick = { copyPolicyToClipboard(context) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(34.dp)
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy Policy for Play Console", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // 2. Language Filter Pills
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("Both", "English", "ಕನ್ನಡ").forEach { lang ->
            val isSelected = selectedLanguage == lang
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) Color(0xFFE50914) else Color.White,
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFFE50914) else Color(0xFFCBD5E1)),
              modifier = Modifier.clickable { selectedLanguage = lang }
            ) {
              Text(
                text = when (lang) {
                  "Both" -> "Bilingual (ಎರಡೂ)"
                  "English" -> "English Only"
                  else -> "ಕನ್ನಡ ಮಾತ್ರ"
                },
                color = if (isSelected) Color.White else Color(0xFF334155),
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
              )
            }
          }
        }
      }

      // 3. Section: Camera Access
      item {
        PolicySectionCard(
          icon = Icons.Default.CameraAlt,
          iconTint = Color(0xFFE50914),
          badgeText = "android.permission.CAMERA",
          title = "Camera Access & Video Recording",
          titleKn = "ಕ್ಯಾಮೆರಾ ಪ್ರವೇಶ ಮತ್ತು ವೀಡಿಯೊ ರೆಕಾರ್ಡಿಂಗ್",
          englishContent = "• Used exclusively when the user chooses to record or broadcast video in 'Live Video & Camera Studio' or 'Shorts'.\n• Camera feeds are processed in real-time on-device for live video streaming and filters.\n• The app NEVER accesses your camera in the background or without explicit user activation.\n• No biometric or facial recognition data is retained, stored, or sold.",
          kannadaContent = "• ಬಳಕೆದಾರರು ಲೈವ್ ವೀಡಿಯೊ ಸ್ಟುಡಿಯೋ ಅಥವಾ ಶಾರ್ಟ್ಸ್ ರೆಕಾರ್ಡ್ ಮಾಡಲು ಸ್ವತಃ ಆಯ್ಕೆ ಮಾಡಿದಾಗ ಮಾತ್ರ ಕ್ಯಾಮೆರಾ ಬಳಸಲಾಗುತ್ತದೆ.\n• ಹಿನ್ನೆಲೆಯಲ್ಲಿ (Background) ಕ್ಯಾಮೆರಾವನ್ನು ಎಂದಿಗೂ ಪ್ರವೇಶಿಸುವುದಿಲ್ಲ.\n• ಯಾವುದೇ ಮುಖ ಗುರುತಿಸುವಿಕೆ (Biometrics) ಡೇಟಾವನ್ನು ಸಂಗ್ರಹಿಸುವುದಿಲ್ಲ ಅಥವಾ ಮಾರಾಟ ಮಾಡುವುದಿಲ್ಲ.",
          selectedLanguage = selectedLanguage
        )
      }

      // 4. Section: Microphone Access
      item {
        PolicySectionCard(
          icon = Icons.Default.Mic,
          iconTint = Color(0xFF00C853),
          badgeText = "android.permission.RECORD_AUDIO",
          title = "Microphone & Audio Capture",
          titleKn = "ಮೈಕ್ರೊಫೋನ್ ಮತ್ತು ಧ್ವನಿ ರೆಕಾರ್ಡಿಂಗ್",
          englishContent = "• Required only to record user audio during live broadcasts, commentary, or video creation.\n• Audio is never recorded, monitored, or transmitted in the background.\n• Real-time audio spectrum visualizer in the Video Player operates solely on active stream playback.",
          kannadaContent = "• ಲೈವ್ ಪ್ರಸಾರ ಅಥವಾ ವೀಡಿಯೊ ರಚನೆಯ ಸಮಯದಲ್ಲಿ ನಿಮ್ಮ ಧ್ವನಿ ರೆಕಾರ್ಡ್ ಮಾಡಲು ಮಾತ್ರ ಮೈಕ್ರೊಫೋನ್ ಅನುಮತಿ ಬಳಸಲಾಗುತ್ತದೆ.\n• ಯಾವುದೇ ಸಮಯದಲ್ಲಿ ಬಳಕೆದಾರರ ಗಮನಕ್ಕೆ ಬಾರದೆ ಅಥವಾ ಹಿನ್ನೆಲೆಯಲ್ಲಿ ಆಡಿಯೋ ರೆಕಾರ್ಡ್ ಮಾಡಲಾಗುವುದಿಲ್ಲ.",
          selectedLanguage = selectedLanguage
        )
      }

      // 5. Section: Google AdMob & Advertising
      item {
        PolicySectionCard(
          icon = Icons.Default.Public,
          iconTint = Color(0xFFFFB300),
          badgeText = "Google AdMob SDK Compliance",
          title = "Google AdMob & Advertising Disclosures",
          titleKn = "ಗೂಗಲ್ ಆಡ್‌ಮಾಬ್ ಜಾಹೀರಾತು ಮಾಹಿತಿ",
          englishContent = "• Youth Tube displays advertisements using Google AdMob (Test IDs: ca-app-pub-3940256099942544/6300978111, Rewarded: ca-app-pub-3940256099942544/5224354917).\n• Google AdMob may use device advertising identifiers (GAID) and coarse location to serve relevant ads.\n• Users can opt out of personalized ads at any time via Android Device Settings or in-app Ad Preferences.\n• Ads displayed comply strictly with Google Play Ad Policy and Families Self-Certified Ads SDK requirements.",
          kannadaContent = "• ಯೂತ್ ಟ್ಯೂಬ್ Google AdMob ತಂತ್ರಜ್ಞಾನವನ್ನು ಬಳಸಿ ಬ್ಯಾನರ್ ಮತ್ತು ರಿವಾರ್ಡೆಡ್ ಜಾಹೀರಾತುಗಳನ್ನು ಪ್ರದರ್ಶಿಸುತ್ತದೆ.\n• ಬಳಕೆದಾರರು ಯಾವುದೇ ಸಮಯದಲ್ಲಿ ತಮ್ಮ ಸಾಧನದ ಸೆಟ್ಟಿಂಗ್ಸ್ ಮೂಲಕ ವೈಯಕ್ತೀಕರಿಸಿದ ಜಾಹೀರಾತುಗಳನ್ನು ಆಫ್ ಮಾಡಬಹುದು.\n• ಎಲ್ಲಾ ಜಾಹೀರಾತುಗಳು ಗೂಗಲ್ ಪ್ಲೇ ಜಾಹೀರಾತು ನೀತಿಗಳಿಗೆ ಸಂಪೂರ್ಣವಾಗಿ ಬದ್ಧವಾಗಿವೆ.",
          selectedLanguage = selectedLanguage
        )
      }

      // 6. Section: Children's Privacy (Kids Zone & COPPA)
      item {
        PolicySectionCard(
          icon = Icons.Default.ChildCare,
          iconTint = Color(0xFF1E88E5),
          badgeText = "COPPA & Google Play Families Policy",
          title = "Child Safety & Kids Zone Protection",
          titleKn = "ಮಕ್ಕಳ ಸುರಕ್ಷತೆ ಮತ್ತು ಕಿಡ್ಸ್ ಜೋನ್ ರಕ್ಷಣೆ",
          englishContent = "• Youth Tube includes a dedicated 'Kids Zone' designed for young audiences and families.\n• We do NOT knowingly collect personal information from children under 13.\n• All content in the Kids Zone undergoes strict Admin Moderation before being published.\n• Advertisements served in child-directed sections adhere to Google Play Families Policy and COPPA standards.",
          kannadaContent = "• ಯೂತ್ ಟ್ಯೂಬ್ ಮಕ್ಕಳಿಗಾಗಿ ವಿಶೇಷ 'ಕಿಡ್ಸ್ ಜೋನ್' ವಿಭಾಗವನ್ನು ಹೊಂದಿದ್ದು, 13 ವರ್ಷಕ್ಕಿಂತ ಕೆಳಗಿನ ಮಕ್ಕಳ ಯಾವುದೇ ವೈಯಕ್ತಿಕ ವಿವರಗಳನ್ನು ಸಂಗ್ರಹಿಸುವುದಿಲ್ಲ.\n• ಕಿಡ್ಸ್ ಜೋನ್‌ನಲ್ಲಿ ಪ್ರಕಟವಾಗುವ ಪ್ರತಿಯೊಂದು ವೀಡಿಯೊವನ್ನು ಅಡ್ಮಿನ್ ಪರಿಶೀಲಿಸಿ ಅನುಮೋದಿಸಿದ ನಂತರವೇ ಪ್ರಕಟಿಸಲಾಗುತ್ತದೆ.",
          selectedLanguage = selectedLanguage
        )
      }

      // 7. Section: User Accounts & Data Security
      item {
        PolicySectionCard(
          icon = Icons.Default.Lock,
          iconTint = Color(0xFF8E24AA),
          badgeText = "User Credentials & Auth",
          title = "Account Information & Data Protection",
          titleKn = "ಬಳಕೆದಾರರ ಖಾತೆ ಮಾಹಿತಿ ಮತ್ತು ಡೇಟಾ ಭದ್ರತೆ",
          englishContent = "• When you sign in using Mail ID or register an account, we store your email address and display name securely.\n• Passwords are encrypted and never stored in plain text.\n• User data is never rented, traded, or sold to third parties.\n• You may request complete account and data deletion at any time by contacting our support team.",
          kannadaContent = "• ಇಮೇಲ್ ಮೂಲಕ ಸೈನ್ ಇನ್ ಆದಾಗ ನಿಮ್ಮ ಇಮೇಲ್ ಮತ್ತು ಪ್ರೊಫೈಲ್ ಹೆಸರನ್ನು ಮಾತ್ರ ಸುರಕ್ಷಿತವಾಗಿರಿಸಿಕೊಳ್ಳಲಾಗುತ್ತದೆ.\n• ನಿಮ್ಮ ಡೇಟಾವನ್ನು ಯಾವುದೇ ಮೂರನೇ ವ್ಯಕ್ತಿಗಳಿಗೆ ಮಾರಾಟ ಮಾಡುವುದಿಲ್ಲ.\n• ಯಾವುದೇ ಸಮಯದಲ್ಲಿ ನಿಮ್ಮ ಖಾತೆಯನ್ನು ಶಾಶ್ವತವಾಗಿ ಅಳಿಸಲು (Delete) ವಿನಂತಿಸಬಹುದು.",
          selectedLanguage = selectedLanguage
        )
      }

      // 8. Contact & Developer Information
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth().testTag("privacy_contact_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFFE50914), modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Developer & Privacy Contact", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Youth Tube Platform Team", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF334155))
            Text("Email: sharanukrishnapur48@gmail.com", fontSize = 12.sp, color = Color(0xFF64748B))
            Text("Karnataka, India • Made for India 🇮🇳", fontSize = 12.sp, color = Color(0xFF64748B))

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "If you have questions about this Privacy Policy or wish to exercise your data rights, please contact us at the email above. Responses are provided within 48 hours.",
              fontSize = 11.sp,
              color = Color(0xFF64748B),
              lineHeight = 15.sp
            )
          }
        }
      }
    }
  }
}

@Composable
fun PolicySectionCard(
  icon: ImageVector,
  iconTint: Color,
  badgeText: String,
  title: String,
  titleKn: String,
  englishContent: String,
  kannadaContent: String,
  selectedLanguage: String
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = CircleShape,
          color = iconTint.copy(alpha = 0.12f),
          modifier = Modifier.size(36.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
          }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
          Text(text = titleKn, fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Surface(
        color = Color(0xFFF1F5F9),
        shape = RoundedCornerShape(4.dp)
      ) {
        Text(
          text = badgeText,
          fontSize = 10.sp,
          color = Color(0xFF475569),
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (selectedLanguage == "Both" || selectedLanguage == "English") {
        Text(
          text = englishContent,
          fontSize = 12.sp,
          color = Color(0xFF334155),
          lineHeight = 17.sp
        )
      }

      if (selectedLanguage == "Both") {
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFF1F5F9))
        Spacer(modifier = Modifier.height(8.dp))
      }

      if (selectedLanguage == "Both" || selectedLanguage == "ಕನ್ನಡ") {
        Text(
          text = kannadaContent,
          fontSize = 12.sp,
          color = Color(0xFF1E293B),
          lineHeight = 17.sp
        )
      }
    }
  }
}

private fun copyPolicyToClipboard(context: Context) {
  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val text = """
# Privacy Policy for Youth Tube

**Last updated:** November 2026
**Developer:** Youth Tube Platform (sharanukrishnapur48@gmail.com)

## 1. Overview
Youth Tube ("we", "our") is committed to protecting your privacy. This policy explains how we handle device permissions, user data, and advertising.

## 2. Camera Access (android.permission.CAMERA)
- Camera access is requested only when users explicitly initiate live video streaming, shorts recording, or broadcast studio features.
- Camera frames are processed in real-time on-device and are NEVER accessed in the background.
- No biometric or facial recognition data is harvested or retained.

## 3. Microphone Access (android.permission.RECORD_AUDIO)
- Used solely for live audio broadcasting and video recording at the user's voluntary command.
- The microphone is never monitored or recorded in the background.

## 4. Google AdMob Advertising
- We display advertisements via Google AdMob (Test IDs: ca-app-pub-3940256099942544/6300978111, Rewarded: ca-app-pub-3940256099942544/5224354917).
- Ads comply strictly with Google Play Developer Policy and Families Policy.
- Users may opt out of personalized ads via device settings.

## 5. Child Safety & Kids Zone (COPPA Compliance)
- Youth Tube provides an age-appropriate Kids Zone.
- We do not knowingly collect personal information from children under 13.
- All child-directed videos are reviewed by human moderators before publication.

## 6. User Accounts & Data Security
- Mail ID logins store your email and public profile name securely.
- You can request full data and account deletion by contacting sharanukrishnapur48@gmail.com.
  """.trimIndent()

  val clip = ClipData.newPlainText("Youth Tube Privacy Policy", text)
  clipboard.setPrimaryClip(clip)
  Toast.makeText(context, "Privacy Policy copied to clipboard! (Google Play Console ಗೆ ನಕಲಿಸಲಾಗಿದೆ)", Toast.LENGTH_LONG).show()
}
