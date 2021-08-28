package dev.sotoestevez.allforone.model.launch

import android.app.Activity
import androidx.lifecycle.*
import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.model.ExtendedViewModel
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.ui.launch.LaunchActivity
import dev.sotoestevez.allforone.ui.setup.SetUpActivity
import dev.sotoestevez.allforone.ui.keeper.KeeperMainActivity
import dev.sotoestevez.allforone.ui.patient.PatientMainActivity
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.*

/**
 * ViewModel of the [LaunchActivity]
 *
 * @property dispatchers [DispatcherProvider] to inject the dispatchers
 *
 * @constructor
 * To create the ViewModel
 *
 * @param savedStateHandle [SavedStateHandle] object to store session data
 */
class LaunchViewModel(
	savedStateHandle: SavedStateHandle,
	private val dispatchers: DispatcherProvider = DefaultDispatcherProvider
): ViewModel(), ExtendedViewModel {

	override val sessionManager: SessionManager = SessionManager(savedStateHandle)

	/** Mutable implementation of the user live data exposed **/
	private var mUser: MutableLiveData<User> = MutableLiveData<User>()
	override val user: LiveData<User>
		get() = mUser

	/** Mutable implementation of the error live data exposed **/
	private var mError = MutableLiveData<Throwable>()
	override val error: LiveData<Throwable>
		get() = mError

	/** Live data holding the class of the next activity to launch from the LaunchActivity **/
	val destiny: LiveData<Class<out Activity>>
		get() = mDestiny
	private var mDestiny = MutableLiveData<Class<out Activity>>()


	/**
	 * Handles the retrieved token in the sign in request.
	 * Sends the Google token to the API to retrieve the User and the session tokens
	 * @param googleIdToken Id Token obtained in the authentication with Google
	 */
	fun handleSignInResult(googleIdToken: String){
		logDebug("Google-SignIn-Authentication: $googleIdToken")
		// Launch the coroutine with the request
		viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
			val result = SessionRepository.signIn(googleIdToken)
			// Store all the session info
			val ( session, user ) = result
			logDebug("Authentication validated. User[${user.id}]")
			sessionManager.setSession(session)
			// Update the data in the Main thread
			withContext(dispatchers.main()) {
				updateDestiny(user)
			}
		}
	}

	/**
	 * Once all the authorization is handled, update the User and [destiny] so the [LaunchActivity] can be
	 * notified and trigger the change of activity
	 *
	 * @param user Data of the authenticated user
	 */
	private fun updateDestiny(user: User) {
		// Save the user
		mUser.value = user
		// Decide the activity to navigate based on the user role (invoking the Activity)
		mDestiny.value = when (user.role) {
			User.Role.KEEPER -> KeeperMainActivity::class.java
			User.Role.PATIENT -> PatientMainActivity::class.java
			User.Role.BLANK -> SetUpActivity::class.java
		}
	}

	private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		mError.postValue(throwable)
	}

}