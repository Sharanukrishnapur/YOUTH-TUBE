package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.DevTask
import com.example.model.TaskPriority
import com.example.model.TaskStatus
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.viewmodel.DevPulseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
  viewModel: DevPulseViewModel,
  modifier: Modifier = Modifier
) {
  val allTasks by viewModel.allTasks.collectAsState()
  val filteredTasks by viewModel.filteredTasks.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedStatus by viewModel.selectedStatusFilter.collectAsState()
  val selectedPriority by viewModel.selectedPriorityFilter.collectAsState()

  var showAddDialog by remember { mutableStateOf(false) }

  val totalCount = allTasks.size
  val doneCount = allTasks.count { it.status == TaskStatus.DONE }
  val inProgressCount = allTasks.count { it.status == TaskStatus.IN_PROGRESS }
  val progress = if (totalCount > 0) doneCount.toFloat() / totalCount else 0f

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("tasks_lazy_column"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 1. Sprint Summary Card
      item {
        SprintOverviewCard(
          totalCount = totalCount,
          doneCount = doneCount,
          inProgressCount = inProgressCount,
          progress = progress
        )
      }

      // 2. Search Field
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { viewModel.setSearchQuery(it) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("task_search_field"),
          placeholder = { Text("Search tasks, categories, or keywords...") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { viewModel.setSearchQuery("") }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear search")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp)
        )
      }

      // 3. Status Filters
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "Filter Status",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            item {
              FilterChip(
                selected = selectedStatus == null,
                onClick = { viewModel.setStatusFilter(null) },
                label = { Text("All (${allTasks.size})") },
                modifier = Modifier.testTag("filter_status_all"),
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                )
              )
            }
            items(TaskStatus.values()) { status ->
              val count = allTasks.count { it.status == status }
              FilterChip(
                selected = selectedStatus == status,
                onClick = { viewModel.setStatusFilter(status) },
                label = { Text("${status.label} ($count)") },
                modifier = Modifier.testTag("filter_status_${status.name.lowercase()}"),
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                )
              )
            }
          }
        }
      }

      // 4. Priority Filter Chips
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "Filter Priority",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            item {
              FilterChip(
                selected = selectedPriority == null,
                onClick = { viewModel.setPriorityFilter(null) },
                label = { Text("Any Priority") },
                modifier = Modifier.testTag("filter_priority_any")
              )
            }
            items(TaskPriority.values()) { priority ->
              FilterChip(
                selected = selectedPriority == priority,
                onClick = { viewModel.setPriorityFilter(priority) },
                label = { Text(priority.label) },
                modifier = Modifier.testTag("filter_priority_${priority.name.lowercase()}")
              )
            }
          }
        }
      }

      // 5. Tasks List
      if (filteredTasks.isEmpty()) {
        item {
          EmptyTasksState()
        }
      } else {
        items(filteredTasks, key = { it.id }) { task ->
          TaskCard(
            task = task,
            onToggleStatus = {
              val nextStatus = when (task.status) {
                TaskStatus.BACKLOG -> TaskStatus.IN_PROGRESS
                TaskStatus.IN_PROGRESS -> TaskStatus.REVIEW
                TaskStatus.REVIEW -> TaskStatus.DONE
                TaskStatus.DONE -> TaskStatus.BACKLOG
              }
              viewModel.updateTaskStatus(task.id, nextStatus)
            },
            onDelete = { viewModel.deleteTask(task.id) }
          )
        }
      }

      // Bottom padding so items aren't occluded by FAB
      item {
        Spacer(modifier = Modifier.height(72.dp))
      }
    }

    // Add Task FAB
    FloatingActionButton(
      onClick = { showAddDialog = true },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("add_task_fab"),
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
      Icon(Icons.Default.Add, contentDescription = "Add New Task")
    }

    if (showAddDialog) {
      AddTaskDialog(
        onDismiss = { showAddDialog = false },
        onConfirm = { title, desc, category, priority ->
          viewModel.addTask(title, desc, category, priority)
          showAddDialog = false
        }
      )
    }
  }
}

@Composable
fun SprintOverviewCard(
  totalCount: Int,
  doneCount: Int,
  inProgressCount: Int,
  progress: Float
) {
  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("sprint_overview_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )
  ) {
    Column(
      modifier = Modifier.padding(18.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Active Sprint Tracker",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "$doneCount of $totalCount completed ($inProgressCount in progress)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Text(
          text = "${(progress * 100).toInt()}%",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Black,
          color = MaterialTheme.colorScheme.primary
        )
      }

      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.outlineVariant,
        strokeCap = StrokeCap.Round
      )
    }
  }
}

@Composable
fun TaskCard(
  task: DevTask,
  onToggleStatus: () -> Unit,
  onDelete: () -> Unit
) {
  val isCompleted = task.status == TaskStatus.DONE

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("task_card_${task.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isCompleted)
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
      else
        MaterialTheme.colorScheme.surface
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isCompleted) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
      else MaterialTheme.colorScheme.outlineVariant
    )
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
        IconButton(
          onClick = onToggleStatus,
          modifier = Modifier.size(32.dp).testTag("toggle_task_${task.id}")
        ) {
          Icon(
            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = "Toggle status",
            tint = if (isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = task.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
          if (task.description.isNotBlank()) {
            Text(
              text = task.description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(32.dp).testTag("delete_task_${task.id}")
        ) {
          Icon(
            imageVector = Icons.Default.DeleteOutline,
            contentDescription = "Delete task",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().padding(start = 40.dp)
      ) {
        StatusBadge(status = task.status)
        PriorityBadge(priority = task.priority)
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "#${task.category}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
fun EmptyTasksState() {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 32.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = "No Tasks Found",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "Try adjusting your search query or status filter, or tap + to create a new engineering task.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
    }
  }
}

@Composable
fun AddTaskDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, desc: String, category: String, priority: TaskPriority) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Compose") }
  var priority by remember { mutableStateOf(TaskPriority.HIGH) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Create Sprint Task", fontWeight = FontWeight.Bold) },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Task Title") },
          placeholder = { Text("e.g., Implement Room DAO for bookmarks") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("add_task_title_input")
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description (Optional)") },
          placeholder = { Text("Details, acceptance criteria, or links") },
          modifier = Modifier.fillMaxWidth().testTag("add_task_desc_input"),
          maxLines = 3
        )

        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("Category Tag") },
          placeholder = { Text("Compose, Architecture, DevOps...") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("add_task_category_input")
        )

        Text(
          text = "Priority Level",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          TaskPriority.values().forEach { p ->
            FilterChip(
              selected = priority == p,
              onClick = { priority = p },
              label = { Text(p.label) },
              modifier = Modifier.testTag("dialog_priority_${p.name.lowercase()}")
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(title, description, category, priority)
          }
        },
        enabled = title.isNotBlank(),
        modifier = Modifier.testTag("dialog_confirm_button")
      ) {
        Text("Create Task")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
