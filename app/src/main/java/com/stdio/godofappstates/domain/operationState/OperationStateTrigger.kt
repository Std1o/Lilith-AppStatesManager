package com.stdio.godofappstates.domain.operationState

import stdio.godofappstates.annotations.OperationState
import stdio.godofappstates.annotations.SingleEvent

@OperationState
sealed interface OperationStateTrigger<out R> {
    @SingleEvent
    data class Init(val jjj: String)
}