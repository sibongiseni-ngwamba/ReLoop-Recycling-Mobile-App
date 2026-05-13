package com.reloop.app.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.reloop.app.data.database.AppDatabase
import com.reloop.app.data.entities.Agent
import com.reloop.app.data.entities.GuidanceContent
import com.reloop.app.data.entities.Notification
import com.reloop.app.data.entities.Pickup
import com.reloop.app.data.entities.RedemptionLog
import com.reloop.app.data.entities.Reward
import com.reloop.app.data.entities.RewardItem
import com.reloop.app.data.entities.User
import com.reloop.app.data.entities.WasteCategory
import com.reloop.app.data.entities.WasteLog
import kotlin.math.max
import kotlin.random.Random

class ReLoopRepository private constructor(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val prefs = context.getSharedPreferences("reloop_seed", Context.MODE_PRIVATE)

    val allUsers: LiveData<List<User>> = db.userDao().allUsers()
    val allPickups: LiveData<List<Pickup>> = db.pickupDao().allPickups()
    val activeRewardItems: LiveData<List<RewardItem>> = db.rewardItemDao().activeItems()
    val allRewardItems: LiveData<List<RewardItem>> = db.rewardItemDao().allItems()
    val allCategories: LiveData<List<WasteCategory>> = db.wasteCategoryDao().allCategories()
    val allGuidance: LiveData<List<GuidanceContent>> = db.guidanceDao().allGuidance()
    val totalUsers: LiveData<Int> = db.userDao().totalNormalUsers()
    val totalPickups: LiveData<Int> = db.pickupDao().totalPickups()
    val pendingPickups: LiveData<Int> = db.pickupDao().countByStatus("pending")
    val completedPickups: LiveData<Int> = db.pickupDao().countByStatus("completed")
    val totalRedemptions: LiveData<Int> = db.redemptionLogDao().totalRedemptions()
    val totalKgRecycled: LiveData<Double?> = db.wasteLogDao().totalKg()
    val totalPointsAwarded: LiveData<Int?> = db.rewardDao().totalPointsAwarded()
    val mostCommonWasteType: LiveData<String?> = db.pickupDao().mostCommonWasteType()

    // Seed data gives the app realistic local content without Firebase or an online server.
    suspend fun seedInitialData() {
        if (prefs.getBoolean("seeded", false)) return
        if (db.userDao().findByEmail("admin@reloop.co.za") != null) {
            prefs.edit().putBoolean("seeded", true).apply()
            return
        }

        val adminId = db.userDao().insert(
            User(
                firstName = "ReLoop",
                lastName = "Admin",
                email = "admin@reloop.co.za",
                password = "Admin@123",
                phoneNumber = "0210000000",
                address = "Cape Town Operations Centre",
                role = "admin"
            )
        ).toInt()
        db.rewardDao().insert(Reward(userID = adminId))

        val userId = db.userDao().insert(
            User(
                firstName = "Sample",
                lastName = "User",
                email = "user@reloop.co.za",
                password = "User@123",
                phoneNumber = "0821234567",
                address = "12 Green Street, Cape Town",
                role = "user"
            )
        ).toInt()
        db.rewardDao().insert(Reward(userID = userId, pointsBalance = 120, totalEarned = 120))

        listOf(
            RewardItem(itemName = "R50 Grocery Voucher", description = "Use at participating local stores.", pointsCost = 100),
            RewardItem(itemName = "Reusable Bottle", description = "Stainless steel bottle from ReLoop.", pointsCost = 150),
            RewardItem(itemName = "R100 Electricity Voucher", description = "Prepaid electricity reward voucher.", pointsCost = 220)
        ).forEach { db.rewardItemDao().insert(it) }

        val categories = listOf(
            WasteCategory(categoryName = "Plastic PET", description = "Clear plastic bottles and PET containers.", modelLabel = "plastic_pet"),
            WasteCategory(categoryName = "Glass", description = "Glass bottles and jars.", modelLabel = "glass"),
            WasteCategory(categoryName = "Paper/Cardboard", description = "Office paper, newspaper and flattened cardboard.", modelLabel = "paper_cardboard"),
            WasteCategory(categoryName = "Metal", description = "Aluminium cans and clean metal containers.", modelLabel = "metal"),
            WasteCategory(categoryName = "E-Waste", description = "Small electronics, cables and batteries.", modelLabel = "e_waste")
        )
        val categoryIds = categories.map { db.wasteCategoryDao().insert(it).toInt() }

        val guidance = listOf(
            GuidanceContent(wasteCategoryID = categoryIds[0], title = "Plastic PET", content = "Rinse bottles, crush them to save space, and place them in the recycling bin.", imageName = "plastic_pet"),
            GuidanceContent(wasteCategoryID = categoryIds[1], title = "Glass", content = "Rinse bottles and jars. Do not break glass before collection because sharp pieces are unsafe.", imageName = "glass"),
            GuidanceContent(wasteCategoryID = categoryIds[2], title = "Paper/Cardboard", content = "Keep paper dry, remove food residue, and flatten cardboard boxes.", imageName = "paper"),
            GuidanceContent(wasteCategoryID = categoryIds[3], title = "Metal", content = "Rinse cans, squash them if possible, and separate aerosols from normal metal waste.", imageName = "metal"),
            GuidanceContent(wasteCategoryID = categoryIds[4], title = "E-Waste", content = "Take electronics and batteries to an authorised collection point or schedule a special pickup.", imageName = "ewaste")
        )
        guidance.forEach { db.guidanceDao().insert(it) }

        listOf(
            Agent(name = "Lebo Mokoena", contactNumber = "0715551001", assignedZone = "Cape Town CBD"),
            Agent(name = "Ayesha Jacobs", contactNumber = "0725551002", assignedZone = "Southern Suburbs")
        ).forEach { db.agentDao().insert(it) }

        db.notificationDao().insert(
            Notification(userID = userId, message = "Welcome to ReLoop Technologies SA. You have 120 starter points.", type = "welcome")
        )
        prefs.edit().putBoolean("seeded", true).apply()
    }

    suspend fun registerUser(user: User): Result<Unit> {
        if (db.userDao().findByEmail(user.email) != null) return Result.failure(Exception("Email already exists"))
        val id = db.userDao().insert(user).toInt()
        db.rewardDao().insert(Reward(userID = id))
        return Result.success(Unit)
    }

    suspend fun login(email: String, password: String): User? = db.userDao().login(email, password)
    suspend fun userById(userID: Int): User? = db.userDao().findById(userID)
    suspend fun updateUser(user: User) = db.userDao().update(user)

    fun rewardForUser(userID: Int): LiveData<Reward?> = db.rewardDao().forUser(userID)
    fun pickupsForUser(userID: Int): LiveData<List<Pickup>> = db.pickupDao().forUser(userID)
    fun upcomingPickup(userID: Int): LiveData<Pickup?> = db.pickupDao().upcomingForUser(userID)
    fun notificationsForUser(userID: Int): LiveData<List<Notification>> = db.notificationDao().forUser(userID)
    fun unreadNotifications(userID: Int): LiveData<Int> = db.notificationDao().unreadCount(userID)
    fun redemptionsForUser(userID: Int): LiveData<List<RedemptionLog>> = db.redemptionLogDao().forUser(userID)
    fun guidanceByCategory(categoryID: Int): LiveData<GuidanceContent?> = db.guidanceDao().byCategory(categoryID)

    suspend fun schedulePickup(pickup: Pickup) {
        db.pickupDao().insert(pickup)
        // Local notification record is displayed inside NotificationsActivity.
        db.notificationDao().insert(
            Notification(
                userID = pickup.userID,
                message = "Pickup scheduled for ${pickup.scheduledDate} at ${pickup.scheduledTime}.",
                type = "pickup"
            )
        )
    }

    suspend fun redeemReward(userID: Int, rewardItemID: Int): Result<String> {
        val reward = db.rewardDao().findForUser(userID) ?: return Result.failure(Exception("Rewards account not found"))
        val item = db.rewardItemDao().findById(rewardItemID) ?: return Result.failure(Exception("Reward item not found"))
        if (!item.isActive) return Result.failure(Exception("Reward is inactive"))
        if (reward.pointsBalance < item.pointsCost) return Result.failure(Exception("Not enough points"))

        // Voucher codes are simulated locally for the college project.
        val voucher = "RL-${System.currentTimeMillis().toString().takeLast(6)}"
        db.rewardDao().update(
            reward.copy(
                pointsBalance = max(0, reward.pointsBalance - item.pointsCost),
                totalRedeemed = reward.totalRedeemed + item.pointsCost,
                lastUpdated = System.currentTimeMillis()
            )
        )
        db.redemptionLogDao().insert(RedemptionLog(userID = userID, rewardItemID = rewardItemID, pointsUsed = item.pointsCost, voucherCode = voucher))
        db.notificationDao().insert(Notification(userID = userID, message = "Reward redeemed: ${item.itemName}. Voucher: $voucher", type = "reward"))
        return Result.success(voucher)
    }

    suspend fun classifyWaste(userID: Int, preferredName: String? = null): Pair<WasteCategory, Int> {
        val categories = db.wasteCategoryDao().allCategoriesNow()
        val category = preferredName?.let { db.wasteCategoryDao().findByName(it) }
            ?: categories.randomOrNull()
            ?: WasteCategory(categoryName = "Plastic PET", description = "Plastic bottle", modelLabel = "plastic_pet")
        db.wasteLogDao().insert(WasteLog(userID = userID, wasteCategoryID = category.wasteCategoryID, weightKg = Random.nextDouble(0.2, 3.5)))
        return category to Random.nextInt(76, 98)
    }

    suspend fun markNotificationRead(notification: Notification) = db.notificationDao().update(notification.copy(isRead = true))

    suspend fun updatePickupStatus(pickup: Pickup, newStatus: String) {
        val wasCompleted = pickup.status == "completed"
        val updated = pickup.copy(status = newStatus)
        db.pickupDao().update(updated)
        if (newStatus == "completed" && !wasCompleted) {
            awardPointsForPickup(updated)
        } else {
            db.notificationDao().insert(Notification(userID = pickup.userID, message = "Pickup #${pickup.pickupID} status updated to $newStatus.", type = "pickup"))
        }
    }

    private suspend fun awardPointsForPickup(pickup: Pickup) {
        // Completed pickups earn different points based on likely recycling value.
        val points = when (pickup.wasteType) {
            "E-Waste" -> 80
            "Mixed" -> 60
            else -> 50
        }
        val reward = db.rewardDao().findForUser(pickup.userID) ?: Reward(userID = pickup.userID)
        if (reward.rewardID == 0) {
            db.rewardDao().insert(reward.copy(pointsBalance = points, totalEarned = points))
        } else {
            db.rewardDao().update(reward.copy(pointsBalance = reward.pointsBalance + points, totalEarned = reward.totalEarned + points, lastUpdated = System.currentTimeMillis()))
        }
        val category = db.wasteCategoryDao().findByName(if (pickup.wasteType == "Paper") "Paper/Cardboard" else pickup.wasteType)
            ?: db.wasteCategoryDao().allCategoriesNow().firstOrNull()
        if (category != null) {
            db.wasteLogDao().insert(WasteLog(userID = pickup.userID, pickupID = pickup.pickupID, wasteCategoryID = category.wasteCategoryID, weightKg = Random.nextDouble(1.0, 7.0)))
        }
        db.notificationDao().insert(Notification(userID = pickup.userID, message = "Pickup completed. You earned $points reward points.", type = "reward"))
    }

    suspend fun saveRewardItem(item: RewardItem) {
        if (item.rewardItemID == 0) db.rewardItemDao().insert(item) else db.rewardItemDao().update(item)
    }

    companion object {
        @Volatile private var INSTANCE: ReLoopRepository? = null
        fun get(context: Context): ReLoopRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ReLoopRepository(context.applicationContext).also { INSTANCE = it }
        }
    }
}
