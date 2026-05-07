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
 * Annotation for validating that a {@code CharSequence} value is a valid semantic version string as
 * defined by <a href="https://semver.org/">semver.org</a>.
 *
 * <p>The format is {@code MAJOR.MINOR.PATCH} with optional pre-release and build-metadata
 * identifiers. Leading zeros in numeric version parts are not allowed.
 *
 * <p>Accepted values include:
 *
 * <ul>
 *   <li>{@code 1.0.0}
 *   <li>{@code 1.2.3-alpha.1} - with pre-release identifier
 *   <li>{@code 1.2.3+build.42} - with build metadata
 *   <li>{@code 1.2.3-beta.1+build.42} - with both
 * </ul>
 *
 * <p>Rejected values include:
 *
 * <ul>
 *   <li>{@code 1.0} - missing patch version
 *   <li>{@code v1.0.0} - leading {@code v} is not part of the specification
 *   <li>{@code 01.0.0} - leading zeros not allowed in version numbers
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
 * &#064;SemVer
 * private String version;
 * </pre>
 *
 * @since 1.2.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(SemVer.List.class)
@Documented
@Constraint(validatedBy = SemVerValidator.class)
public @interface SemVer {

  /**
   * Returns the error message template.
   *
   * @return the error message template, which can be a literal message or a message key in a
   *     resource bundle
   * @since 1.2.0
   */
  String message() default "must be a valid semantic version string";

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
   * Defines several {@link SemVer} annotations on the same element.
   *
   * @see SemVer
   * @since 1.2.0
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * Returns the contained {@link SemVer} annotations.
     *
     * @return array of {@link SemVer} annotations
     * @since 1.2.0
     */
    SemVer[] value();
  }
}
