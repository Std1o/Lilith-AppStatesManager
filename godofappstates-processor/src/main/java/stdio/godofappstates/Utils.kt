package stdio.godofappstates

import com.google.devtools.ksp.symbol.KSAnnotated

object Utils {
    internal fun getStrParents(annotatedClasses: Sequence<KSAnnotated>): String {
        val parents = annotatedClasses.toList().map { it.toString() }
        var strParents = ""
        parents.forEach { strParents += "$it<R>, " }
        strParents = strParents.dropLast(2)
        return strParents
    }
}