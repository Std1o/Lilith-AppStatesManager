package stdio.godofappstates.core.util

import kotlin.reflect.KType

// TODO обновить KDoc
/**
 * 1.) Make sure that you are using RequestState or his parent in your UseCase or Repository
 *
 * 2.) Call [student.testing.system.presentation.viewmodels.protect]
 *
 * on your calling of [student.testing.system.presentation.viewmodels.StatesViewModel.executeOperation]
 *
 * Explanation:
 *
 * Kotlin compiler may do wrong auto cast of fun's generics inside coroutine if there is not at least one of this:
 *
 * 1.) val/var for result of method
 *
 * 2.) Any val/var after calling method
 *
 * 3.) Explicit generics with fun's calling
 *
 * 4.) Located inside async {}
 *
 * [Watch issue](https://github.com/Kotlin/kotlinx.coroutines/issues/3904)
 */
class WrongGenericsAutoCastException : RuntimeException(
    "\n1.) If passed method returns Flow, make sure that you are calling .collect{}\n" +
            "2.) You can create val with result of this method, if you need to. It will help\n" +
            "3.) Else call protect() extension on your calling"
)

class NoOperationStateFoundException(kType: KType?) : RuntimeException(
    "\nExpected: OperationState or OperationState superclass\n" +
            "Found: $kType\n\n" +
            "Please make sure that you use sealed interface marked with annotation @OperationState\n\n"
)

class NoFlowOfOperationStateFoundException(kType: KType?) : RuntimeException(
    "\nExpected: Flow of OperationState or OperationState superclass\n" +
            "Found: $kType\n\n" +
            "Please make sure that you use Flow of sealed interface marked with annotation @OperationState\n\n"
)

class InvalidArgumentException(expected: String, found: KType?, advice: String = "") :
    RuntimeException(
        "" +
                "\nExpected: $expected" +
                "\nFound: ${found?.classifier}" +
                "\n $advice"
    )