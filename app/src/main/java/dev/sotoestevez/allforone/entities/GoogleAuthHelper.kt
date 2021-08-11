package dev.sotoestevez.allforone.entities

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.util.logDebug
import dev.sotoestevez.allforone.util.logError
import dev.sotoestevez.allforone.util.logWarning
import dev.sotoestevez.allforone.util.toast

class GoogleAuthHelper (private val activity: ComponentActivity) {

	private lateinit var authenticationLauncher: ActivityResultLauncher<IntentSenderRequest>

	/**
	 * Launches an intent to call the Google Sign In API to perform the authentication
	 */
	fun invokeSignInAPI() {
		// Build the sign-in request
		val request = GetSignInIntentRequest.builder()
			.setServerClientId(activity.getString(R.string.server_client_id))
			.build()
		// Launches and manages the sign-in intent
		Identity.getSignInClient(activity)
			.getSignInIntent(request)
			.addOnSuccessListener { signInResult ->
				activity.logDebug("Launching Sign-In intent")
				authenticationLauncher.launch(IntentSenderRequest.Builder(signInResult).build())
			}
			.addOnFailureListener { e ->
				activity.logError("Google Sign-in failed", e)
				activity.toast(activity.getString(R.string.error_google_auth))
			}
	}

	public fun setCallback(
		processToken: (String) -> Unit
	) {
		authenticationLauncher = activity.registerForActivityResult(
			ActivityResultContracts.StartIntentSenderForResult()
		) { result ->
			if (result.resultCode == Activity.RESULT_OK) {
				try {
					val credential = Identity.getSignInClient(activity)
						.getSignInCredentialFromIntent(result.data)
					if (credential.googleIdToken != null)
						processToken(credential.googleIdToken!!)
					else
						activity.toast(activity.getString(R.string.error_invalid_google_account))
				} catch (e: ApiException) {
					activity.logError("Error retrieving user data from intent", e)
					activity.toast(activity.getString(R.string.error_google_auth))
				}
			} else {
				activity.logWarning("Google Sign-In intent failed and returned ${result.resultCode}")
				activity.toast(activity.getString(R.string.error_google_auth))
			}
		}
	}

}