package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.SystemTelemetry
import com.example.ui.components.MetricCard
import com.example.ui.components.PulsingDot
import com.example.viewmodel.DevPulseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelemetryScreen(
  viewModel: DevPulseViewModel,
  modifier: Modifier = Modifier
) {
  val telemetry by viewModel.telemetry.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("telemetry_screen_column"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Live Telemetry Header
    item {
      LiveTelemetryBanner(
        telemetry = telemetry,
        onRefresh = { viewModel.refreshTelemetry() }
      )
    }

    // 2. Battery & RAM Progress Gauges
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        BatteryGaugeCard(
          batteryLevel = telemetry.batteryLevel,
          isCharging = telemetry.isCharging,
          powerSource = telemetry.powerSource,
          modifier = Modifier.weight(1f)
        )
        RamGaugeCard(
          usedMb = telemetry.ramUsedMb,
          totalMb = telemetry.ramTotalMb,
          percent = telemetry.ramPercent,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // 3. Device & System Specs
    item {
      DeviceSpecsCard(telemetry = telemetry)
    }

    // 4. Display & Density Metrics
    item {
      DisplayMetricsCard(telemetry = telemetry)
    }

    // 5. Sensors & Hardware Peripherals
    item {
      SensorsInventoryCard(telemetry = telemetry)
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun LiveTelemetryBanner(
  telemetry: SystemTelemetry,
  onRefresh: () -> Unit
) {
  val dateStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    .format(Date(telemetry.refreshedAtMillis))

  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("telemetry_banner_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          PulsingDot()
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Live System Telemetry",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Text(
          text = "Real Android APIs · Last updated $dateStr",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Button(
        onClick = onRefresh,
        modifier = Modifier.testTag("refresh_telemetry_button"),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
      ) {
        Icon(Icons.Default.Refresh, contentDescription = "Refresh", modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Refresh")
      }
    }
  }
}

@Composable
fun BatteryGaugeCard(
  batteryLevel: Int,
  isCharging: Boolean,
  powerSource: String,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.testTag("battery_gauge_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
        Text(
          text = "Battery",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
          imageVector = Icons.Default.BatteryChargingFull,
          contentDescription = null,
          tint = if (isCharging) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
      }

      Text(
        text = "$batteryLevel%",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.onSurface
      )

      LinearProgressIndicator(
        progress = { (batteryLevel / 100f).coerceIn(0f, 1f) },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = if (batteryLevel > 20) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
        trackColor = MaterialTheme.colorScheme.outlineVariant,
        strokeCap = StrokeCap.Round
      )

      Text(
        text = if (isCharging) "Charging ($powerSource)" else "Discharging",
        style = MaterialTheme.typography.labelSmall,
        color = if (isCharging) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun RamGaugeCard(
  usedMb: Long,
  totalMb: Long,
  percent: Int,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.testTag("ram_gauge_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
        Text(
          text = "RAM Memory",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
          imageVector = Icons.Default.Memory,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.size(20.dp)
        )
      }

      Text(
        text = "$percent%",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.onSurface
      )

      LinearProgressIndicator(
        progress = { (percent / 100f).coerceIn(0f, 1f) },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.outlineVariant,
        strokeCap = StrokeCap.Round
      )

      Text(
        text = "${usedMb}MB / ${totalMb}MB",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun DeviceSpecsCard(telemetry: SystemTelemetry) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("device_specs_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Smartphone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Device & Operating System",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      SpecRow("Device Model", telemetry.deviceModel)
      SpecRow("Manufacturer", telemetry.manufacturer)
      SpecRow("Android Version", "Android ${telemetry.androidVersion}")
      SpecRow("Target SDK / API", "API Level ${telemetry.apiLevel}")
      SpecRow("Network Transport", telemetry.networkStatus)
    }
  }
}

@Composable
fun DisplayMetricsCard(telemetry: SystemTelemetry) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("display_metrics_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Display & Screen Metrics",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      SpecRow("Viewport Dimension", "${telemetry.screenWidthDp}dp × ${telemetry.screenHeightDp}dp")
      SpecRow("Screen Density", "${telemetry.screenDensityDpi} DPI")
      SpecRow("Density Bucket", when {
        telemetry.screenDensityDpi >= 560 -> "xxxhdpi"
        telemetry.screenDensityDpi >= 400 -> "xxhdpi"
        telemetry.screenDensityDpi >= 280 -> "xhdpi"
        telemetry.screenDensityDpi >= 200 -> "hdpi"
        else -> "mdpi"
      })
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SensorsInventoryCard(telemetry: SystemTelemetry) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("sensors_inventory_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
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
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Sensors, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Hardware Sensors",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Surface(
          color = MaterialTheme.colorScheme.tertiaryContainer,
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "${telemetry.sensorCount} Found",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        telemetry.availableSensors.forEach { sensorName ->
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
          ) {
            Text(
              text = sensorName,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun SpecRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}
