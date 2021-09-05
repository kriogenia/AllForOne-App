package dev.sotoestevez.allforone.ui.bonds

import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityBondsBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.bonds.BondsViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import java.util.*

/** Activity to list the bonds of the user and create new ones in case that the user is a [User.Role.PATIENT]*/
class BondsActivity : PrivateActivity() {

	override val model: BondsViewModel by viewModels { ExtendedViewModelFactory(this) }

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT, User.Role.KEEPER)

	private lateinit var binding: ActivityBondsBinding
	private val bondsAdapter by lazy { BondsAdapter() }

	override fun bindLayout() {
		binding = ActivityBondsBinding.inflate(layoutInflater)
		binding.model = model
		binding.lifecycleOwner = this
		setContentView(binding.root)
		// RecyclerView
		binding.rvBonds.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = bondsAdapter
		}
	}

	override fun attachListeners() {
		super.attachListeners()
		binding.btnAddBond.setOnClickListener { showQrPanel(true) }
		binding.btnCollapseQr.setOnClickListener { showQrPanel(false) }
	}

	override fun attachObservers() {
		super.attachObservers()
		model.bonds.observe(this) { bondsAdapter.submitList(it) }
		model.qrCode.observe(this) {
			if (Strings.isEmptyOrWhitespace(it))
				return@observe
			model.loadingQr.value = false
			binding.imgQrCode.setImageBitmap(model.getQrCodeBitmap(it))
		}
	}

	private fun showQrPanel(expand: Boolean) {
		TransitionManager.beginDelayedTransition(binding.layBonds, ChangeBounds())
		if (expand) {
			binding.layQr.visibility = View.VISIBLE
			binding.btnAddBond.visibility = View.GONE
			model.generateNewQRCode()
		} else {
			binding.layQr.visibility = View.GONE
			binding.btnAddBond.visibility = View.VISIBLE
		}
	}

}