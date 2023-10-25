package stdio.godofappstates.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import stdio.godofappstates.UsedPackages.loadableDataPackage
import stdio.godofappstates.UsedPackages.operationStatePackage
import java.io.OutputStream

internal class BaseRemoteDataSourceKClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val dependencies: Dependencies,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val packageName = "godofappstates.data.dataSource"

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = "BaseRemoteDataSource"
        )



        outputStream.write(
            """
    |package $packageName
    |
    |import $operationStatePackage.OperationState
    |import $loadableDataPackage.LoadableData
    |import kotlinx.coroutines.Dispatchers
    |import kotlinx.coroutines.withContext
    |import retrofit2.Response
    |import stdio.godofappstates.core.util.Utils
    |import stdio.godofappstates.core.domain.DataType
    |import stdio.godofappstates.core.domain.OperationType
    |
    |open class BaseRemoteDataSource {
    |
    |    /**
    |     * Generates OperationState that contains a limited set of states for any request.
    |     *
    |     * ```
    |     * ```
    |     *
    |     * In its turn, use cases generate special states some any functionality
    |     *
    |     * (For example: authorization with validation, registration with validation).
    |     */
    |    suspend fun <T> executeOperation(
    |        operationType: OperationType = OperationType.DefaultOperation,
    |        call: suspend () -> Response<T>
    |    ): OperationState<T> {
    |        try {
    |            val response = withContext(Dispatchers.IO) { call() }
    |            if (response.isSuccessful) {
    |                val body = response.body()
    |                body?.let {
    |                    return OperationState.Success(body, operationType)
    |                }
    |                return OperationState.Empty204(response.code(), operationType)
    |            }
    |            val errorMessage = Utils.encodeErrorCode(response.errorBody())
    |            return operationError(errorMessage, response.code())
    |        } catch (e: Exception) {
    |            return operationError(e.message ?: " ", -1)
    |        }
    |    }
    |
    |    /**
    |     * Generates LoadableData that contains a limited set of loading data states
    |     */
    |    suspend fun <T> loadData(
    |        dataType: DataType = DataType.NotSpecified,
    |        call: suspend () -> Response<T>
    |    ): LoadableData<T> {
    |        try {
    |            val response = withContext(Dispatchers.IO) { call() }
    |            if (response.isSuccessful) {
    |                val body = response.body()
    |                body?.let {
    |                    return LoadableData.Success(body, dataType)
    |                }
    |                return LoadableData.Empty204(response.code(), dataType)
    |            }
    |            val errorMessage = Utils.encodeErrorCode(response.errorBody())
    |            return loadError(errorMessage, response.code())
    |        } catch (e: Exception) {
    |            return loadError(e.message ?: " ", -1)
    |        }
    |    }
    |
    |    private fun <T> operationError(errorMessage: String, code: Int): OperationState<T> =
    |        OperationState.Error(errorMessage, code)
    |
    |    private fun <T> loadError(errorMessage: String, code: Int): LoadableData<T> =
    |        LoadableData.Error(errorMessage, code)
    |}
    """.trimMargin().toByteArray()
        )

    }
}