package dev.sotoestevez.allforone.ui.components.fragments.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dev.sotoestevez.allforone.databinding.FragmentNotificationsBinding
import dev.sotoestevez.allforone.util.helpers.notifications.NotificationsManager
import androidx.recyclerview.widget.DividerItemDecoration
import dev.sotoestevez.allforone.R


/**
 * Dialog to display and manage the pending notifications of the user
 */
class NotificationsDialog(private val notificationsManager: NotificationsManager) : DialogFragment() {

    companion object {
        /** Tag of the fragment */
        const val TAG = "NotificationDialog"
    }

    private lateinit var binding: FragmentNotificationsBinding

    @Suppress("KDocMissingDocumentation")   // override method
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        binding.manager = notificationsManager
        binding.rvNotifications.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        return binding.root
    }

    @Suppress("KDocMissingDocumentation")  // override method, it overrides the dialog theme
    override fun getTheme(): Int = R.style.AppTheme_Dialog_Full

    @Suppress("KDocMissingDocumentation")  // override method
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { this.dismiss() }
    }
}