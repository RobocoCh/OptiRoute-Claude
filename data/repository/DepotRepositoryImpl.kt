package com.optiroute.com.data.repository

import com.optiroute.com.data.local.dao.DepotDao
import com.optiroute.com.data.local.entities.DepotEntity
import com.optiroute.com.domain.models.Depot
import com.optiroute.com.domain.repository.DepotRepository
import com.optiroute.com.utils.Resource
import com.optiroute.com.utils.generateId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepotRepositoryImpl @Inject constructor(
    private val depotDao: DepotDao
) : DepotRepository {

    override suspend fun createDepot(depot: Depot): Resource<Depot> {
        return try {
            val depotEntity = DepotEntity.fromDomainModel(
                depot.copy(id = generateId())
            )
            depotDao.insertDepot(depotEntity)
            Resource.Success(depotEntity.toDomainModel())
        } catch (e: Exception) {
            Resource.Error("Failed to create depot: ${e.message}")
        }
    }

    override suspend fun updateDepot(depot: Depot): Resource<Depot> {
        return try {
            val depotEntity = DepotEntity.fromDomainModel(depot)
            depotDao.updateDepot(depotEntity)
            Resource.Success(depot)
        } catch (e: Exception) {
            Resource.Error("Failed to update depot: ${e.message}")
        }
    }

    override suspend fun deleteDepot(depotId: String): Resource<Boolean> {
        return try {
            val depot = depotDao.getDepotById(depotId)
            if (depot != null) {
                depotDao.deleteDepot(depot)
                Resource.Success(true)
            } else {
                Resource.Error("Depot not found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to delete depot: ${e.message}")
        }
    }

    override suspend fun getDepotById(depotId: String): Resource<Depot> {
        return try {
            val depot = depotDao.getDepotById(depotId)
            if (depot != null) {
                Resource.Success(depot.toDomainModel())
            } else {
                Resource.Error("Depot not found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to get depot: ${e.message}")
        }
    }

    override fun getDepotsByAdmin(adminId: String): Flow<List<Depot>> {
        return depotDao.getDepotsByAdmin(adminId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getAllActiveDepots(): Flow<List<Depot>> {
        return depotDao.getAllActiveDepots().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun updateDepotStatus(depotId: String, isActive: Boolean): Resource<Boolean> {
        return try {
            depotDao.updateDepotStatus(depotId, isActive)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error("Failed to update depot status: ${e.message}")
        }
    }
}