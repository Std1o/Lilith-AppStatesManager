package com.stdio.lilith.domain.operationState

import stdio.lilith.annotations.OperationState

@OperationState
sealed interface OperationStateTrigger<out R>