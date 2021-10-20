package dev.sotoestevez.allforone.ui.activities.keeper

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.ui.viewmodel.WithProfileCard
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.repositories.NotificationRepository
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.helpers.NotificationsManager
import dev.sotoestevez.allforone.util.helpers.ViewModelNotificationsHandler
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** ViewModel to handle the logic of the keeper's main activity */
class KeeperMainViewModel(
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatcherProvider,
    sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val globalRoomRepository: GlobalRoomRepository,
    private val notificationRepository: NotificationRepository
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard {

    /** LiveData holding the info about the patient cared by this user */
    val cared: LiveData<User>
        get() = mCared
    private val mCared = MutableLiveData<User>(null)

    // WithProfileCard
    override val profileCardExpandable: Boolean = true
    override val profileCardWithBanner: Boolean = true
    override val profileCardExpanded: MutableLiveData<Boolean> = MutableLiveData(false)

    /** Notifications manager */
    val notificationManager = NotificationsManager(object: ViewModelNotificationsHandler {
        override suspend fun getNotifications(): List<Notification> {
           return notificationRepository.getNotifications(authHeader())
        }

        override fun onNotification(action: Action, callback: (name: Notification) -> Unit) {
            notificationRepository.onNotification(action, callback)
        }

        override fun setAsRead(notification: Notification) {
            viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
                notificationRepository.setAsRead(notification, authHeader())
            }
        }
    })

    @Suppress("unused") // Used in the factory with a class call
    constructor(builder: ExtendedViewModel.Builder): this(
        builder.savedStateHandle,
        builder.dispatchers,
        builder.sessionRepository,
        builder.userRepository,
        builder.globalRoomRepository,
        builder.notificationRepository
    )

    init {
    	loading.value = true
        // Load cared user
        logDebug("Requesting info of cared user")
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
            userRepository.getCared(user.value!!, authHeader())?.let { setCared(it) }
            loading.postValue(false)
        }
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) { notificationManager.load() }
    }


    /**
     * Sends the scanned code to the server to bond with the user that provided the code
     *
     * @param code  Scanned code to perform the bond operation
     */
    fun bond(code: String) {
        logDebug("[${user.value?.id}] scanned QR Code: ${code.substring(0,6)}...")
        loading.value = true
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
            viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
                Log.d(KeeperMainViewModel::class.simpleName, "Sending bonding code")
                userRepository.sendBondingCode(code, authHeader())
            }.join()    // Wait until the send request finishes to know the outcome, if no error was thrown it was a success
            val response = userRepository.getCared(user.value!!, authHeader())
                ?: throw NullPointerException("Unable to forge the bond, please try again")
            Log.d(KeeperMainViewModel::class.simpleName, "Bond accepted")
            setCared(response)
        }
    }

    private suspend fun setCared(cared: User) {
        logDebug("Retrieved cared user ${cared.displayName}")
        withContext(dispatchers.main()) { mCared.value = cared }
        // Start socket connection
        notificationManager.subscribe()
        globalRoomRepository.join(user.value!!)
    }

}