package com.reloop.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface UserDao {
    @Insert suspend fun insert(user: User): Long
    @Update suspend fun update(user: User)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1") suspend fun findByEmail(email: String): User?
    @Query("SELECT * FROM users WHERE email = :email AND password = :password AND isActive = 1 LIMIT 1") suspend fun login(email: String, password: String): User?
    @Query("SELECT * FROM users WHERE userID = :userID LIMIT 1") suspend fun findById(userID: Int): User?
    @Query("SELECT * FROM users ORDER BY createdAt DESC") fun allUsers(): LiveData<List<User>>
    @Query("SELECT COUNT(*) FROM users WHERE role = 'user'") fun totalNormalUsers(): LiveData<Int>
}

@Dao
interface AgentDao {
    @Insert suspend fun insert(agent: Agent): Long
    @Query("SELECT * FROM agents WHERE isAvailable = 1 LIMIT 1") suspend fun firstAvailable(): Agent?
}

@Dao
interface PickupDao {
    @Insert suspend fun insert(pickup: Pickup): Long
    @Update suspend fun update(pickup: Pickup)
    @Query("SELECT * FROM pickups WHERE pickupID = :pickupID LIMIT 1") suspend fun findById(pickupID: Int): Pickup?
    @Query("SELECT * FROM pickups WHERE userID = :userID ORDER BY createdAt DESC") fun forUser(userID: Int): LiveData<List<Pickup>>
    @Query("SELECT * FROM pickups ORDER BY createdAt DESC") fun allPickups(): LiveData<List<Pickup>>
    @Query("SELECT * FROM pickups WHERE userID = :userID AND status IN ('pending','confirmed') ORDER BY scheduledDate ASC, scheduledTime ASC LIMIT 1") fun upcomingForUser(userID: Int): LiveData<Pickup?>
    @Query("SELECT COUNT(*) FROM pickups") fun totalPickups(): LiveData<Int>
    @Query("SELECT COUNT(*) FROM pickups WHERE status = :status") fun countByStatus(status: String): LiveData<Int>
    @Query("SELECT wasteType FROM pickups GROUP BY wasteType ORDER BY COUNT(*) DESC LIMIT 1") fun mostCommonWasteType(): LiveData<String?>
}

@Dao
interface RewardDao {
    @Insert suspend fun insert(reward: Reward): Long
    @Update suspend fun update(reward: Reward)
    @Query("SELECT * FROM rewards WHERE userID = :userID LIMIT 1") fun forUser(userID: Int): LiveData<Reward?>
    @Query("SELECT * FROM rewards WHERE userID = :userID LIMIT 1") suspend fun findForUser(userID: Int): Reward?
    @Query("SELECT SUM(totalEarned) FROM rewards") fun totalPointsAwarded(): LiveData<Int?>
}

@Dao
interface RewardItemDao {
    @Insert suspend fun insert(item: RewardItem): Long
    @Update suspend fun update(item: RewardItem)
    @Delete suspend fun delete(item: RewardItem)
    @Query("SELECT * FROM reward_items WHERE isActive = 1 ORDER BY pointsCost ASC") fun activeItems(): LiveData<List<RewardItem>>
    @Query("SELECT * FROM reward_items ORDER BY isActive DESC, pointsCost ASC") fun allItems(): LiveData<List<RewardItem>>
    @Query("SELECT * FROM reward_items WHERE rewardItemID = :itemID LIMIT 1") suspend fun findById(itemID: Int): RewardItem?
}

@Dao
interface RedemptionLogDao {
    @Insert suspend fun insert(log: RedemptionLog): Long
    @Query("SELECT * FROM redemption_logs WHERE userID = :userID ORDER BY redeemedAt DESC") fun forUser(userID: Int): LiveData<List<RedemptionLog>>
    @Query("SELECT COUNT(*) FROM redemption_logs") fun totalRedemptions(): LiveData<Int>
}

@Dao
interface WasteCategoryDao {
    @Insert suspend fun insert(category: WasteCategory): Long
    @Query("SELECT * FROM waste_categories ORDER BY categoryName ASC") fun allCategories(): LiveData<List<WasteCategory>>
    @Query("SELECT * FROM waste_categories ORDER BY categoryName ASC") suspend fun allCategoriesNow(): List<WasteCategory>
    @Query("SELECT * FROM waste_categories WHERE categoryName = :name LIMIT 1") suspend fun findByName(name: String): WasteCategory?
    @Query("SELECT * FROM waste_categories WHERE wasteCategoryID = :id LIMIT 1") suspend fun findById(id: Int): WasteCategory?
}

@Dao
interface WasteLogDao {
    @Insert suspend fun insert(log: WasteLog): Long
    @Query("SELECT SUM(weightKg) FROM waste_logs") fun totalKg(): LiveData<Double?>
}

@Dao
interface GuidanceDao {
    @Insert suspend fun insert(content: GuidanceContent): Long
    @Query("SELECT * FROM guidance_content ORDER BY title ASC") fun allGuidance(): LiveData<List<GuidanceContent>>
    @Query("SELECT * FROM guidance_content WHERE wasteCategoryID = :categoryID LIMIT 1") fun byCategory(categoryID: Int): LiveData<GuidanceContent?>
    @Query("SELECT * FROM guidance_content WHERE wasteCategoryID = :categoryID LIMIT 1") suspend fun byCategoryNow(categoryID: Int): GuidanceContent?
}

@Dao
interface NotificationDao {
    @Insert suspend fun insert(notification: Notification): Long
    @Update suspend fun update(notification: Notification)
    @Query("SELECT * FROM notifications WHERE userID = :userID ORDER BY sentAt DESC") fun forUser(userID: Int): LiveData<List<Notification>>
    @Query("SELECT COUNT(*) FROM notifications WHERE userID = :userID AND isRead = 0") fun unreadCount(userID: Int): LiveData<Int>
}
