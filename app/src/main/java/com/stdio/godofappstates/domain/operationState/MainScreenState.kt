package com.stdio.godofappstates.domain.operationState

import stdio.godofappstates.annotations.OperationState
import stdio.godofappstates.annotations.SingleEvent

@OperationState
sealed interface MainScreenState<out R> {
    @SingleEvent
    data class ShowTextSingle(val text: String) : MainScreenState<String>
    data class ShowText(val text: String) : MainScreenState<String>
    data object Initial : MainScreenState<Nothing>
}