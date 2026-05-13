package com.reloop.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reloop.app.data.dao.AgentDao
import com.reloop.app.data.dao.GuidanceDao
import com.reloop.app.data.dao.NotificationDao
import com.reloop.app.data.dao.PickupDao
import com.reloop.app.data.dao.RedemptionLogDao
import com.reloop.app.data.dao.RewardDao
import com.reloop.app.data.dao.RewardItemDao
import com.reloop.app.data.dao.UserDao
import com.reloop.app.data.dao.WasteCategoryDao
import com.reloop.app.data.dao.WasteLogDao
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

@Database(
    entities = [
        User::class, Agent::class, Pickup::class, Reward::class, RewardItem::class,
        RedemptionLog::class, WasteCategory::class, WasteLog::class,
        GuidanceContent::class, Notification::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun agentDao(): AgentDao
    abstract fun pickupDao(): PickupDao
    abstract fun rewardDao(): RewardDao
    abstract fun rewardItemDao(): RewardItemDao
    abstract fun redemptionLogDao(): RedemptionLogDao
    abstract fun wasteCategoryDao(): WasteCategoryDao
    abstract fun wasteLogDao(): WasteLogDao
    abstract fun guidanceDao(): GuidanceDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        // Singleton database instance prevents opening multiple Room connections.
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reloop_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
