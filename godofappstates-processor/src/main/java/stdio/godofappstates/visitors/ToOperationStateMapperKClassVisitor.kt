package stdio.godofappstates.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import stdio.godofappstates.UsedPackages.operationStatePackage
import java.io.OutputStream

internal class ToOperationStateMapperKClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val dependencies: Dependencies,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val packageName = "godofappstates.data.mapper"

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = "ToOperationStateMapper"
        )



        outputStream.write(
            """
    |package $packageName
    |
    |import $operationStatePackage.OperationState

    |class ToOperationStateMapper<State, T> {
    |    @Suppress("UNCHECKED_CAST")
    |    fun map(input: State): OperationState<T> = try {
    |        input as OperationState<T>
    |    } catch (e: ClassCastException) {
    |        OperationState.NoState
    |    }
    |}
    """.trimMargin().toByteArray()
        )

    }
}