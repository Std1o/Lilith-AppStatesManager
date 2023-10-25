package stdio.godofappstates.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import stdio.godofappstates.Constants
import java.io.OutputStream

internal class OperationStateKClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val dependencies: Dependencies,
    private val parents: String,
    private val onPackageKnown: (String) -> Unit
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val packageName = classDeclaration.packageName.asString()

        if (classDeclaration.classKind != ClassKind.INTERFACE) {
            logger.error(Constants.OPERATION_MUST_BE_AN_INTERFACE, classDeclaration)
        }
        onPackageKnown(packageName)

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = "OperationState"
        )



        outputStream.write(
            """
    |package $packageName

    |import stdio.godofappstates.annotations.FunctionalityState
    |import stdio.godofappstates.core.domain.OperationType
    |
    |/**
    | * OperationState contains the result of operation and is not intended for long-term storage of the screen state.
    | *
    | * Its value must either be passed to the ContentState,
    | * or used for some quick actions (show the loader, show the snackbar with an error, navigate to a new screen)
    | */
    |@FunctionalityState
    |sealed interface OperationState<out R> : $parents {
    |    data object NoState : OperationState<Nothing>
    |
    |    data class Success<out T>(
    |        val data: T,
    |        val operationType: OperationType = OperationType.DefaultOperation
    |    ) : OperationState<T>
    |
    |    /**
    |     * Must be converted to Success with own local value in ViewModel.
    |     *
    |     * Or you have to handle it by yourself
    |     */
    |    data class Empty204(
    |        val code: Int,
    |        val operationType: OperationType = OperationType.DefaultOperation
    |    ) : OperationState<Nothing>
    |
    |    data class Error(
    |        val exception: String,
    |        val code: Int = -1,
    |        val operationType: OperationType = OperationType.DefaultOperation
    |    ) : OperationState<Nothing>
    |
    |    data class Loading(val operationType: OperationType = OperationType.DefaultOperation) :
    |        OperationState<Nothing>
    |}
    """.trimMargin().toByteArray()
        )

    }
}