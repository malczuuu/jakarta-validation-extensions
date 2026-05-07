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
 * Annotation for validating that a {@code CharSequence} value is a valid hexadecimal string.
 *
 * <p>Each character must be in the range {@code 0–9}, {@code a–f}, or {@code A–F}. Mixed case is
 * accepted.
 *
 * <p>Accepted values include:
 *
 * <ul>
 *   <li>{@code ff00aa} - lowercase
 *   <li>{@code FF00AA} - uppercase
 *   <li>{@code deadBEEF} - mixed case
 * </ul>
 *
 * <p>Rejected values include:
 *
 * <ul>
 *   <li>{@code #ff00aa} - leading {@code #} is not accepted
 *   <li>{@code 0x1a} - {@code 0x} prefix is not accepted
 *   <li>{@code gg} - invalid hex characters
 * </ul>
 *
 * <p>Supported types are:
 *
 * <ul>
 *   <li>{@code CharSequence} ({@code String} in particular, but also {@code StringBuilder} etc.)
 * </ul>
 *
 * <p>Empty strings and {@code null} elements are considered valid.
 *
 * <p>Example usage:
 *
 * <pre>
 * &#064;Hex
 * private String checksum;
 * </pre>
 *
 * @since 1.2.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(Hex.List.class)
@Documented
@Constraint(validatedBy = HexValidator.class)
public @interface Hex {

  /**
   * Returns the error message template.
   *
   * @return the error message template, which can be a literal message or a message key in a
   *     resource bundle
   * @since 1.2.0
   */
  String message() default "must be a valid hexadecimal string";

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
   * Defines several {@link Hex} annotations on the same element.
   *
   * @see Hex
   * @since 1.2.0
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * Returns the contained {@link Hex} annotations.
     *
     * @return array of {@link Hex} annotations
     * @since 1.2.0
     */
    Hex[] value();
  }
}
