package com.reloop.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userID: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val address: String,
    val role: String = "user",
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

@Entity(tableName = "agents")
data class Agent(
    @PrimaryKey(autoGenerate = true) val agentID: Int = 0,
    val name: String,
    val contactNumber: String,
    val assignedZone: String,
    val isAvailable: Boolean = true
)

@Entity(tableName = "pickups")
data class Pickup(
    @PrimaryKey(autoGenerate = true) val pickupID: Int = 0,
    val userID: Int,
    val agentID: Int? = null,
    val scheduledDate: String,
    val scheduledTime: String,
    val wasteType: String,
    val address: String,
    val status: String = "pending",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey(autoGenerate = true) val rewardID: Int = 0,
    val userID: Int,
    val pointsBalance: Int = 0,
    val totalEarned: Int = 0,
    val totalRedeemed: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "reward_items")
data class RewardItem(
    @PrimaryKey(autoGenerate = true) val rewardItemID: Int = 0,
    val itemName: String,
    val description: String,
    val pointsCost: Int,
    val isActive: Boolean = true
)

@Entity(tableName = "redemption_logs")
data class RedemptionLog(
    @PrimaryKey(autoGenerate = true) val redemptionID: Int = 0,
    val userID: Int,
    val rewardItemID: Int,
    val pointsUsed: Int,
    val voucherCode: String,
    val redeemedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "waste_categories")
data class WasteCategory(
    @PrimaryKey(autoGenerate = true) val wasteCategoryID: Int = 0,
    val categoryName: String,
    val description: String,
    val modelLabel: String
)

@Entity(tableName = "waste_logs")
data class WasteLog(
    @PrimaryKey(autoGenerate = true) val wasteLogID: Int = 0,
    val userID: Int,
    val pickupID: Int? = null,
    val wasteCategoryID: Int,
    val weightKg: Double,
    val loggedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "guidance_content")
data class GuidanceContent(
    @PrimaryKey(autoGenerate = true) val guidanceID: Int = 0,
    val wasteCategoryID: Int,
    val title: String,
    val content: String,
    val imageName: String = ""
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val notificationID: Int = 0,
    val userID: Int,
    val message: String,
    val type: String,
    val sentAt: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
