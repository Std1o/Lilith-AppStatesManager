package stdio.godofappstates.processors.statesViewModel

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class StatesViewModelProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return StatesViewModelProcessor(environment.codeGenerator, environment.logger)
    }
}