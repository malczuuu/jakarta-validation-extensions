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
 * Annotation for validating that a value is alphanumeric.
 *
 * <p>Supported types are:
 *
 * <ul>
 *   <li>{@code CharSequence} ({@code String} in particular, but also {@code StringBuilder} etc.)
 *   <li>{@code Character}
 * </ul>
 *
 * <p>{@code null} elements are considered valid.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(Alphanumeric.List.class)
@Documented
@Constraint(validatedBy = AlphanumericValidator.class)
public @interface Alphanumeric {

  String message() default "must be alphanumeric";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Returns: characters to ignore during validation, declared as {@link String} instead of {@code
   * char[]} for simplicity.
   */
  String ignoreChars() default "";

  /**
   * Defines several {@link Alphanumeric} annotations on the same element.
   *
   * @see Alphanumeric
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * @return array of {@link Alphanumeric} annotations
     */
    Alphanumeric[] value();
  }
}
