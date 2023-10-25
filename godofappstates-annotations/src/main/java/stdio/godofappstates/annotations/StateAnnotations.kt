package stdio.godofappstates.annotations

/**
 * It is the domain-level state. It can be used in the presentation layer, but it cannot be formed there.
 *
 * As a rule, it is formed in UseCase.
 * Or it is LoadableData or OperationState, which are identical in structure, but different in semantics.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE_PARAMETER)
@MustBeDocumented
annotation class FunctionalityState

@Target(AnnotationTarget.CLASS)
annotation class OperationState

@Target(AnnotationTarget.CLASS)
annotation class LoadableData

/**
 * It is the presentation-level state. It must be formed and must be used only in presentation layer.
 */
@MustBeDocumented
annotation class ContentState

/**
 * The ScreenSession is used for those cases when rememberSaveable can't cope.
 * Or if you plan to store the state of some screen in the database in the future.
 *
 * The ScreenSession is formed and used on the presentation layer.
 * It is not connected to other layers
 */
@MustBeDocumented
annotation class ScreenSession

/**
 * For states from UseCase. It is marked if loader should be still shown
 */
@MustBeDocumented
annotation class StillLoading

@Target(AnnotationTarget.CLASS)
annotation class AllStatesReadyToUse