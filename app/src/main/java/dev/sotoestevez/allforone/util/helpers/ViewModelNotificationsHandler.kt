package dev.sotoestevez.allforone.util.helpers

import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification

interface ViewModelNotificationsHandler {

    suspend fun getNotifications(): List<Notification>

    /**
     * Subscribes the socket to a certain notification and executes the given callback
     *
     * @param callback  Event listener, receives the notification
     */
    fun onNotification(action: Action, callback: (name: Notification) -> Unit)

    /**
     * Sets a notification as read by the user
     *
     * @param notification	Notification to set as read
     */
    fun setAsRead(notification: Notification)

}