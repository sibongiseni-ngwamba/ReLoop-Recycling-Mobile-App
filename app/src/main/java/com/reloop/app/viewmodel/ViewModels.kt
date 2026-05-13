package com.reloop.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.reloop.app.data.entities.Notification
import com.reloop.app.data.entities.Pickup
import com.reloop.app.data.entities.RewardItem
import com.reloop.app.data.entities.User
import com.reloop.app.data.repository.ReLoopRepository
import kotlinx.coroutines.launch

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val repo: ReLoopRepository = ReLoopRepository.get(application)
    val message = MutableLiveData<String>()
}

class AuthViewModel(application: Application) : BaseViewModel(application) {
    val loginResult = MutableLiveData<User?>()

    fun seed() = viewModelScope.launch { repo.seedInitialData() }

    fun login(email: String, password: String) = viewModelScope.launch {
        loginResult.value = repo.login(email.trim(), password)
    }

    fun register(user: User, onDone: () -> Unit) = viewModelScope.launch {
        val result = repo.registerUser(user)
        if (result.isSuccess) onDone() else message.value = result.exceptionOrNull()?.message
    }
}

class DashboardViewModel(application: Application) : BaseViewModel(application) {
    fun reward(userID: Int) = repo.rewardForUser(userID)
    fun upcoming(userID: Int) = repo.upcomingPickup(userID)
    fun unread(userID: Int) = repo.unreadNotifications(userID)
    fun loadUser(userID: Int, callback: (User?) -> Unit) = viewModelScope.launch { callback(repo.userById(userID)) }
}

class PickupViewModel(application: Application) : BaseViewModel(application) {
    fun pickups(userID: Int) = repo.pickupsForUser(userID)
    fun schedule(pickup: Pickup, onDone: () -> Unit) = viewModelScope.launch {
        repo.schedulePickup(pickup)
        onDone()
    }
}

class RewardsViewModel(application: Application) : BaseViewModel(application) {
    val items = repo.activeRewardItems
    fun reward(userID: Int) = repo.rewardForUser(userID)
    fun redemptions(userID: Int) = repo.redemptionsForUser(userID)
    fun redeem(userID: Int, itemID: Int) = viewModelScope.launch {
        val result = repo.redeemReward(userID, itemID)
        message.value = result.fold({ "Voucher generated: $it" }, { it.message ?: "Unable to redeem" })
    }
}

class GuidanceViewModel(application: Application) : BaseViewModel(application) {
    val categories = repo.allCategories
    val guidance = repo.allGuidance
    fun guidanceFor(categoryID: Int) = repo.guidanceByCategory(categoryID)
}

class ScannerViewModel(application: Application) : BaseViewModel(application) {
    val result = MutableLiveData<String>()
    fun classify(userID: Int, preferred: String? = null) = viewModelScope.launch {
        val (category, confidence) = repo.classifyWaste(userID, preferred)
        result.value = "${category.categoryName}|$confidence|${category.wasteCategoryID}"
    }
}

class ProfileViewModel(application: Application) : BaseViewModel(application) {
    val user = MutableLiveData<User?>()
    fun load(userID: Int) = viewModelScope.launch { user.value = repo.userById(userID) }
    fun update(user: User, onDone: () -> Unit) = viewModelScope.launch {
        repo.updateUser(user)
        onDone()
    }
}

class NotificationsViewModel(application: Application) : BaseViewModel(application) {
    fun notifications(userID: Int) = repo.notificationsForUser(userID)
    fun unread(userID: Int) = repo.unreadNotifications(userID)
    fun markRead(notification: Notification) = viewModelScope.launch { repo.markNotificationRead(notification) }
}

class AdminViewModel(application: Application) : BaseViewModel(application) {
    val users = repo.allUsers
    val allPickups = repo.allPickups
    val rewardItems: LiveData<List<RewardItem>> = repo.allRewardItems
    val totalUsers = repo.totalUsers
    val totalPickups = repo.totalPickups
    val pendingPickups = repo.pendingPickups
    val completedPickups = repo.completedPickups
    val totalRedemptions = repo.totalRedemptions
    val totalKg = repo.totalKgRecycled
    val totalPoints = repo.totalPointsAwarded
    val commonWaste = repo.mostCommonWasteType

    fun toggleUser(user: User) = viewModelScope.launch { repo.updateUser(user.copy(isActive = !user.isActive)) }
    fun updatePickup(pickup: Pickup, status: String) = viewModelScope.launch { repo.updatePickupStatus(pickup, status) }
    fun saveReward(item: RewardItem) = viewModelScope.launch { repo.saveRewardItem(item) }
}
