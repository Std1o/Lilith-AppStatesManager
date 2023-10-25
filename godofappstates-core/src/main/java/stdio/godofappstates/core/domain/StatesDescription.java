package stdio.godofappstates.core.domain;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

/**
 * Why is there such a strange inheritance hierarchy?
 * <p>
 * Subtype polymorphism: program elements (typically subroutines or functions),
 * written to operate on elements of the supertype, can also operate on elements of the subtype
 * <p>
 * Example:
 * <p>
 * <img width="741" height="427" src="https://i.ibb.co/DLgpbpr/states-hierarchy.png" alt="">
 */
@Documented
@Retention(value=SOURCE)
@interface StatesDescription {

}