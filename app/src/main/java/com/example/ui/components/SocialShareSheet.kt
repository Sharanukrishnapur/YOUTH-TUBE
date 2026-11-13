package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialShareSheet(
  videoTitle: String,
  videoSubtitle: String = "Watch on YOUTH TUBE · Your Video, Our World!",
  videoUrl: String = "https://youthtube.app/watch?v=shivaji4k",
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val shareMessage = "🔥 Watch \"$videoTitle\" on YOUTH TUBE!\nYour Video, Our World! 🇮🇳\n$videoUrl"

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(0xFF1E1E1E),
    dragHandle = {
      Box(
        modifier = Modifier
          .padding(top = 10.dp, bottom = 4.dp)
          .size(width = 40.dp, height = 4.dp)
          .clip(RoundedCornerShape(2.dp))
          .background(Color(0xFF555555))
      )
    },
    modifier = Modifier.testTag("social_share_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      // Header: Title and Close button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Share Video",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
          Text(
            text = videoTitle,
            color = Color(0xFFAAAAAA),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color(0xFFAAAAAA),
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Primary Social Targets: WhatsApp, Facebook, Instagram, GitHub
      Text(
        text = "DIRECT SHARE",
        color = Color(0xFF888888),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        // 1. WhatsApp Button
        SocialShareIconItem(
          label = "WhatsApp",
          subtitle = "Chats",
          testTag = "share_whatsapp_btn",
          modifier = Modifier.weight(1f),
          onClick = {
            shareToWhatsApp(context, shareMessage)
            onDismiss()
          }
        ) {
          WhatsAppLogoIcon(modifier = Modifier.size(48.dp))
        }

        // 2. Facebook Button
        SocialShareIconItem(
          label = "Facebook",
          subtitle = "Feed",
          testTag = "share_facebook_btn",
          modifier = Modifier.weight(1f),
          onClick = {
            shareToFacebook(context, videoUrl, shareMessage)
            onDismiss()
          }
        ) {
          FacebookLogoIcon(modifier = Modifier.size(48.dp))
        }

        // 3. Instagram Button
        SocialShareIconItem(
          label = "Instagram",
          subtitle = "Stories",
          testTag = "share_instagram_btn",
          modifier = Modifier.weight(1f),
          onClick = {
            shareToInstagram(context, shareMessage)
            onDismiss()
          }
        ) {
          InstagramLogoIcon(modifier = Modifier.size(48.dp))
        }

        // 4. GitHub Button
        SocialShareIconItem(
          label = "GitHub",
          subtitle = "Gist / Repo",
          testTag = "share_github_btn",
          modifier = Modifier.weight(1f),
          onClick = {
            shareToGitHub(context, videoTitle, videoUrl, shareMessage)
            onDismiss()
          }
        ) {
          GitHubLogoIcon(modifier = Modifier.size(48.dp))
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
      HorizontalDivider(color = Color(0xFF2E2E2E))
      Spacer(modifier = Modifier.height(16.dp))

      // Secondary Actions: Copy Link & More Options
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Copy Link Tile
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF2A2A2A),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3A3A3A)),
          modifier = Modifier
            .weight(1f)
            .clickable {
              copyToClipboard(context, videoUrl)
              onDismiss()
            }
            .testTag("share_copy_link_btn")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF3F3F46)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy Link",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("Copy Link", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("To clipboard", color = Color(0xFFAAAAAA), fontSize = 10.sp)
            }
          }
        }

        // More Apps Native Chooser Tile
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF2A2A2A),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3A3A3A)),
          modifier = Modifier
            .weight(1f)
            .clickable {
              shareGeneral(context, videoTitle, shareMessage)
              onDismiss()
            }
            .testTag("share_more_apps_btn")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFE50914)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "More Apps",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("More Apps", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Twitter, SMS, etc.", color = Color(0xFFAAAAAA), fontSize = 10.sp)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

// -------------------------------------------------------------
// SOCIAL ICON BUTTON COMPONENT
// -------------------------------------------------------------

@Composable
fun SocialShareIconItem(
  label: String,
  subtitle: String,
  testTag: String,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  iconContent: @Composable () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(vertical = 8.dp, horizontal = 2.dp)
      .testTag(testTag)
  ) {
    iconContent()
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = label,
      color = Color.White,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      textAlign = TextAlign.Center
    )
    Text(
      text = subtitle,
      color = Color(0xFF888888),
      fontSize = 10.sp,
      textAlign = TextAlign.Center
    )
  }
}

