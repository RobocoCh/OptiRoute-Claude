package com.optiroute.com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optiroute.com.domain.models.*
import com.optiroute.com.domain.usecase.depot.CreateDepotUseCase
import com.optiroute.com.domain.usecase.depot.GetDepotsByAdminUseCase
import com.optiroute.com.domain.usecase.vehicle.CreateVehicleUseCase
import com.optiroute.com.domain.usecase.vehicle.GetAvailableVehiclesByDepotUseCase
import com.optiroute.com.domain.usecase.auth.GetCurrentUserUseCase
import com.optiroute.com.utils.Resource
import com.optiroute.com.utils.generateId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val createDepotUseCase: CreateDepotUseCase,
    private val getDepotsByAdminUseCase: GetDepotsByAdminUseCase,
    private val createVehicleUseCase: CreateVehicleUseCase,
    private val getAvailableVehiclesByDepotUseCase: GetAvailableVehiclesByDepotUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _adminState = MutableStateFlow<AdminState>(AdminState.Initial)
    val adminState: StateFlow<AdminState> = _adminState.asStateFlow()

    private val _depots = MutableStateFlow<List<Depot>>(emptyList())
    val depots: StateFlow<List<Depot>> = _depots.asStateFlow()

    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles.asStateFlow()

    private val _depotFormState = MutableStateFlow(DepotFormState())
    val depotFormState: StateFlow<DepotFormState> = _depotFormState.asStateFlow()

    private val _vehicleFormState = MutableStateFlow(VehicleFormState())
    val vehicleFormState: StateFlow<VehicleFormState> = _vehicleFormState.asStateFlow()

    fun loadDepots() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
            if (currentUser != null) {
                getDepotsByAdminUseCase(currentUser.id).collect { depotList ->
                    _depots.value = depotList
                }
            }
        }
    }

    fun loadVehicles(depotId: String) {
        viewModelScope.launch {
            getAvailableVehiclesByDepotUseCase(depotId).collect { vehicleList ->
                _vehicles.value = vehicleList
            }
        }
    }

    fun createDepot(
        name: String,
        location: Location,
        capacity: Double,
        operationalHours: String
    ) {
        viewModelScope.launch {
            _adminState.value = AdminState.Loading

            val currentUser = getCurrentUserUseCase()
            if (currentUser == null) {
                _adminState.value = AdminState.Error("User not found")
                return@launch
            }

            val depot = Depot(
                id = generateId(),
                name = name,
                location = location,
                capacity = capacity,
                operationalHours = operationalHours,
                adminId = currentUser.id
            )

            when (val result = createDepotUseCase(depot)) {
                is Resource.Success -> {
                    _adminState.value = AdminState.Success("Depot created successfully")
                    clearDepotForm()
                }
                is Resource.Error -> {
                    _adminState.value = AdminState.Error(result.message ?: "Failed to create depot")
                }
                is Resource.Loading -> {
                    _adminState.value = AdminState.Loading
                }
            }
        }
    }

    fun createVehicle(
        licensePlate: String,
        vehicleType: VehicleType,
        capacity: Double,
        capacityUnit: String,
        fuelConsumption: Double,
        depotId: String,
        kurirId: String
    ) {
        viewModelScope.launch {
            _adminState.value = AdminState.Loading

            val vehicle = Vehicle(
                id = generateId(),
                licensePlate = licensePlate,
                vehicleType = vehicleType,
                capacity = capacity,
                capacityUnit = capacityUnit,
                fuelConsumption = fuelConsumption,
                depotId = depotId,
                kurirId = kurirId
            )

            when (val result = createVehicleUseCase(vehicle)) {
                is Resource.Success -> {
                    _adminState.value = AdminState.Success("Vehicle created successfully")
                    clearVehicleForm()
                }
                is Resource.Error -> {
                    _adminState.value = AdminState.Error(result.message ?: "Failed to create vehicle")
                }
                is Resource.Loading -> {
                    _adminState.value = AdminState.Loading
                }
            }
        }
    }

    fun updateDepotForm(formState: DepotFormState) {
        _depotFormState.value = formState
    }

    fun updateVehicleForm(formState: VehicleFormState) {
        _vehicleFormState.value = formState
    }

    fun clearDepotForm() {
        _depotFormState.value = DepotFormState()
    }

    fun clearVehicleForm() {
        _vehicleFormState.value = VehicleFormState()
    }

    fun clearAdminState() {
        _adminState.value = AdminState.Initial
    }

    sealed class AdminState {
        object Initial : AdminState()
        object Loading : AdminState()
        data class Success(val message: String) : AdminState()
        data class Error(val message: String) : AdminState()
    }

    data class DepotFormState(
        val name: String = "",
        val address: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val capacity: String = "",
        val operationalHours: String = ""
    )

    data class VehicleFormState(
        val licensePlate: String = "",
        val vehicleType: VehicleType = VehicleType.MOTOR,
        val capacity: String = "",
        val capacityUnit: String = "kg",
        val fuelConsumption: String = "",
        val selectedDepotId: String = "",
        val selectedKurirId: String = ""
    )
}