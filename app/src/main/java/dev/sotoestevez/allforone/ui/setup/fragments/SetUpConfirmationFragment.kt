package dev.sotoestevez.allforone.ui.setup.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentSetUpConfirmationBinding
import dev.sotoestevez.allforone.model.setup.SetUpViewModel
import dev.sotoestevez.allforone.ui.MyFragment
import dev.sotoestevez.allforone.util.extensions.logDebug


/**
 * Last [Fragment] of SetUpActivity, it shows all the info that the user entered
 * and asks for confirmation. Once the user confirms the data, it's send to the
 * API to update it and the users is moved to its correct MainActivity
 */
class SetUpConfirmationFragment : MyFragment() {

	private val binding: FragmentSetUpConfirmationBinding
		get() = _binding!!
	private var _binding: FragmentSetUpConfirmationBinding? = null

	private val model: SetUpViewModel by activityViewModels()

	override fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
		_binding = FragmentSetUpConfirmationBinding.inflate(inflater, container, false)
		binding.profileCard.user = model.user.value
		return binding.root
	}

	override fun attachListeners() {
		binding.layButtonsSetUpConfirmation.btnPrevious.setOnClickListener {
			findNavController().navigate(R.id.action_SetUpConfirmationFragment_to_ContactFillFragment)
		}
		binding.layButtonsSetUpConfirmation.btnNext.setOnClickListener {
			confirmData()
		}
	}

	override fun attachObservers() {
		model.destiny.observe(this) {
			logDebug("Move to $it")
		}
	}

	private fun confirmData() {
		uiLoading(true)
		model.sendUpdate()
	}

	// Receives update
	// Changes Activity

	private fun uiLoading(loading: Boolean) {
		binding.loadBarSetUp.visibility = if (loading) View.VISIBLE else View.GONE
		binding.layButtonsSetUpConfirmation.btnPrevious.isEnabled = !loading
		binding.layButtonsSetUpConfirmation.btnNext.isEnabled = !loading
	}

}