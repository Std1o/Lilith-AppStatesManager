import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Events<T> {
    private val mutableSharedFlow = MutableSharedFlow<T>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    fun tryEmit(value: T) {
        mutableSharedFlow.tryEmit(value)
    }

    suspend fun emit(value: T) {
        mutableSharedFlow.emit(value)
    }

    suspend fun collect(flowCollector: FlowCollector<T>) {
        mutableSharedFlow.collect(flowCollector)
    }

    fun asSharedFlow(): SharedFlow<T> {
        return mutableSharedFlow.asSharedFlow()
    }
}