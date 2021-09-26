package dev.sotoestevez.allforone.api.schemas

import com.google.gson.annotations.SerializedName
import java.time.Instant

// Schemas of the different messages to send and receive in socket event emissions

/**
 * Message sent from the server when an user joins the Global room
 *
 * @property id id of the user subscribed
 * @property room id of the room the user has joined
 */
data class GlobalSubscriptionMsg(
	val id: String,
	val room: String
)

/**
 * Message with the most basic user info
 *
 * @property id unique id of the current user
 * @property displayName display name of the current user
 */
data class UserInfoMsg(
	@SerializedName("_id") val id: String,
	val displayName: String
)

/**
 * Message with the info to send a new feed message
 *
 * @property message content of the message
 * @property user author of the message
 * @property timestamp creation timestamp of the message
 */
data class FeedMsg(
	val message: String,
	val user: UserInfoMsg,
	val timestamp: Long = Instant.now().toEpochMilli()
)
