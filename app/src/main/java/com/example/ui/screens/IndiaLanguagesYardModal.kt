package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.IndianLanguage
import com.example.model.IndianLanguagesRepository

/**
 * India All Languages Yard (ಭಾರತದ ಎಲ್ಲಾ ಭಾಷೆಗಳ ತಾಣ).
 * Provides an interactive hub to explore and select from all 14 official Indian languages.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndiaLanguagesYardModal(
  currentSelectedLanguage: String,
  onLanguageSelected: (IndianLanguage) -> Unit,
  onSelectAllIndia: () -> Unit,
  onDismiss: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var activeSelection by remember {
    mutableStateOf(
      IndianLanguagesRepository.findByName(currentSelectedLanguage)
        ?: IndianLanguagesRepository.allLanguages.first()
    )
  }

  val filteredLanguages = remember(searchQuery) {
    if (searchQuery.isBlank()) {
      IndianLanguagesRepository.allLanguages
    } else {
      val q = searchQuery.trim().lowercase()
      IndianLanguagesRepository.allLanguages.filter {
        it.englishName.lowercase().contains(q) ||
            it.nativeName.lowercase().contains(q) ||
            it.region.lowercase().contains(q) ||
            it.kannadaRegion.lowercase().contains(q) ||
            it.code.lowercase().contains(q)
      }
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .fillMaxHeight(0.92f)
        .testTag("india_languages_yard_dialog"),
      shape = RoundedCornerShape(24.dp),
      color = Color.White,
      shadowElevation = 16.dp
    ) {
      Column(
        modifier = Modifier.fillMaxSize()
      ) {
        // -------------------------------------------------------------
        // GRADIENT TRICOLOR TOP BAR
        // -------------------------------------------------------------
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  Color(0xFFFF9933), // Indian Saffron
                  Color(0xFFFFFFFF), // White
                  Color(0xFF138808)  // Indian Green
                )
              )
            )
            .height(5.dp)
        )

        // Header Section
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color(0xFFFFF1F2), Color.White)
              )
            )
            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                color = Color(0xFFE50914),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language Yard",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "🇮🇳 INDIA ALL LANGUAGES YARD",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1E2430)
                  )
                }
                Text(
                  text = "ಭಾರತದ ಎಲ್ಲಾ ಭಾಷೆಗಳ ತಾಣ · 14 Official Languages",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color(0xFFD32F2F),
                  fontWeight = FontWeight.Bold
                )
              }
            }

            IconButton(
              onClick = onDismiss,
              modifier = Modifier.testTag("btn_close_language_yard")
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Yard",
                tint = Color(0xFF555555)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Search Field
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
              Text(
                "Search language / ಭಾಷೆಯನ್ನು ಹುಡುಕಿ (e.g. Kannada, हिन्दी, Telugu)...",
                fontSize = 12.sp,
                color = Color.Gray
              )
            },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFFE50914))
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                }
              }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFFE50914),
              unfocusedBorderColor = Color(0xFFE2E8F0),
              focusedContainerColor = Color(0xFFFAFAFA),
              unfocusedContainerColor = Color(0xFFFAFAFA)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("input_search_languages")
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Quick Filter Strip: All India Option + Popular Chips
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp)
          ) {
            item {
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (currentSelectedLanguage == "All India") Color(0xFF1E2430) else Color(0xFFF1F5F9),
                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                modifier = Modifier
                  .clickable {
                    onSelectAllIndia()
                    onDismiss()
                  }
                  .testTag("chip_all_india")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Text(text = "🇮🇳", fontSize = 13.sp)
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "All India · ಎಲ್ಲಾ ಭಾಷೆಗಳು",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentSelectedLanguage == "All India") Color.White else Color(0xFF1E293B)
                  )
                }
              }
            }

            items(listOf("ಕನ್ನಡ", "हिन्दी", "English", "తెలుగు", "தமிழ்", "मराठी", "বাংলা")) { langName ->
              val isCurrent = currentSelectedLanguage == langName
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isCurrent) Color(0xFFE50914) else Color.White,
                border = BorderStroke(1.dp, if (isCurrent) Color(0xFFE50914) else Color(0xFFCBD5E1)),
                modifier = Modifier
                  .clickable {
                    val lang = IndianLanguagesRepository.findByName(langName)
                    if (lang != null) {
                      activeSelection = lang
                      onLanguageSelected(lang)
                      onDismiss()
                    }
                  }
                  .testTag("quick_chip_$langName")
              ) {
                Text(
                  text = langName,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isCurrent) Color.White else Color(0xFF334155),
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
              }
            }
          }
        }

        Divider(color = Color(0xFFE2E8F0), thickness = 1.dp)

        // Active Selection Banner
        Surface(
          color = activeSelection.accentColor.copy(alpha = 0.08f),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, activeSelection.accentColor.copy(alpha = 0.3f))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Active Greeting: ",
                  fontSize = 11.sp,
                  color = Color.Gray,
                  fontWeight = FontWeight.Medium
                )
                Text(
                  text = "\"${activeSelection.greeting}\"",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Black,
                  color = activeSelection.accentColor
                )
              }
              Text(
                text = "${activeSelection.nativeName} (${activeSelection.englishName}) · ${activeSelection.kannadaRegion}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155)
              )
            }

            Button(
              onClick = {
                onLanguageSelected(activeSelection)
                onDismiss()
              },
              colors = ButtonDefaults.buttonColors(containerColor = activeSelection.accentColor),
              shape = RoundedCornerShape(20.dp),
              contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
              modifier = Modifier.testTag("btn_apply_active_language")
            ) {
              Text("Apply · ಅನ್ವಯಿಸಿ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        // -------------------------------------------------------------
        // SCROLLABLE LIST OF ALL 14 INDIAN LANGUAGES
        // -------------------------------------------------------------
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(horizontal = 14.dp)
            .testTag("indian_languages_list"),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp)
        ) {
          item {
            Text(
              text = "14 OFFICIAL INDIAN LANGUAGES (ಭಾರತೀಯ ಭಾಷೆಗಳು)",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = Color.Gray,
              modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
            )
          }

          items(filteredLanguages) { lang ->
            val isSelected = currentSelectedLanguage == lang.nativeName ||
                (activeSelection.code == lang.code && currentSelectedLanguage == lang.englishName)

            IndianLanguageCard(
              language = lang,
              isSelected = isSelected,
              onClick = {
                activeSelection = lang
                onLanguageSelected(lang)
                onDismiss()
              }
            )
          }

          if (filteredLanguages.isEmpty()) {
            item {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(40.dp),
                contentAlignment = Alignment.Center
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "ಯಾವುದೇ ಭಾಷೆ ಕಂಡುಬಂದಿಲ್ಲ",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 14.sp
                  )
                  Text(
                    text = "No languages matching '$searchQuery'",
                    fontSize = 12.sp,
                    color = Color.LightGray
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

/**
 * Individual Language Item Card inside the India Languages Yard.
 */
