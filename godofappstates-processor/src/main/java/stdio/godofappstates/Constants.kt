package stdio.godofappstates

object Constants {
    const val OPERATION_MUST_BE_AN_INTERFACE =
        "||Class with the OperationState annotation must be an interface"
    const val LOADABLE_MUST_BE_AN_INTERFACE =
        "||Class with the LoadableData annotation must be an interface"
    const val NO_OPERATION_STATE_ANNOTATION = "||@OperationState annotation not found.\n" +
            "If you don't need an extended OperationState yet, just create an empty sealed interface and mark it with an annotation. " +
            "You can delete it as soon as a useful custom OperationState appears.\n\n" +
            "Example:\n" +
            "@OperationState\n" +
            "sealed interface OperationStateTrigger<out R>"
    const val NO_LOADABLE_DATA_ANNOTATION = "||@LoadableData annotation not found.\n" +
            "If you don't need an extended LoadableData yet, just create an empty sealed interface and mark it with an annotation. " +
            "You can delete it as soon as a useful custom LoadableData appears.\n\n" +
            "Example:\n" +
            "@LoadableData\n" +
            "sealed interface LoadableDataTrigger<out R>"
}