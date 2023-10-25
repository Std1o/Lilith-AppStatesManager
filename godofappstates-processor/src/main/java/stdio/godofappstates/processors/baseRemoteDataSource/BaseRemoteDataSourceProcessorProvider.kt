package stdio.godofappstates.processors.baseRemoteDataSource

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class BaseRemoteDataSourceProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return BaseRemoteDataSourceProcessor(environment.codeGenerator, environment.logger)
    }
}