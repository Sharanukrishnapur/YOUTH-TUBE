package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ContrastCheckResult
import com.example.viewmodel.DevPulseViewModel
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DesignStudioScreen(
  viewModel: DevPulseViewModel,
  modifier: Modifier = Modifier
) {
  val fgHex by viewModel.designForegroundColor.collectAsState()
  val bgHex by viewModel.designBackgroundColor.collectAsState()
  val contrastResult by viewModel.contrastResult.collectAsState()

  var inputFg by remember(fgHex) { mutableStateOf(fgHex) }
  var inputBg by remember(bgHex) { mutableStateOf(bgHex) }

  val fgColor = remember(fgHex) { DevPulseViewModel.parseColorSafe(fgHex, Color(0xFF00D2FF)) }
  val bgColor = remember(bgHex) { DevPulseViewModel.parseColorSafe(bgHex, Color(0xFF0B0F19)) }

  val presetPalettes = listOf(
    "Cyan Cyber" to Pair("#00D2FF", "#0B0F19"),
    "Neon Violet" to Pair("#B388FF", "#0B0F19"),
    "Mint Emerald" to Pair("#00E5A3", "#0B0F19"),
    "Sunset Amber" to Pair("#FF9100", "#14110A"),
    "Coral Blaze" to Pair("#FF5252", "#140A0A"),
    "High Contrast Dark" to Pair("#FFFFFF", "#000000"),
    "Clean Light" to Pair("#00677D", "#FFFFFF")
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("design_studio_column"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Contrast Ratio Banner
    item {
      ContrastResultCard(
        contrast = contrastResult,
        fgColor = fgColor,
        bgColor = bgColor
      )
    }

    // 2. Preset Swatches
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("preset_palettes_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Preset Color Harmonies",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            presetPalettes.forEach { (name, pair) ->
              val isSelected = fgHex == pair.first && bgHex == pair.second
              Surface(
                modifier = Modifier
                  .clip(RoundedCornerShape(20.dp))
                  .clickable {
                    inputFg = pair.first
                    inputBg = pair.second
                    viewModel.setDesignColors(pair.first, pair.second)
                  }
                  .testTag("palette_chip_${name.lowercase().replace(" ", "_")}"),
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                )
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(12.dp)
                      .clip(CircleShape)
                      .background(DevPulseViewModel.parseColorSafe(pair.first, Color.Cyan))
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }
        }
      }
    }

    // 3. Custom Hex Inputs
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("color_inputs_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ColorLens, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Custom Hex Values",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            OutlinedTextField(
              value = inputFg,
              onValueChange = {
                inputFg = it
                if (it.length in 4..9) viewModel.setDesignColors(it, bgHex)
              },
              label = { Text("Text / FG Hex") },
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("fg_hex_input")
            )

            OutlinedTextField(
              value = inputBg,
              onValueChange = {
                inputBg = it
                if (it.length in 4..9) viewModel.setDesignColors(fgHex, it)
              },
              label = { Text("Surface / BG Hex") },
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("bg_hex_input")
            )
          }
        }
      }
    }

    // 4. M3 Typography Scale Preview
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("typography_preview_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.TextFields, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Material 3 Typography Hierarchy",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Text("Display Large (57sp)", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
          Text("Headline Medium (28sp)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
          Text("Title Medium (16sp)", style = MaterialTheme.typography.titleMedium)
          Text("Body Large (16sp) - Clean legible paragraph text for mobile apps.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("Label Small (11sp) - For tags, badges, and metadata", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
      }
    }

    // 5. M3 Components Sandbox
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("components_sandbox_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Text(
            text = "Interactive Component Sandbox",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Button(onClick = {}) { Text("Filled Button") }
            ElevatedButton(onClick = {}) { Text("Elevated") }
            FilledTonalButton(onClick = {}) { Text("Tonal") }
            OutlinedButton(onClick = {}) { Text("Outlined") }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun ContrastResultCard(
  contrast: ContrastCheckResult,
  fgColor: Color,
  bgColor: Color
) {
  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("contrast_result_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "WCAG 2.1 Contrast Ratio",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Accessibility evaluation",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Text(
          text = String.format(Locale.US, "%.1f : 1", contrast.ratio),
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Black,
          color = if (contrast.aaNormal) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
        )
      }

      // Live Sample text preview box with selected colors
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(bgColor)
          .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
          .padding(14.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "DevPulse Live Contrast Preview",
            color = fgColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          Text(
            text = "Testing foreground readability directly on top of background canvas.",
            color = fgColor.copy(alpha = 0.85f),
            fontSize = 13.sp
          )
        }
      }

      // Compliance Chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        ComplianceTag("AA Normal", contrast.aaNormal, Modifier.weight(1f))
        ComplianceTag("AA Large", contrast.aaLarge, Modifier.weight(1f))
        ComplianceTag("AAA Normal", contrast.aaaNormal, Modifier.weight(1f))
        ComplianceTag("AAA Large", contrast.aaaLarge, Modifier.weight(1f))
      }
    }
  }
}

@Composable
fun ComplianceTag(
  label: String,
  passed: Boolean,
  modifier: Modifier = Modifier
) {
  val color = if (passed) Color(0xFF00E5A3) else Color(0xFFFF5252)

  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(8.dp),
    color = color.copy(alpha = 0.12f),
    border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f))
  ) {
    Row(
      modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = if (passed) Icons.Default.Check else Icons.Default.Close,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(12.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = label,
        color = color,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp
      )
    }
  }
}
