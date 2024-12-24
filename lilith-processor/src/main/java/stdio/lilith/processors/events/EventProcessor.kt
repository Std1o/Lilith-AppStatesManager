package stdio.lilith.processors.events

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import stdio.lilith.Constants
import stdio.lilith.UsedPackages.loadableDataPackage
import stdio.lilith.UsedPackages.operationStatePackage
import stdio.lilith.annotations.AllStatesReadyToUse
import stdio.lilith.visitors.EventKClassVisitor

class EventProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())

        val symbols = resolver
            .getSymbolsWithAnnotation(AllStatesReadyToUse::class.qualifiedName!!)
            .filter { it is KSClassDeclaration && it.validate() }
        if (symbols.toList().isEmpty()) return emptyList()
        if (operationStatePackage == null) {
            logger.error(Constants.NO_OPERATION_STATE_ANNOTATION)
        } else if (loadableDataPackage == null) {
            logger.error(Constants.NO_LOADABLE_DATA_ANNOTATION)
        } else {
            symbols.firstOrNull()?.accept(
                EventKClassVisitor(codeGenerator, dependencies),
                Unit
            )
        }
        return symbols.filterNot { it.validate() }.toList()
    }
}