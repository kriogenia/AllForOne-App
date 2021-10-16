package dev.sotoestevez.allforone.ui.activities.patient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.ui.viewmodel.WithProfileCard
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.vo.Notification

/** View Model of the Main activity for Patients */
class PatientMainViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository,
	globalRoomRepository: GlobalRoomRepository
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard {

	/** LiveData holding the identifier of the message to show in the warning panel */
	val notification: LiveData<Notification>
		get() = mNotification
	private val mNotification: MutableLiveData<Notification> = MutableLiveData(null)

	/** User sharing its location */
	var sharing: String = ""

	/** WithProfileCard */
	override val profileCardExpandable: Boolean = true
	override val profileCardWithBanner: Boolean = true
	override val profileCardExpanded: MutableLiveData<Boolean> = MutableLiveData(false)

	@Suppress("unused") // Used in the factory with a class call
	constructor(builder: ExtendedViewModel.Builder) : this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository,
		builder.globalRoomRepository
	)

	init {
		globalRoomRepository.onSharingLocation { mNotification.postValue(it) }
		globalRoomRepository.join(user.value!!)
	}


}