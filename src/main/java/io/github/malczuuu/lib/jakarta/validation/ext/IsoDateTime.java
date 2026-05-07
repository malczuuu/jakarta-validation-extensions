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
 * Annotation for validating that a {@code CharSequence} value is a valid ISO 8601 date-time string.
 *
 * <p>The value must contain at least a date and a time part separated by {@code T}. A timezone
 * offset is accepted but optional unless {@link #offsetRequired} is set to {@code true}.
 *
 * <p>Accepted values include:
 *
 * <ul>
 *   <li>{@code 2024-01-15T10:30:00} - local date-time, no offset
 *   <li>{@code 2024-01-15T10:30:00Z} - UTC
 *   <li>{@code 2024-01-15T10:30:00+05:30} - with timezone offset
 *   <li>{@code 2024-01-15T10:30:00.123Z} - with fractional seconds
 * </ul>
 *
 * <p>Rejected values include:
 *
 * <ul>
 *   <li>{@code 2024-01-15} - date only, missing time component
 *   <li>{@code 2024-01-15 10:30:00} - space instead of {@code T}
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
 * // Any ISO 8601 date-time, offset optional
 * &#064;IsoDateTime
 * private String createdAt;
 *
 * // Require a timezone offset, e.g. "2024-01-15T10:30:00Z"
 * &#064;IsoDateTime(offsetRequired = true)
 * private String updatedAt;
 * </pre>
 *
 * @since 1.2.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(IsoDateTime.List.class)
@Documented
@Constraint(validatedBy = IsoDateTimeValidator.class)
public @interface IsoDateTime {

  /**
   * Returns the error message template.
   *
   * @return the error message template, which can be a literal message or a message key in a
   *     resource bundle
   * @since 1.2.0
   */
  String message() default "must be a valid ISO 8601 date-time";

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
   *     {@code false} to allow date-times without an offset
   * @since 1.2.0
   */
  boolean offsetRequired() default false;

  /**
   * Defines several {@link IsoDateTime} annotations on the same element.
   *
   * @see IsoDateTime
   * @since 1.2.0
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * Returns the contained {@link IsoDateTime} annotations.
     *
     * @return array of {@link IsoDateTime} annotations
     * @since 1.2.0
     */
    IsoDateTime[] value();
  }
}
