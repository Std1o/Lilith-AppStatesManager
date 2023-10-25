package stdio.godofappstates.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import stdio.godofappstates.Constants
import java.io.OutputStream

internal class LoadableDataKClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val dependencies: Dependencies,
    private val parents: String,
    private val onPackageKnown: (String) -> Unit
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val packageName = classDeclaration.packageName.asString()

        if (classDeclaration.classKind != ClassKind.INTERFACE) {
            logger.error(Constants.LOADABLE_MUST_BE_AN_INTERFACE, classDeclaration)
        }
        onPackageKnown(packageName)

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = "LoadableData"
        )



        outputStream.write(
            """
    |package $packageName

    |import stdio.godofappstates.annotations.FunctionalityState
    |import stdio.godofappstates.core.domain.DataType
    |
    |/**
    | * LoadableData is used for loading UI content from any data source.
    | *
    | * Examples of UI content: images, text information, lists.
    | *
    | * Usage exmaple:
    | * ```
    | * data class FeedContentState(
    | *     val news: LoadableData<News>,
    | *     val stories: LoadableData<Stories>
    | * )
    | * ```
    | *
    | * Or you can parse LoadableData in ViewModel and update ContentState fields
    | *
    | * ```
    | * data class FeedContentState(
    | *     val news: List<News>,
    | *     val newsIsLoading: Boolean,
    | *     val newsError: String,
    | *     val stories: List<Stories>,
    | *     val storiesIsLoading: Boolean,
    | *     val storiesError: String,
    | * )
    | * ```
    | */
    |@FunctionalityState
    |sealed interface LoadableData<out R> : $parents {
    |    data object NoState : LoadableData<Nothing>
    |
    |    data class Success<out T>(
    |        val data: T,
    |        val dataType: DataType = DataType.NotSpecified
    |    ) : LoadableData<T>
    |
    |    /**
    |     * Used only for 204 response code
    |     */
    |    data class Empty204(
    |        val code: Int,
    |        val dataType: DataType = DataType.NotSpecified
    |    ) : LoadableData<Nothing>
    |
    |    data class Error(
    |        val exception: String,
    |        val code: Int = -1,
    |        val dataType: DataType = DataType.NotSpecified
    |    ) : LoadableData<Nothing>
    |
    |    data class Loading(val dataType: DataType = DataType.NotSpecified) :
    |        LoadableData<Nothing>
    |}
    """.trimMargin().toByteArray()
        )

    }
}