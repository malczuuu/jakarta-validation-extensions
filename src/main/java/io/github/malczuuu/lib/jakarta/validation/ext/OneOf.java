package io.github.malczuuu.lib.jakarta.validation.ext;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for validating that a value is one of a specified set of allowed values.
 *
 * <p>Supported types are:
 *
 * <ul>
 *   <li>{@code CharSequence} ({@code String} in particular, but also {@code StringBuilder} etc.)
 *   <li>{@code Enum}
 *   <li>{@code Number} (compared to {@code values} with {@code Number::toString})
 *   <li>{@code Character}
 * </ul>
 *
 * <p>{@code null} elements are considered valid.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(OneOf.List.class)
@Documented
@Constraint(validatedBy = OneOfValidator.class)
public @interface OneOf {

  String message() default "must be one of {values}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /** Returns: allowed values for the annotated element */
  String[] values();

  /** Returns: whether to ignore case when validating {@code String} or {@code Enum} values. */
  boolean ignoreCase() default false;

  /**
   * Defines several {@link OneOf} annotations on the same element.
   *
   * @see OneOf
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * @return array of {@link OneOf} annotations
     */
    OneOf[] value();
  }
}
