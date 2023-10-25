package stdio.godofappstates.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import stdio.godofappstates.Constants
import java.io.OutputStream

internal class StatesProtectionKClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val dependencies: Dependencies,
    private val parents: List<String>,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val packageName = classDeclaration.packageName.asString()

        if (classDeclaration.classKind != ClassKind.INTERFACE) {
            logger.error(Constants.OPERATION_MUST_BE_AN_INTERFACE, classDeclaration)
        }

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = "StatesProtection"
        )

        var statesProtections = ""
        for (parent in parents) {
            statesProtections += """
                |
                |/**
                |* If you use [StatesViewModel.executeOperation] or [StatesViewModel.executeEmptyOperation] or
                |* [StatesViewModel.executeOperationAndIgnoreData] with labda that returns some OperationState
                |* there may be problem with generics auto cast
                |*
                |* [Watch issue](https://github.com/Kotlin/kotlinx.coroutines/issues/3904)
                |*
                |* Use this extension, otherwise your coroutine must contain at least one val/var
                |* or you must specify explicit generics for this method!
                |*
                |*/
                |fun <R> $parent<R>.protect() {}
                |
            """.trimMargin()
        }

        outputStream.write(
            ("""
    |package $packageName
    |
    |/**
    |* If you use [StatesViewModel.executeOperation] or [StatesViewModel.executeEmptyOperation] or
    |* [StatesViewModel.executeOperationAndIgnoreData] with labda that returns some OperationState
    |* there may be problem with generics auto cast
    |*
    |* [Watch issue](https://github.com/Kotlin/kotlinx.coroutines/issues/3904)
    |*
    |* Use this extension, otherwise your coroutine must contain at least one val/var
    |* or you must specify explicit generics for this method!
    |*
    |*/
    |fun <R> OperationState<R>.protect() {}
    |
    """.trimMargin() + statesProtections).toByteArray()
        )

    }
}