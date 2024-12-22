package com.stdio.lilith.domain.operationState

import stdio.lilith.annotations.OperationState
import stdio.lilith.annotations.SingleEvents

@OperationState
@SingleEvents
sealed interface MainScreenEventsChild<out R> {
    data class ShowSuccessToast(val text: String) : MainScreenEventsChild<String>
}