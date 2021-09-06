package dev.sotoestevez.allforone.ui.interfaces

import dev.sotoestevez.allforone.model.ExtendedViewModel

/** Objects using an ExtendedViewModel */
interface WithModel {

	/** Model to handle the logic of the UI component */
	val model: ExtendedViewModel

}