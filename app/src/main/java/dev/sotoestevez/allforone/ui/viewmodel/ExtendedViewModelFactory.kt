package dev.sotoestevez.allforone.ui.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * Factory to generate ViewModels with SavedStateHandles and SharedPreferences
 *
 * @property activity of the ViewModel
 */
class ExtendedViewModelFactory(
    val activity: AppCompatActivity
) : AbstractSavedStateViewModelFactory(
    activity,
    if (activity.intent != null) activity.intent.extras else null
) {

    /**
     * Factory method to build the desired ViewModel
     *
     * @param T type of the ViewModel
     * @param key qualified name of the ViewModel
     * @param modelClass class of the ViewModel
     * @param handle handle of the SavedState to pass as parameter
     * @return  expected ViewModel
     */
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return ExtendedViewModel.Builder().apply { savedStateHandle = handle }.build(modelClass)
    }

}