// -------------------------------------------------------------
// CUSTOM HIGH-FIDELITY VECTOR LOGO ICONS
// -------------------------------------------------------------

// 1. WhatsApp Logo (Green Circle + White Speech Bubble + Handset)
@Composable
fun WhatsAppLogoIcon(modifier: Modifier = Modifier) {
  Canvas(modifier = modifier) {
    val radius = size.minDimension / 2f
    val center = Offset(size.width / 2f, size.height / 2f)

    // Green Background Circle
    drawCircle(
      color = Color(0xFF25D366),
      radius = radius,
      center = center
    )

    // Speech bubble path
    val bubblePath = Path().apply {
      moveTo(center.x, center.y - radius * 0.45f)
      // Rounded bubble
      addOval(
        androidx.compose.ui.geometry.Rect(
          center.x - radius * 0.5f,
          center.y - radius * 0.5f,
          center.x + radius * 0.5f,
          center.y + radius * 0.45f
        )
      )
      // Tail pointing to bottom-left
      moveTo(center.x - radius * 0.35f, center.y + radius * 0.35f)
      lineTo(center.x - radius * 0.5f, center.y + radius * 0.55f)
      lineTo(center.x - radius * 0.15f, center.y + radius * 0.45f)
      close()
    }

    drawPath(path = bubblePath, color = Color.White)

    // Phone Handset inside bubble (green arc receiver)
    val phoneArc = Path().apply {
      moveTo(center.x - radius * 0.22f, center.y + radius * 0.15f)
      quadraticTo(
        center.x - radius * 0.3f,
        center.y - radius * 0.15f,
        center.x + radius * 0.15f,
        center.y - radius * 0.25f
      )
      lineTo(center.x + radius * 0.25f, center.y - radius * 0.12f)
      lineTo(center.x + radius * 0.12f, center.y - radius * 0.05f)
      quadraticTo(
        center.x - radius * 0.05f,
        center.y + radius * 0.05f,
        center.x - radius * 0.08f,
        center.y + radius * 0.12f
      )
      lineTo(center.x - radius * 0.18f, center.y + radius * 0.25f)
      close()
    }
    drawPath(path = phoneArc, color = Color(0xFF25D366))
  }
}

// 2. Facebook Logo (Blue Circle + White "f")
@Composable
fun FacebookLogoIcon(modifier: Modifier = Modifier) {
  Canvas(modifier = modifier) {
    val radius = size.minDimension / 2f
    val center = Offset(size.width / 2f, size.height / 2f)

    // Facebook Blue Background Circle
    drawCircle(
      color = Color(0xFF1877F2),
      radius = radius,
      center = center
    )

    // White "f"
    val fPath = Path().apply {
      val left = center.x - radius * 0.05f
      val crossY = center.y - radius * 0.05f
      val topY = center.y - radius * 0.55f
      val botY = center.y + radius * 0.85f

      moveTo(left, botY)
      lineTo(left + radius * 0.28f, botY)
      lineTo(left + radius * 0.28f, crossY + radius * 0.12f)
      lineTo(left + radius * 0.52f, crossY + radius * 0.12f)
      lineTo(left + radius * 0.46f, crossY - radius * 0.12f)
      lineTo(left + radius * 0.28f, crossY - radius * 0.12f)
      lineTo(left + radius * 0.28f, topY + radius * 0.25f)
      quadraticTo(left + radius * 0.28f, topY, left + radius * 0.55f, topY)
      lineTo(left + radius * 0.55f, topY - radius * 0.22f)
      quadraticTo(left - radius * 0.05f, topY - radius * 0.22f, left, topY + radius * 0.25f)
      lineTo(left, crossY - radius * 0.12f)
      lineTo(left - radius * 0.2f, crossY - radius * 0.12f)
      lineTo(left - radius * 0.2f, crossY + radius * 0.12f)
      lineTo(left, crossY + radius * 0.12f)
      close()
    }

    drawPath(path = fPath, color = Color.White)
  }
}

