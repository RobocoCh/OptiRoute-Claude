package com.optiroute.com.data.repository

import com.optiroute.com.data.local.dao.VehicleDao
import com.optiroute.com.data.local.entities.VehicleEntity
import com.optiroute.com.domain.models.Vehicle
import com.optiroute.com.domain.repository.VehicleRepository
import com.optiroute.com.utils.Resource
import com.optiroute.com.utils.generateId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleRepositoryImpl @Inject constructor(
    private val vehicleDao: VehicleDao
) : VehicleRepository {

    override suspend fun createVehicle(vehicle: Vehicle): Resource<Vehicle> {
        return try {
            val vehicleEntity = VehicleEntity.fromDomainModel(
                vehicle.copy(id = generateId())
            )
            vehicleDao.insertVehicle(vehicleEntity)
            Resource.Success(vehicleEntity.toDomainModel())
        } catch (e: Exception) {
            Resource.Error("Failed to create vehicle: ${e.message}")
        }
    }

    override suspend fun updateVehicle(vehicle: Vehicle): Resource<Vehicle> {
        return try {
            val vehicleEntity = VehicleEntity.fromDomainModel(vehicle)
            vehicleDao.updateVehicle(vehicleEntity)
            Resource.Success(vehicle)
        } catch (e: Exception) {
            Resource.Error("Failed to update vehicle: ${e.message}")
        }
    }

    override suspend fun deleteVehicle(vehicleId: String): Resource<Boolean> {
        return try {
            val vehicle = vehicleDao.getVehicleById(vehicleId)
            if (vehicle != null) {
                vehicleDao.deleteVehicle(vehicle)
                Resource.Success(true)
            } else {
                Resource.Error("Vehicle not found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to delete vehicle: ${e.message}")
        }
    }

    override suspend fun getVehicleById(vehicleId: String): Resource<Vehicle> {
        return try {
            val vehicle = vehicleDao.getVehicleById(vehicleId)
            if (vehicle != null) {
                Resource.Success(vehicle.toDomainModel())
            } else {
                Resource.Error("Vehicle not found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to get vehicle: ${e.message}")
        }
    }

    override fun getVehiclesByDepot(depotId: String): Flow<List<Vehicle>> {
        return vehicleDao.getVehiclesByDepot(depotId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getVehiclesByKurir(kurirId: String): Flow<List<Vehicle>> {
        return vehicleDao.getVehiclesByKurir(kurirId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getAvailableVehiclesByDepot(depotId: String): Flow<List<Vehicle>> {
        return vehicleDao.getAvailableVehiclesByDepot(depotId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun updateVehicleAvailability(vehicleId: String, isAvailable: Boolean): Resource<Boolean> {
        return try {
            vehicleDao.updateVehicleAvailability(vehicleId, isAvailable)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error("Failed to update vehicle availability: ${e.message}")
        }
    }
}