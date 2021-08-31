package dev.sotoestevez.allforone.ui.keeper

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityKeeperMainBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.keeper.qr.QRScannerActivity
import dev.sotoestevez.allforone.model.keeper.KeeperMainViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import dev.sotoestevez.allforone.util.extensions.toast
import java.util.*

/**
 *  Main activity of Keepers.
 *  Handles the bonding with the Patient when no bond is set invoking the QR Scanner and connecting with the server
 */
class KeeperMainActivity : PrivateActivity() {

	override val model: KeeperMainViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityKeeperMainBinding

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.KEEPER)

	private lateinit var qrScannerLauncher: ActivityResultLauncher<Intent>

	override fun bindLayout() {
		binding = ActivityKeeperMainBinding.inflate(layoutInflater)
		binding.model = model
		binding.lifecycleOwner = this
		setContentView(binding.root)
	}

	override fun attachListeners() {
		super.attachListeners()
		/* No bond listeners */
		qrScannerLauncher = registerForActivityResult(
			ActivityResultContracts.StartActivityForResult()
		) { result ->
			if (result.resultCode == Activity.RESULT_OK)
				model.bond(result.data?.data.toString())
			else
				toast(getString(R.string.error_qr_scanner))
		}
		binding.btnForgeBond.setOnClickListener { qrScannerLauncher.launch(Intent(this, QRScannerActivity::class.java)) }
	}

}