// 3. Instagram Logo (Vibrant Radial/Linear Gradient + Camera Outline & Lens)
@Composable
fun InstagramLogoIcon(modifier: Modifier = Modifier) {
  Canvas(modifier = modifier) {
    val radius = size.minDimension / 2f
    val center = Offset(size.width / 2f, size.height / 2f)

    // Gradient background circle: Yellow -> Red -> Pink -> Purple
    val gradient = Brush.linearGradient(
      colors = listOf(
        Color(0xFFFEDA75),
        Color(0xFFFA7E1E),
        Color(0xFFD62976),
        Color(0xFF962FBF),
        Color(0xFF4F5BD5)
      ),
      start = Offset(0f, size.height),
      end = Offset(size.width, 0f)
    )

    drawCircle(
      brush = gradient,
      radius = radius,
      center = center
    )

    // Camera Rounded Rectangle Outline
    val camSize = radius * 1.05f
    val strokeWidth = radius * 0.12f
    drawRoundRect(
      color = Color.White,
      topLeft = Offset(center.x - camSize / 2f, center.y - camSize / 2f),
      size = Size(camSize, camSize),
      cornerRadius = CornerRadius(camSize * 0.28f, camSize * 0.28f),
      style = Stroke(width = strokeWidth)
    )

    // Center Lens Circle
    drawCircle(
      color = Color.White,
      radius = camSize * 0.24f,
      center = center,
      style = Stroke(width = strokeWidth)
    )

    // Top-Right Flash Dot
    drawCircle(
      color = Color.White,
      radius = camSize * 0.07f,
      center = Offset(center.x + camSize * 0.27f, center.y - camSize * 0.27f)
    )
  }
}

// 4. GitHub Logo (Dark Circle + White Code-Branch / Octo Crest)
@Composable
fun GitHubLogoIcon(modifier: Modifier = Modifier) {
  Canvas(modifier = modifier) {
    val radius = size.minDimension / 2f
    val center = Offset(size.width / 2f, size.height / 2f)

    // GitHub Dark Background Circle with subtle border
    drawCircle(
      color = Color(0xFF24292E),
      radius = radius,
      center = center
    )
    drawCircle(
      color = Color.White.copy(alpha = 0.22f),
      radius = radius - 1.dp.toPx(),
      center = center,
      style = Stroke(width = 1.5.dp.toPx())
    )

    // Stylized White Git Cat / Code Fork Emblem inside circle
    val strokeW = radius * 0.14f
    val topLeft = Offset(center.x - radius * 0.26f, center.y - radius * 0.24f)
    val bottomLeft = Offset(center.x - radius * 0.26f, center.y + radius * 0.3f)
    val topRight = Offset(center.x + radius * 0.26f, center.y - radius * 0.14f)

    // Vertical main branch line
    drawLine(
      color = Color.White,
      start = topLeft,
      end = bottomLeft,
      strokeWidth = strokeW
    )

    // Curved merge branch to right node
    val branchPath = Path().apply {
      moveTo(bottomLeft.x, bottomLeft.y - radius * 0.1f)
      quadraticTo(
        center.x + radius * 0.24f,
        center.y + radius * 0.12f,
        topRight.x,
        topRight.y
      )
    }
    drawPath(path = branchPath, color = Color.White, style = Stroke(width = strokeW))

    // Three Git commit nodes
    drawCircle(color = Color.White, radius = radius * 0.15f, center = topLeft)
    drawCircle(color = Color.White, radius = radius * 0.15f, center = bottomLeft)
    drawCircle(color = Color(0xFF2EA44F), radius = radius * 0.15f, center = topRight)
    drawCircle(
      color = Color.White,
      radius = radius * 0.15f,
      center = topRight,
      style = Stroke(width = strokeW * 0.55f)
    )
  }
}

