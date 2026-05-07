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
 * Annotation for validating that a {@code CharSequence} value is a recognized ISO 3166-1 alpha-2
 * country code (e.g. {@code US}, {@code PL}, {@code DE}).
 *
 * <p>The recognized codes are sourced from the JDK via {@link java.util.Locale#getISOCountries()}.
 * By default, matching is case-sensitive and codes must be in canonical uppercase form.
 *
 * <p>Accepted values include:
 *
 * <ul>
 *   <li>{@code US} - United States
 *   <li>{@code PL} - Poland
 *   <li>{@code DE} - Germany
 * </ul>
 *
 * <p>Rejected values include:
 *
 * <ul>
 *   <li>{@code us} - lowercase (unless {@link #ignoreCase} is {@code true})
 *   <li>{@code XX} - not a recognized country code
 *   <li>{@code USA} - three-letter code, not alpha-2
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
 * // Strict uppercase matching
 * &#064;CountryCode
 * private String country;
 *
 * // Accept lowercase codes as well
 * &#064;CountryCode(ignoreCase = true)
 * private String country;
 * </pre>
 *
 * @since 1.2.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(CountryCode.List.class)
@Documented
@Constraint(validatedBy = CountryCodeValidator.class)
public @interface CountryCode {

  /**
   * Returns the error message template.
   *
   * @return the error message template, which can be a literal message or a message key in a
   *     resource bundle
   * @since 1.2.0
   */
  String message() default "must be a valid country code";

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
   * Returns whether to ignore case when matching the country code.
   *
   * @return whether to ignore case when matching the country code, {@code false} requires canonical
   *     uppercase (e.g. {@code US})
   * @since 1.2.0
   */
  boolean ignoreCase() default false;

  /**
   * Defines several {@link CountryCode} annotations on the same element.
   *
   * @see CountryCode
   * @since 1.2.0
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * Returns the contained {@link CountryCode} annotations.
     *
     * @return array of {@link CountryCode} annotations
     * @since 1.2.0
     */
    CountryCode[] value();
  }
}
