package com.optiroute.com.data.local.dao

import androidx.room.*
import com.optiroute.com.data.local.entities.TaskAssignmentEntity
import com.optiroute.com.data.local.entities.TaskOfferEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskAssignmentDao {

    @Query("SELECT * FROM task_assignments WHERE id = :id")
    suspend fun getTaskAssignmentById(id: String): TaskAssignmentEntity?

    @Query("SELECT * FROM task_assignments WHERE umkmId = :umkmId ORDER BY createdAt DESC")
    fun getTaskAssignmentsByUmkm(umkmId: String): Flow<List<TaskAssignmentEntity>>

    @Query("SELECT * FROM task_assignments WHERE adminId = :adminId ORDER BY createdAt DESC")
    fun getTaskAssignmentsByAdmin(adminId: String): Flow<List<TaskAssignmentEntity>>

    @Query("SELECT * FROM task_assignments WHERE kurirId = :kurirId ORDER BY createdAt DESC")
    fun getTaskAssignmentsByKurir(kurirId: String): Flow<List<TaskAssignmentEntity>>

    @Query("SELECT * FROM task_assignments WHERE status = :status ORDER BY createdAt DESC")
    fun getTaskAssignmentsByStatus(status: String): Flow<List<TaskAssignmentEntity>>

    @Query("SELECT * FROM task_assignments WHERE status = 'PENDING_ADMIN_APPROVAL' ORDER BY createdAt DESC")
    fun getPendingApprovalTasks(): Flow<List<TaskAssignmentEntity>>

    @Query("SELECT * FROM task_assignments WHERE status = 'PENDING_KURIR_ACCEPTANCE' ORDER BY createdAt DESC")
    fun getAvailableTasksForKurir(): Flow<List<TaskAssignmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskAssignment(taskAssignment: TaskAssignmentEntity)

    @Update
    suspend fun updateTaskAssignment(taskAssignment: TaskAssignmentEntity)

    @Delete
    suspend fun deleteTaskAssignment(taskAssignment: TaskAssignmentEntity)

    @Query("UPDATE task_assignments SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateTaskStatus(id: String, status: String, updatedAt: Long)

    @Query("UPDATE task_assignments SET kurirId = :kurirId, status = :status, acceptedAt = :acceptedAt, updatedAt = :updatedAt WHERE id = :id")
    suspend fun acceptTask(id: String, kurirId: String, status: String, acceptedAt: Long, updatedAt: Long)

    @Query("UPDATE task_assignments SET status = :status, rejectedAt = :rejectedAt, rejectionReason = :reason, updatedAt = :updatedAt WHERE id = :id")
    suspend fun rejectTask(id: String, status: String, rejectedAt: Long, reason: String, updatedAt: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskOffer(taskOffer: TaskOfferEntity)

    @Query("SELECT * FROM task_offers WHERE kurirId = :kurirId AND status = 'PENDING' AND expiresAt > :currentTime ORDER BY offeredAt DESC")
    fun getActiveTaskOffersForKurir(kurirId: String, currentTime: Long): Flow<List<TaskOfferEntity>>

    @Query("UPDATE task_offers SET status = 'EXPIRED' WHERE expiresAt <= :currentTime AND status = 'PENDING'")
    suspend fun expireOldTaskOffers(currentTime: Long)
}