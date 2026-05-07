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
 * Annotation for validating that a {@code CharSequence} value is a valid ISO 8601 date string.
 *
 * <p>The value must be in the format {@code yyyy-MM-dd} with zero-padded month and day.
 *
 * <p>Accepted values include:
 *
 * <ul>
 *   <li>{@code 2024-01-15} - standard date
 *   <li>{@code 2024-12-31} - end of year
 * </ul>
 *
 * <p>Rejected values include:
 *
 * <ul>
 *   <li>{@code 2024-1-5} - non-padded month and day
 *   <li>{@code 2024-13-01} - invalid month
 *   <li>{@code 2024-01-15T10:30:00} - date-time, not a date
 * </ul>
 *
 * <p>Supported types are:
 *
 * <ul>
 *   <li>{@code CharSequence} ({@code String} in particular, but also {@code StringBuilder} etc.)
 * </ul>
 *
 * <p>{@code null} elements are considered valid.
 *
 * <p>Example usage:
 *
 * <pre>
 * &#064;IsoDate
 * private String birthDate;
 * </pre>
 *
 * @since 1.2.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(IsoDate.List.class)
@Documented
@Constraint(validatedBy = IsoDateValidator.class)
public @interface IsoDate {

  /**
   * Returns the error message template.
   *
   * @return the error message template, which can be a literal message or a message key in a
   *     resource bundle
   * @since 1.2.0
   */
  String message() default "must be a valid ISO 8601 date";

  /**
   * Returns the validation groups to which this constraint belongs.
   *
   * @return the validation groups to which this constraint belongs
   * @since 1.2.0
   */
  Class<?>[] groups() default {};

  /**
   * Returns the payload with which the constraint violation can be associated.
   *
   * @return the payload with which the constraint violation can be associated
   * @since 1.2.0
   */
  Class<? extends Payload>[] payload() default {};

  /**
   * Defines several {@link IsoDate} annotations on the same element.
   *
   * @see IsoDate
   * @since 1.2.0
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * Returns the contained {@link IsoDate} annotations.
     *
     * @return array of {@link IsoDate} annotations
     * @since 1.2.0
     */
    IsoDate[] value();
  }
}
