package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Input
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CodeSnippet
import com.example.ui.theme.CodeEditorBg
import com.example.ui.theme.CodeText
import com.example.viewmodel.DevPulseViewModel

@Composable
fun SnippetsScreen(
  viewModel: DevPulseViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val snippets by viewModel.snippets.collectAsState()
  val scratchpadText by viewModel.scratchpadText.collectAsState()

  var selectedCategory by remember { mutableStateOf<String?>(null) }

  val categories = remember(snippets) {
    listOf("All") + snippets.map { it.category }.distinct()
  }

  val filteredSnippets = remember(snippets, selectedCategory) {
    if (selectedCategory == null || selectedCategory == "All") {
      snippets
    } else {
      snippets.filter { it.category == selectedCategory }
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("snippets_screen_column"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Interactive Mobile Scratchpad
    item {
      ScratchpadCard(
        text = scratchpadText,
        onTextChange = { viewModel.updateScratchpad(it) },
        onCopy = {
          copyToClipboard(context, "DevPulse Scratchpad", scratchpadText)
          Toast.makeText(context, "Scratchpad copied to clipboard!", Toast.LENGTH_SHORT).show()
        },
        onClear = {
          viewModel.updateScratchpad("")
          Toast.makeText(context, "Scratchpad cleared", Toast.LENGTH_SHORT).show()
        }
      )
    }

    // 2. Snippet Categories Filter
    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = "Snippet Templates Library",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(categories) { category ->
            val isSelected = (selectedCategory == null && category == "All") || (selectedCategory == category)
            FilterChip(
              selected = isSelected,
              onClick = { selectedCategory = if (category == "All") null else category },
              label = { Text(category) },
              modifier = Modifier.testTag("category_chip_${category.lowercase()}")
            )
          }
        }
      }
    }

    // 3. Snippets Cards
    items(filteredSnippets, key = { it.id }) { snippet ->
      SnippetCard(
        snippet = snippet,
        onInsert = {
          viewModel.appendSnippetToScratchpad(snippet)
          Toast.makeText(context, "Inserted '${snippet.title}' into Scratchpad!", Toast.LENGTH_SHORT).show()
        },
        onCopy = {
          copyToClipboard(context, snippet.title, snippet.code)
          Toast.makeText(context, "Copied '${snippet.title}' to clipboard!", Toast.LENGTH_SHORT).show()
        }
      )
    }

    item {
      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@Composable
fun ScratchpadCard(
  text: String,
  onTextChange: (String) -> Unit,
  onCopy: () -> Unit,
  onClear: () -> Unit
) {
  val lines = text.lines().size
  val chars = text.length
  val words = if (text.isBlank()) 0 else text.trim().split("\\s+".toRegex()).size

  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("scratchpad_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    )
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
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = "Code",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Live Code Scratchpad",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "$lines lines · $words words · $chars chars",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          IconButton(
            onClick = onCopy,
            modifier = Modifier.size(36.dp).testTag("copy_scratchpad_button")
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy code",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(18.dp)
            )
          }
          IconButton(
            onClick = onClear,
            modifier = Modifier.size(36.dp).testTag("clear_scratchpad_button")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = "Clear code",
              tint = MaterialTheme.colorScheme.error,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      // Code editor box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(CodeEditorBg)
          .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
      ) {
        TextField(
          value = text,
          onValueChange = onTextChange,
          modifier = Modifier
            .fillMaxSize()
            .testTag("scratchpad_text_field"),
          colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
          ),
          textStyle = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            color = CodeText,
            lineHeight = 18.sp
          )
        )
      }
    }
  }
}

@Composable
fun SnippetCard(
  snippet: CodeSnippet,
  onInsert: () -> Unit,
  onCopy: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("snippet_card_${snippet.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
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
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = snippet.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = snippet.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Surface(
          color = MaterialTheme.colorScheme.secondaryContainer,
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = snippet.category,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      // Code preview block
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(CodeEditorBg)
          .padding(10.dp)
          .horizontalScroll(rememberScrollState())
      ) {
        Text(
          text = snippet.code.trim(),
          fontFamily = FontFamily.Monospace,
          fontSize = 11.sp,
          color = CodeText,
          lineHeight = 16.sp
        )
      }

      // Action buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedButton(
          onClick = onCopy,
          modifier = Modifier.testTag("copy_snippet_${snippet.id}"),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Copy", style = MaterialTheme.typography.labelMedium)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
          onClick = onInsert,
          modifier = Modifier.testTag("insert_snippet_${snippet.id}"),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.Input, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Insert into Pad", style = MaterialTheme.typography.labelMedium)
        }
      }
    }
  }
}

private fun copyToClipboard(context: Context, label: String, text: String) {
  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val clip = ClipData.newPlainText(label, text)
  clipboard.setPrimaryClip(clip)
}
