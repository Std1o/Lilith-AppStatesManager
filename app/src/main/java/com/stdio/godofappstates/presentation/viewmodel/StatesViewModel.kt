package com.stdio.godofappstates.presentation.viewmodel

import androidx.lifecycle.ViewModel

/**
 * Dagger2 doesn't see generated StatesViewModel,
 * therefore it cannot check that it is a ViewModel subclass and throws an error
 */
@Suppress("unused")
class StatesViewModel : ViewModel()