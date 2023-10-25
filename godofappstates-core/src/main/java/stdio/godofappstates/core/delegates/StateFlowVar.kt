package stdio.godofappstates.core.delegates

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StateFlowVar<T>(
    private val mutableStateFlow: MutableStateFlow<T>
) : ReadWriteProperty<Any?, T> {
    override fun getValue(
        thisRef: Any?, property: KProperty<*>
    ): T = synchronized(this) {
        return mutableStateFlow.value
    }

    override fun setValue(
        thisRef: Any?, property: KProperty<*>, value: T
    ) = synchronized(this) {
        mutableStateFlow.value = value
    }

    companion object {
        fun <T> stateFlowVar(mutableStateFlow: MutableStateFlow<T>) = StateFlowVar(mutableStateFlow)
    }
}