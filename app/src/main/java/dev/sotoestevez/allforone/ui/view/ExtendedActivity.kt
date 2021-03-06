package dev.sotoestevez.allforone.ui.view

import android.app.Activity
import android.content.Intent

/** Extension to the Activities with common operations to all the application activities */
interface ExtendedActivity : WithModel {

    /** Operation to bind the layout of the activity */
    fun bindLayout()

    /**
     * Builds a new intent with all the commonly available info to start the desired activity
     *
     * @param next Next activity to launch
     */
    fun buildIntent(next: Class<out Activity>): Intent

    /**
     * Error handling function to manage known errors
     *
     * @param error Registered error to handle
     */
    fun handleError(error: Throwable)

}