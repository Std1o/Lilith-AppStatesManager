package stdio.lilith.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import stdio.lilith.UsedPackages.operationStatePackage
import java.io.OutputStream

internal class StateWrapperKClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val dependencies: Dependencies,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val packageName = "lilith.presentation.stateWrapper"

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = "StateWrapper"
        )



        outputStream.write(
            """
    |package $packageName

    |import stdio.lilith.annotations.FunctionalityState
    |import stdio.lilith.core.domain.OperationType
    |import stdio.lilith.core.presentation.stateWrapper.OnReceiveListener
    |import kotlinx.coroutines.flow.MutableStateFlow
    |import $operationStatePackage.OperationState
    |
    |/**
    | * Wrapper is necessary in order for state to be resettable
    | */
    |class StateWrapper<State>(operationState: State = OperationState.NoState as State) :
    |    OnReceiveListener {
    |    var uiState: State = operationState
    |        private set
    |
    |    /**
    |     * Used to reset state. You can use it as a SingleEvent.
    |     * However, you can reset the state later. For example, to reset the error in TextField's
    |     *
    |     * ```
    |     * onValueChange {}
    |     * ```
    |     */
    |    override fun onReceive() {
    |        uiState = OperationState.NoState as State
    |    }
    |
    |    companion object {
    |        fun <State> mutableStateFlow(operationState: State = OperationState.NoState as State) =
    |            MutableStateFlow(StateWrapper(operationState))
    |    }
    |}
    """.trimMargin().toByteArray()
        )

    }
}