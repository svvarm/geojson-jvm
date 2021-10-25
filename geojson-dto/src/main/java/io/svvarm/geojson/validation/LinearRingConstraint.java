package io.svvarm.geojson.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.svvarm.geojson.geometry.Position;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * Constraint to validate linear ring has 4 at least positions and is explicitly closed.
 *
 * @since 1.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = LinearRingConstraint.Validator.class)
public @interface LinearRingConstraint {

  String message() default "must have 4 at least positions and be explicitly closed";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Validator for linear ring constraint.
   *
   * @since 1.0
   */
  final class Validator implements
      ConstraintValidator<LinearRingConstraint, List<? extends Position>> {

    @Override
    public boolean isValid(
        final List<? extends Position> value,
        final ConstraintValidatorContext context) {
      return value.size() >= 4 && value.get(0).equals(value.get(value.size() - 1));
    }
  }
}
