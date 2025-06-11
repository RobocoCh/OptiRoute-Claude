package com.optiroute.com.presentation.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.optiroute.com.domain.models.Vehicle
import com.optiroute.com.domain.models.VehicleType
import com.optiroute.com.utils.toDateString

@Composable
fun VehicleCard(
    vehicle: Vehicle,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onToggleAvailability: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VehicleTypeIcon(vehicleType = vehicle.vehicleType)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = vehicle.licensePlate,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = vehicle.vehicleType.name.replace("_", " "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                StatusChip(
                    text = if (vehicle.isAvailable) "Available" else "Busy",
                    isActive = vehicle.isAvailable
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vehicle Specifications
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VehicleSpecItem(
                    icon = Icons.Default.Scale,
                    label = "Capacity",
                    value = "${vehicle.capacity} ${vehicle.capacityUnit}",
                    modifier = Modifier.weight(1f)
                )

                VehicleSpecItem(
                    icon = Icons.Default.LocalGasStation,
                    label = "Fuel Usage",
                    value = "${vehicle.fuelConsumption} L/100km",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Additional Information
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    InfoRow(
                        label = "Depot ID",
                        value = vehicle.depotId.take(8) + "..."
                    )

                    if (vehicle.kurirId.isNotEmpty()) {
                        InfoRow(
                            label = "Assigned Kurir",
                            value = vehicle.kurirId.take(8) + "..."
                        )
                    }

                    InfoRow(
                        label = "Created",
                        value = vehicle.createdAt.toDateString("dd MMM yyyy")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (onToggleAvailability != null) {
                    OutlinedButton(
                        onClick = onToggleAvailability,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            if (vehicle.isAvailable) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (vehicle.isAvailable) "Set Busy" else "Set Available")
                    }
                }

                if (onEdit != null) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Vehicle",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Vehicle",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VehicleTypeIcon(vehicleType: VehicleType) {
    val icon = when (vehicleType) {
        VehicleType.MOTOR -> Icons.Default.TwoWheeler
        VehicleType.MOBIL -> Icons.Default.DirectionsCar
        VehicleType.TRUK -> Icons.Default.LocalShipping
    }

    Icon(
        icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
private fun VehicleSpecItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}