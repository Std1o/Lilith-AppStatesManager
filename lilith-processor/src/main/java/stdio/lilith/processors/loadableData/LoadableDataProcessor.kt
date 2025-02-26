package stdio.lilith.processors.loadableData

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import stdio.lilith.UsedPackages.loadableDataPackage
import stdio.lilith.Utils.getStrParents
import stdio.lilith.annotations.LoadableData
import stdio.lilith.visitors.LoadableDataKClassVisitor

class LoadableDataProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())

        val symbols = resolver
            .getSymbolsWithAnnotation(LoadableData::class.qualifiedName!!)
            .filter { it is KSClassDeclaration && it.validate() }
        val loadableDataParents = getStrParents(symbols)
        symbols.firstOrNull()?.accept(
            LoadableDataKClassVisitor(
                codeGenerator, logger, dependencies, loadableDataParents
            ) { mPackage ->
                loadableDataPackage = mPackage
            },
            Unit
        )
        return symbols.filterNot { it.validate() }.toList()
    }
}