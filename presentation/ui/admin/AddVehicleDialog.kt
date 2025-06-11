package com.optiroute.com.presentation.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.optiroute.com.domain.models.Depot
import com.optiroute.com.domain.models.VehicleType

data class VehicleFormData(
    val licensePlate: String,
    val vehicleType: VehicleType,
    val capacity: Double,
    val capacityUnit: String,
    val fuelConsumption: Double,
    val depotId: String,
    val kurirId: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleDialog(
    depots: List<Depot>,
    onDismiss: () -> Unit,
    onConfirm: (VehicleFormData) -> Unit,
    isLoading: Boolean = false,
    error: String? = null
) {
    var licensePlate by remember { mutableStateOf("") }
    var selectedVehicleType by remember { mutableStateOf(VehicleType.MOTOR) }
    var capacity by remember { mutableStateOf("") }
    var capacityUnit by remember { mutableStateOf("kg") }
    var fuelConsumption by remember { mutableStateOf("") }
    var selectedDepotId by remember { mutableStateOf("") }
    var kurirId by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    // Set default depot if available
    LaunchedEffect(depots) {
        if (depots.isNotEmpty() && selectedDepotId.isEmpty()) {
            selectedDepotId = depots.first().id
        }
    }

    Dialog(onDismissRequest = { if (!isLoading) onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Add New Vehicle",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Vehicle Information Section
                    Text(
                        text = "Vehicle Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = licensePlate,
                        onValueChange = { licensePlate = it.uppercase() },
                        label = { Text("License Plate") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("e.g., B 1234 CD") }
                    )

                    // Vehicle Type Selection
                    Text(
                        text = "Vehicle Type",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )

                    VehicleType.values().forEach { vehicleType ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = selectedVehicleType == vehicleType,
                                    onClick = { selectedVehicleType = vehicleType },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedVehicleType == vehicleType,
                                onClick = { selectedVehicleType = vehicleType }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            VehicleTypeIcon(vehicleType = vehicleType)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = vehicleType.name.replace("_", " "),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = when (vehicleType) {
                                        VehicleType.MOTOR -> "Motorcycle - Small deliveries"
                                        VehicleType.MOBIL -> "Car - Medium capacity"
                                        VehicleType.TRUK -> "Truck - Large capacity"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Divider()

                    // Specifications Section
                    Text(
                        text = "Vehicle Specifications",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        OutlinedTextField(
                            value = capacity,
                            onValueChange = { capacity = it },
                            label = { Text("Capacity") },
                            leadingIcon = { Icon(Icons.Default.Scale, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(2f),
                            singleLine = true
                        )

                        var capacityExpanded by remember { mutableStateOf(false) }
                        val capacityUnits = listOf("kg", "ton", "liter", "m³")

                        ExposedDropdownMenuBox(
                            expanded = capacityExpanded,
                            onExpandedChange = { capacityExpanded = !capacityExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = capacityUnit,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Unit") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = capacityExpanded) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = capacityExpanded,
                                onDismissRequest = { capacityExpanded = false }
                            ) {
                                capacityUnits.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit) },
                                        onClick = {
                                            capacityUnit = unit
                                            capacityExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = fuelConsumption,
                        onValueChange = { fuelConsumption = it },
                        label = { Text("Fuel Consumption (L/100km)") },
                        leadingIcon = { Icon(Icons.Default.LocalGasStation, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("e.g., 8.5") }
                    )

                    Divider()

                    // Assignment Section
                    Text(
                        text = "Assignment",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Depot Selection
                    var depotExpanded by remember { mutableStateOf(false) }
                    val selectedDepot = depots.find { it.id == selectedDepotId }

                    ExposedDropdownMenuBox(
                        expanded = depotExpanded,
                        onExpandedChange = { depotExpanded = !depotExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedDepot?.name ?: "Select Depot",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Assign to Depot") },
                            leadingIcon = { Icon(Icons.Default.Warehouse, contentDescription = null) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = depotExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = depotExpanded,
                            onDismissRequest = { depotExpanded = false }
                        ) {
                            depots.forEach { depot ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(depot.name)
                                            Text(
                                                text = depot.location.address,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedDepotId = depot.id
                                        depotExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = kurirId,
                        onValueChange = { kurirId = it },
                        label = { Text("Kurir ID (Optional)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Leave empty to assign later") }
                    )

                    if (error != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val cap = capacity.toDoubleOrNull() ?: 0.0
                            val fuel = fuelConsumption.toDoubleOrNull() ?: 0.0

                            val vehicleData = VehicleFormData(
                                licensePlate = licensePlate,
                                vehicleType = selectedVehicleType,
                                capacity = cap,
                                capacityUnit = capacityUnit,
                                fuelConsumption = fuel,
                                depotId = selectedDepotId,
                                kurirId = kurirId
                            )
                            onConfirm(vehicleData)
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading &&
                                licensePlate.isNotBlank() &&
                                capacity.isNotBlank() &&
                                fuelConsumption.isNotBlank() &&
                                selectedDepotId.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Add Vehicle")
                        }
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
        modifier = Modifier.size(20.dp)
    )
}