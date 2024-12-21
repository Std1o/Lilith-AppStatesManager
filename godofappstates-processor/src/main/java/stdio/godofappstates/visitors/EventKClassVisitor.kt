package stdio.godofappstates.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream

internal class EventKClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val dependencies: Dependencies,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val packageName = "godofappstates.domain"

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = "SingleEventFlow"
        )

        val content = this.javaClass.classLoader.getResource("SingleEventFlow.txt")?.readText()

        outputStream.write(
            ("""

    |package $packageName

    """.trimMargin() + "\n" + content).trimMargin().toByteArray()
        )

    }
}