@Composable
fun IndianLanguageCard(
  language: IndianLanguage,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = if (isSelected) language.accentColor.copy(alpha = 0.06f) else Color.White,
    border = BorderStroke(
      width = if (isSelected) 2.dp else 1.dp,
      color = if (isSelected) language.accentColor else Color(0xFFE2E8F0)
    ),
    shadowElevation = if (isSelected) 3.dp else 1.dp,
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("lang_card_${language.code}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Big Native Letter Avatar
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = language.accentColor,
        modifier = Modifier.size(52.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Text(
            text = language.nativeName.take(2),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Language details
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = language.nativeName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = Color(0xFF1E2430)
          )

          Spacer(modifier = Modifier.width(6.dp))

          Text(
            text = "(${language.englishName})",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B),
            fontWeight = FontWeight.Medium
          )

          language.popularBadge?.let { badge ->
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              color = language.accentColor,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = badge,
                color = Color.White,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Region & Greeting
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            color = Color(0xFFF1F5F9),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "📍 ${language.kannadaRegion} · ${language.region}",
              fontSize = 10.sp,
              color = Color(0xFF475569),
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Surface(
            color = language.accentColor.copy(alpha = 0.12f),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "🗣️ ${language.greeting}",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = language.accentColor,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = language.kannadaDescription,
          fontSize = 11.sp,
          color = Color(0xFF334155),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Selection Checkmark / Radio
      if (isSelected) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "Selected",
          tint = language.accentColor,
          modifier = Modifier.size(24.dp)
        )
      } else {
        Box(
          modifier = Modifier
            .size(20.dp)
            .border(2.dp, Color(0xFFCBD5E1), CircleShape)
        )
      }
    }
  }
}
