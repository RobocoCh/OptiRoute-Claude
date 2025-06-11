package com.optiroute.com.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.optiroute.com.domain.models.CustomerPriority
import com.optiroute.com.domain.models.TaskAssignment
import com.optiroute.com.domain.models.TaskAssignmentStatus

@Entity(tableName = "task_assignments")
data class TaskAssignmentEntity(
    @PrimaryKey
    val id: String,
    val customerId: String,
    val umkmId: String,
    val adminId: String,
    val kurirId: String,
    val status: String,
    val assignedAt: Long,
    val acceptedAt: Long,
    val rejectedAt: Long,
    val rejectionReason: String,
    val notes: String,
    val priority: String,
    val estimatedDeliveryTime: Long,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toDomainModel(): TaskAssignment {
        return TaskAssignment(
            id = id,
            customerId = customerId,
            umkmId = umkmId,
            adminId = adminId,
            kurirId = kurirId,
            status = TaskAssignmentStatus.valueOf(status),
            assignedAt = assignedAt,
            acceptedAt = acceptedAt,
            rejectedAt = rejectedAt,
            rejectionReason = rejectionReason,
            notes = notes,
            priority = CustomerPriority.valueOf(priority),
            estimatedDeliveryTime = estimatedDeliveryTime,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromDomainModel(taskAssignment: TaskAssignment): TaskAssignmentEntity {
            return TaskAssignmentEntity(
                id = taskAssignment.id,
                customerId = taskAssignment.customerId,
                umkmId = taskAssignment.umkmId,
                adminId = taskAssignment.adminId,
                kurirId = taskAssignment.kurirId,
                status = taskAssignment.status.name,
                assignedAt = taskAssignment.assignedAt,
                acceptedAt = taskAssignment.acceptedAt,
                rejectedAt = taskAssignment.rejectedAt,
                rejectionReason = taskAssignment.rejectionReason,
                notes = taskAssignment.notes,
                priority = taskAssignment.priority.name,
                estimatedDeliveryTime = taskAssignment.estimatedDeliveryTime,
                createdAt = taskAssignment.createdAt,
                updatedAt = taskAssignment.updatedAt
            )
        }
    }
}

@Entity(tableName = "task_offers")
data class TaskOfferEntity(
    @PrimaryKey
    val id: String,
    val taskAssignmentId: String,
    val kurirId: String,
    val offeredAt: Long,
    val expiresAt: Long,
    val status: String,
    val notes: String
) {
    fun toDomainModel(): com.optiroute.com.domain.models.TaskOffer {
        return com.optiroute.com.domain.models.TaskOffer(
            id = id,
            taskAssignmentId = taskAssignmentId,
            kurirId = kurirId,
            offeredAt = offeredAt,
            expiresAt = expiresAt,
            status = com.optiroute.com.domain.models.TaskOfferStatus.valueOf(status),
            notes = notes
        )
    }

    companion object {
        fun fromDomainModel(taskOffer: com.optiroute.com.domain.models.TaskOffer): TaskOfferEntity {
            return TaskOfferEntity(
                id = taskOffer.id,
                taskAssignmentId = taskOffer.taskAssignmentId,
                kurirId = taskOffer.kurirId,
                offeredAt = taskOffer.offeredAt,
                expiresAt = taskOffer.expiresAt,
                status = taskOffer.status.name,
                notes = taskOffer.notes
            )
        }
    }
}