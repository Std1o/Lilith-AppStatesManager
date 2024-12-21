package com.stdio.godofappstates.presentation.viewmodel;

import androidx.lifecycle.viewModelScope
import com.stdio.godofappstates.domain.operationState.MainScreenEventsChild
import com.stdio.godofappstates.domain.operationState.OperationState
import com.stdio.godofappstates.domain.operationState.protect
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.domain.SingleEventFlow
import godofappstates.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : StatesViewModel() {

    private val _screenEvents = SingleEventFlow<MainScreenEventsChild<String>>()
    val screenEvents = _screenEvents.asSharedFlow()

    init {
        //test()
        testErrors()
    }

    private fun test() {
        makeSingleRequest()
        makeRequest()
    }

    private fun testErrors() {
        makeSingleErrorRequest()
        makeErrorRequest()
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

    private fun makeErrorRequest() {
        viewModelScope.launch {
            executeOperation(
                { fakeRequestError() }, String.Companion::class.java::class
            ).protect()
        }
    }

    private fun makeSingleErrorRequest() {
        viewModelScope.launch {
            val result = executeOperation(
                { fakeRequestSingleError() }, String.Companion::class.java::class
            )
            _screenEvents.emit(result)
        }
    }

    private suspend fun fakeRequest(): OperationState<String> {
        delay(7000)
        return OperationState.Success("All data has been uploaded successfully")
    }

    private suspend fun fakeRequestSingle(): MainScreenEventsChild<String> {
        delay(7000)
        return MainScreenEventsChild.ShowSuccessToast("It's Single event")
    }

    private suspend fun fakeRequestError(): OperationState<String> {
        delay(7000)
        return OperationState.Error("Something went wrong")
    }

    private suspend fun fakeRequestSingleError(): MainScreenEventsChild<String> {
        delay(7000)
        return OperationState.ErrorSingle("Something went wrong")
    }
}
