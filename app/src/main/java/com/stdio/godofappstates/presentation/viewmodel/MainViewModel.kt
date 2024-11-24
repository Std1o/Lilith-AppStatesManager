package com.stdio.godofappstates.presentation.viewmodel;

import androidx.lifecycle.viewModelScope
import com.stdio.godofappstates.domain.operationState.MainScreenState
import com.stdio.godofappstates.domain.operationState.OperationState
import com.stdio.godofappstates.domain.operationState.protect
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.domain.EventFlow
import godofappstates.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : StatesViewModel() {

    private val _screenEvents = EventFlow<MainScreenState<String>>()
    val screenEvents = _screenEvents.asSharedFlow()

    init {
        makeSingleRequest()
        makeRequest()
    }

    private fun makeRequest() {
        viewModelScope.launch {
            executeOperation(
                { fakeRequest() }, String.Companion::class.java::class
            ).protect()
        }
    }

    private fun makeSingleRequest() {
        viewModelScope.launch {
            val result = executeOperation(
                { fakeRequestSingle() }, String.Companion::class.java::class
            )
            _screenEvents.emit(result)
        }
    }

    private suspend fun fakeRequest(): OperationState<String> {
        delay(7000)
        return OperationState.Success("All data has been uploaded successfully")
    }

    private suspend fun fakeRequestSingle(): MainScreenState<String> {
        delay(7000)
        return MainScreenState.ShowTextSingle("It's Single event")
    }
}
