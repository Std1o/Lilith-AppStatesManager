package com.stdio.godofappstates.presentation.viewmodel;

import androidx.lifecycle.viewModelScope
import com.stdio.godofappstates.domain.operationState.OperationState
import com.stdio.godofappstates.domain.operationState.protect
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : StatesViewModel() {

    init {
        makeRequest()
    }

    private fun makeRequest() {
        viewModelScope.launch {
            executeOperation(
                { fakeRequest() }, String.Companion::class.java::class
            ).protect()
        }
    }

    suspend fun fakeRequest(): OperationState<String> {
        delay(7000)
        return OperationState.Success("Hello")
    }
}
