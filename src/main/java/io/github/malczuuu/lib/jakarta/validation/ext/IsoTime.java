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
 * Annotation for validating that a {@code CharSequence} value is a valid ISO 8601 time string.
 *
 * <p>The value must contain at least hours and minutes. Seconds and fractional seconds are
 * optional. A timezone offset is accepted but optional unless {@link #offsetRequired} is set to
 * {@code true}.
 *
 * <p>Accepted values include:
 *
 * <ul>
 *   <li>{@code 10:30} - hours and minutes
 *   <li>{@code 10:30:00} - with seconds
 *   <li>{@code 10:30:00.123} - with fractional seconds
 *   <li>{@code 10:30:00Z} - with UTC offset
 *   <li>{@code 10:30:00+05:30} - with timezone offset
 * </ul>
 *
 * <p>Rejected values include:
 *
 * <ul>
 *   <li>{@code 25:00:00} - invalid hour
 *   <li>{@code 2024-01-15T10:30:00} - date-time, not a time
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
 * <p>Example usages:
 *
 * <pre>
 * // Any ISO 8601 time, offset optional
 * &#064;IsoTime
 * private String openingTime;
 *
 * // Require a timezone offset, e.g. "10:30:00Z"
 * &#064;IsoTime(offsetRequired = true)
 * private String meetingTime;
 * </pre>
 *
 * @since 1.2.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(IsoTime.List.class)
@Documented
@Constraint(validatedBy = IsoTimeValidator.class)
public @interface IsoTime {

  /**
   * Returns the error message template.
   *
   * @return the error message template, which can be a literal message or a message key in a
   *     resource bundle
   * @since 1.2.0
   */
  String message() default "must be a valid ISO 8601 time";

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
   * Returns whether a timezone offset is required.
   *
   * @return {@code true} if a timezone offset (e.g. {@code Z} or {@code +05:30}) is required,
   *     {@code false} to allow times without an offset
   * @since 1.2.0
   */
  boolean offsetRequired() default false;

  /**
   * Defines several {@link IsoTime} annotations on the same element.
   *
   * @see IsoTime
   * @since 1.2.0
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * Returns the contained {@link IsoTime} annotations.
     *
     * @return array of {@link IsoTime} annotations
     * @since 1.2.0
     */
    IsoTime[] value();
  }
}
