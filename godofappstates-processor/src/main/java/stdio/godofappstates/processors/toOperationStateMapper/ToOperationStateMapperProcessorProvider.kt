package stdio.godofappstates.processors.toOperationStateMapper

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class ToOperationStateMapperProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ToOperationStateMapperProcessor(environment.codeGenerator, environment.logger)
    }
}