// -------------------------------------------------------------
// INTENT HELPERS FOR WHATSAPP, FACEBOOK, INSTAGRAM, GITHUB
// -------------------------------------------------------------

fun shareToWhatsApp(context: Context, text: String) {
  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, text)
    setPackage("com.whatsapp")
  }
  try {
    context.startActivity(intent)
  } catch (_: Exception) {
    // If WhatsApp app is not installed, open web or fallback chooser
    try {
      val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://api.whatsapp.com/send?text=${Uri.encode(text)}")
      )
      context.startActivity(webIntent)
    } catch (_: Exception) {
      val chooser = Intent.createChooser(
        Intent(Intent.ACTION_SEND).apply {
          type = "text/plain"
          putExtra(Intent.EXTRA_TEXT, text)
        },
        "Share via WhatsApp"
      )
      context.startActivity(chooser)
    }
  }
}

fun shareToFacebook(context: Context, url: String, text: String) {
  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, "$text\n$url")
    setPackage("com.facebook.katana")
  }
  try {
    context.startActivity(intent)
  } catch (_: Exception) {
    // Web fallback for Facebook
    try {
      val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.facebook.com/sharer/sharer.php?u=${Uri.encode(url)}")
      )
      context.startActivity(webIntent)
    } catch (_: Exception) {
      val chooser = Intent.createChooser(
        Intent(Intent.ACTION_SEND).apply {
          type = "text/plain"
          putExtra(Intent.EXTRA_TEXT, "$text\n$url")
        },
        "Share to Facebook"
      )
      context.startActivity(chooser)
    }
  }
}

fun shareToInstagram(context: Context, text: String) {
  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, text)
    setPackage("com.instagram.android")
  }
  try {
    context.startActivity(intent)
  } catch (_: Exception) {
    val chooser = Intent.createChooser(
      Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
      },
      "Share to Instagram"
    )
    context.startActivity(chooser)
  }
}

fun shareToGitHub(context: Context, title: String, url: String, text: String) {
  val markdownShare = "### [$title]($url)\n\n$text\n\nShared from **Youth Tube** Android App (`#YouthTube`)"
  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  clipboard.setPrimaryClip(ClipData.newPlainText("GitHub Markdown Share", markdownShare))

  val githubAppIntent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_SUBJECT, title)
    putExtra(Intent.EXTRA_TEXT, markdownShare)
    setPackage("com.github.android")
  }
  try {
    context.startActivity(githubAppIntent)
    Toast.makeText(context, "Markdown copied & opening GitHub!", Toast.LENGTH_SHORT).show()
  } catch (_: Exception) {
    try {
      val gistIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gist.github.com/"))
      context.startActivity(gistIntent)
      Toast.makeText(context, "GitHub Markdown copied to clipboard! Paste into your Gist/Issue.", Toast.LENGTH_LONG).show()
    } catch (_: Exception) {
      val chooser = Intent.createChooser(
        Intent(Intent.ACTION_SEND).apply {
          type = "text/plain"
          putExtra(Intent.EXTRA_SUBJECT, title)
          putExtra(Intent.EXTRA_TEXT, markdownShare)
        },
        "Share to GitHub"
      )
      context.startActivity(chooser)
    }
  }
}

fun shareGeneral(context: Context, title: String, text: String) {
  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_SUBJECT, title)
    putExtra(Intent.EXTRA_TEXT, text)
  }
  context.startActivity(Intent.createChooser(intent, "Share \"$title\" via"))
}

fun copyToClipboard(context: Context, text: String) {
  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val clip = ClipData.newPlainText("Youth Tube Video", text)
  clipboard.setPrimaryClip(clip)
  Toast.makeText(context, "Link copied to clipboard! 📋", Toast.LENGTH_SHORT).show()
}
