package com.stdio.godofappstates.domain.operationState

import stdio.godofappstates.annotations.OperationState

@OperationState
sealed interface OperationStateTrigger<out R>