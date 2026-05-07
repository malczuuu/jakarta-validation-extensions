/*
 * Copyright 2025-2026 Damian Malczewski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 *
 * <p>Example usages:
 *
 * <pre>
 * // Accept letters and digits only
 * &#064;Alphanumeric
 * private String username;
 *
 * // Also allow hyphens and underscores
 * &#064;Alphanumeric(ignoreChars = "-_")
 * private String slug;
 * </pre>
 *
 * @since 1.1.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(Alphanumeric.List.class)
@Documented
@Constraint(validatedBy = AlphanumericValidator.class)
public @interface Alphanumeric {

  /**
   * Returns the error message template.
   *
   * @return the error message template, which can be a literal message or a message key in a
   *     resource bundle
   * @since 1.1.0
   */
  String message() default "must be alphanumeric";

  /**
   * Returns the validation groups to which this constraint belongs.
   *
   * @return the validation groups to which this constraint belongs
   * @since 1.1.0
   */
  Class<?>[] groups() default {};

  /**
   * Returns the payload with which the constraint violation can be associated.
   *
   * @return the payload associated with this constraint
   * @since 1.1.0
   */
  Class<? extends Payload>[] payload() default {};

  /**
   * Returns the characters to ignore during validation.
   *
   * @return characters to ignore during validation, declared as {@link String} instead of {@code
   *     char[]} for simplicity.
   * @since 1.1.0
   */
  String ignoreChars() default "";

  /**
   * Defines several {@link Alphanumeric} annotations on the same element.
   *
   * @see Alphanumeric
   * @since 1.1.0
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * Returns the contained {@link Alphanumeric} annotations.
     *
     * @return array of {@link Alphanumeric} annotations
     * @since 1.1.0
     */
    Alphanumeric[] value();
  }
}
