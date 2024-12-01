package com.stdio.godofappstates.domain.operationState

import stdio.godofappstates.annotations.OperationState
import stdio.godofappstates.annotations.SingleEvent
import stdio.godofappstates.annotations.SingleEvents

@OperationState
@SingleEvents
sealed interface MainScreenEventsChild<out R> {
    data class ShowSuccessToast(val text: String) : MainScreenEventsChild<String>
}