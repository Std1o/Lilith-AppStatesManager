package com.stdio.godofappstates.domain.operationState

import stdio.godofappstates.annotations.OperationState
import stdio.godofappstates.annotations.SingleEvent

@OperationState
sealed interface MainScreenEvents<out R> {
    @SingleEvent
    data class ShowSuccessToast(val text: String) : MainScreenEvents<String>
}