package stdio.lilith.processors.operationState

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import stdio.lilith.UsedPackages.operationStatePackage
import stdio.lilith.Utils.getStrParents
import stdio.lilith.annotations.OperationState
import stdio.lilith.visitors.OperationStateKClassVisitor
import stdio.lilith.visitors.StatesProtectionKClassVisitor

class OperationStateProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())

        val symbols = resolver
            .getSymbolsWithAnnotation(OperationState::class.qualifiedName!!)
            .filter { it is KSClassDeclaration && it.validate() }
        val operationStateParents = getStrParents(symbols)
        symbols.firstOrNull()?.accept(
            OperationStateKClassVisitor(
                codeGenerator, logger, dependencies, operationStateParents
            ) { mPackage ->
                operationStatePackage = mPackage
            },
            Unit
        )
        symbols.firstOrNull()?.accept(
            StatesProtectionKClassVisitor(
                codeGenerator, logger, dependencies, symbols.toList().map { it.toString() }
            ),
            Unit
        )
        return symbols.filterNot { it.validate() }.toList()
    }
}