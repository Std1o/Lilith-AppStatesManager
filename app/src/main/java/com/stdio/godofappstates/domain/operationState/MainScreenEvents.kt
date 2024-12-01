package com.stdio.godofappstates.domain.operationState

import stdio.godofappstates.annotations.OperationState

@OperationState
sealed interface MainScreenEvents<out R> {
}