package com.optiroute.com.domain.repository

import com.optiroute.com.domain.models.Vehicle
import com.optiroute.com.utils.Resource
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    suspend fun createVehicle(vehicle: Vehicle): Resource<Vehicle>
    suspend fun updateVehicle(vehicle: Vehicle): Resource<Vehicle>
    suspend fun deleteVehicle(vehicleId: String): Resource<Boolean>
    suspend fun getVehicleById(vehicleId: String): Resource<Vehicle>
    fun getVehiclesByDepot(depotId: String): Flow<List<Vehicle>>
    fun getVehiclesByKurir(kurirId: String): Flow<List<Vehicle>>
    fun getAvailableVehiclesByDepot(depotId: String): Flow<List<Vehicle>>
    suspend fun updateVehicleAvailability(vehicleId: String, isAvailable: Boolean): Resource<Boolean>
}