package stdio.godofappstates.core.domain

/**
 * To solve collisions when using multiple operations on screen,
 * the result of which UI should react differently
 */
interface OperationType {
    data object DefaultOperation : OperationType
}