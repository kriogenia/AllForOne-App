package dev.sotoestevez.allforone.ui.components.recyclerview.notifications

import android.content.Context
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.vo.Notification

/** View for user's action to notify in the feed */
class NotificationView(private val notification: Notification) : BindedItemView {

    override val id: String = notification.id

    override val layoutId: Int = R.layout.content_notification

    override val viewType: Int = 0

    /**
     * Prints the notification text
     *
     * @param context   Application context
     */
    fun print(context: Context) = notification.print(context)

    fun onClickListener() {}

}