package com.optiroute.com.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskAssignment(
    val id: String = "",
    val customerId: String = "",
    val umkmId: String = "",
    val adminId: String = "",
    val kurirId: String = "",
    val status: TaskAssignmentStatus = TaskAssignmentStatus.PENDING_ADMIN_APPROVAL,
    val assignedAt: Long = System.currentTimeMillis(),
    val acceptedAt: Long = 0L,
    val rejectedAt: Long = 0L,
    val rejectionReason: String = "",
    val notes: String = "",
    val priority: CustomerPriority = CustomerPriority.NORMAL,
    val estimatedDeliveryTime: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class TaskAssignmentStatus {
    PENDING_ADMIN_APPROVAL,    // Menunggu approval admin
    PENDING_KURIR_ACCEPTANCE,  // Menunggu kurir menerima
    ACCEPTED_BY_KURIR,         // Diterima kurir
    REJECTED_BY_KURIR,         // Ditolak kurir
    REJECTED_BY_ADMIN,         // Ditolak admin
    CANCELLED,                 // Dibatalkan
    COMPLETED                  // Selesai
}

@Parcelize
data class TaskOffer(
    val id: String = "",
    val taskAssignmentId: String = "",
    val kurirId: String = "",
    val offeredAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000), // 24 jam
    val status: TaskOfferStatus = TaskOfferStatus.PENDING,
    val notes: String = ""
) : Parcelable

enum class TaskOfferStatus {
    PENDING, ACCEPTED, REJECTED, EXPIRED
}