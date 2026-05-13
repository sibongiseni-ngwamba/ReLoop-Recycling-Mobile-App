package com.reloop.app.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reloop.app.R
import com.reloop.app.data.entities.GuidanceContent
import com.reloop.app.data.entities.Notification
import com.reloop.app.data.entities.Pickup
import com.reloop.app.data.entities.RedemptionLog
import com.reloop.app.data.entities.RewardItem
import com.reloop.app.data.entities.User
import com.reloop.app.data.entities.WasteCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun dateText(value: Long): String = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(value))

class PickupAdapter(private val adminMode: Boolean = false, private val onStatus: (Pickup, String) -> Unit = { _, _ -> }) :
    RecyclerView.Adapter<PickupAdapter.Holder>() {
    private var items = listOf<Pickup>()
    fun submit(list: List<Pickup>) { items = list; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_pickup, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.titleText)
        private val detail: TextView = view.findViewById(R.id.detailText)
        private val status: TextView = view.findViewById(R.id.statusText)
        private val action: Button = view.findViewById(R.id.actionButton)
        fun bind(item: Pickup) {
            title.text = "${item.wasteType} pickup"
            detail.text = "${item.scheduledDate} at ${item.scheduledTime}\n${item.address}"
            status.text = item.status.uppercase()
            status.setBackgroundResource(if (item.status == "completed") R.drawable.badge_done else if (item.status == "cancelled") R.drawable.badge_cancelled else R.drawable.badge_pending)
            action.visibility = if (adminMode) View.VISIBLE else View.GONE
            action.text = when (item.status) { "pending" -> "Confirm"; "confirmed" -> "Complete"; "completed" -> "Completed"; else -> "Reset" }
            action.isEnabled = true
            action.setOnClickListener {
                if (adminMode) {
                    PopupMenu(itemView.context, action).apply {
                        listOf("pending", "confirmed", "completed", "cancelled").forEach { menu.add(it) }
                        setOnMenuItemClickListener { selected ->
                            onStatus(item, selected.title.toString())
                            true
                        }
                    }.show()
                } else {
                    val next = when (item.status) { "pending" -> "confirmed"; "confirmed" -> "completed"; "cancelled" -> "pending"; else -> "completed" }
                    onStatus(item, next)
                }
            }
        }
    }
}

class RewardItemAdapter(private val onRedeem: (RewardItem) -> Unit) : RecyclerView.Adapter<RewardItemAdapter.Holder>() {
    private var items = listOf<RewardItem>()
    fun submit(list: List<RewardItem>) { items = list; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_reward, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: RewardItem) {
            itemView.findViewById<TextView>(R.id.titleText).text = item.itemName
            itemView.findViewById<TextView>(R.id.detailText).text = "${item.description}\n${item.pointsCost} points"
            itemView.findViewById<Button>(R.id.actionButton).apply {
                text = if (item.isActive) "Redeem" else "Inactive"
                isEnabled = item.isActive
                setOnClickListener { onRedeem(item) }
            }
        }
    }
}

class GuidanceAdapter(private val onClick: (WasteCategory) -> Unit) : RecyclerView.Adapter<GuidanceAdapter.Holder>() {
    private var items = listOf<WasteCategory>()
    fun submit(list: List<WasteCategory>) { items = list; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_simple, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: WasteCategory) {
            itemView.findViewById<TextView>(R.id.titleText).text = item.categoryName
            itemView.findViewById<TextView>(R.id.detailText).text = item.description
            itemView.setOnClickListener { onClick(item) }
        }
    }
}

class GuidanceContentAdapter : RecyclerView.Adapter<GuidanceContentAdapter.Holder>() {
    private var items = listOf<GuidanceContent>()
    fun submit(list: List<GuidanceContent>) { items = list; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_simple, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: GuidanceContent) {
            itemView.findViewById<TextView>(R.id.titleText).text = item.title
            itemView.findViewById<TextView>(R.id.detailText).text = item.content
        }
    }
}

class NotificationAdapter(private val onRead: (Notification) -> Unit) : RecyclerView.Adapter<NotificationAdapter.Holder>() {
    private var items = listOf<Notification>()
    fun submit(list: List<Notification>) { items = list; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Notification) {
            itemView.findViewById<TextView>(R.id.titleText).text = if (item.isRead) item.type.uppercase() else "NEW ${item.type.uppercase()}"
            itemView.findViewById<TextView>(R.id.detailText).text = "${item.message}\n${dateText(item.sentAt)}"
            itemView.findViewById<Button>(R.id.actionButton).apply {
                visibility = if (item.isRead) View.GONE else View.VISIBLE
                setOnClickListener { onRead(item) }
            }
        }
    }
}

class UserAdapter(private val onToggle: (User) -> Unit) : RecyclerView.Adapter<UserAdapter.Holder>() {
    private var items = listOf<User>()
    fun submit(list: List<User>) { items = list; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: User) {
            itemView.findViewById<TextView>(R.id.titleText).text = "${item.firstName} ${item.lastName}"
            itemView.findViewById<TextView>(R.id.detailText).text = "${item.email}\nRole: ${item.role} | ${if (item.isActive) "Active" else "Inactive"}"
            itemView.findViewById<Button>(R.id.actionButton).apply {
                text = if (item.isActive) "Deactivate" else "Activate"
                setOnClickListener { onToggle(item) }
            }
        }
    }
}

class RedemptionAdapter : RecyclerView.Adapter<RedemptionAdapter.Holder>() {
    private var items = listOf<RedemptionLog>()
    fun submit(list: List<RedemptionLog>) { items = list; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_simple, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: RedemptionLog) {
            itemView.findViewById<TextView>(R.id.titleText).text = "Voucher ${item.voucherCode}"
            itemView.findViewById<TextView>(R.id.detailText).text = "${item.pointsUsed} points used on ${dateText(item.redeemedAt)}"
        }
    }
}
