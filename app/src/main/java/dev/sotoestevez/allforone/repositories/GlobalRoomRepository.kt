package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.Notification

/** Repository in charge of the Global Room connections and events */
interface GlobalRoomRepository: SocketRepository {

	/**
	 * Subscribes the socket to the event thrown when a Bond starts sharing its location
	 *
	 * @param callback  Event listener, receives the name of the subscribed user
	 */
	fun onSharingLocation(callback: (name: Notification) -> Unit)

}