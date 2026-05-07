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
 * Annotation for validating that a {@code CharSequence} value is a syntactically valid BCP 47
 * language tag.
 *
 * <p>Syntactic validity is checked per BCP 47; individual subtag values are not verified against
 * the IANA language subtag registry.
 *
 * <p>Accepted values include:
 *
 * <ul>
 *   <li>{@code en} - English
 *   <li>{@code en-US} - English as used in the United States
 *   <li>{@code zh-Hant} - Traditional Chinese
 *   <li>{@code sr-Latn-RS} - Serbian in Latin script as used in Serbia
 * </ul>
 *
 * <p>Rejected values include:
 *
 * <ul>
 *   <li>{@code en US} - space is not a valid separator
 *   <li>{@code en!} - invalid characters
 *   <li>{@code en--US} - empty subtag between separators
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
 * &#064;LanguageTag
 * private String locale;
 * </pre>
 *
 * @since 1.2.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(LanguageTag.List.class)
@Documented
@Constraint(validatedBy = LanguageTagValidator.class)
public @interface LanguageTag {

  /**
   * Returns the error message template.
   *
   * @return the error message template, which can be a literal message or a message key in a
   *     resource bundle
   * @since 1.2.0
   */
  String message() default "must be a valid language tag";

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
   * Defines several {@link LanguageTag} annotations on the same element.
   *
   * @see LanguageTag
   * @since 1.2.0
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    /**
     * Returns the contained {@link LanguageTag} annotations.
     *
     * @return array of {@link LanguageTag} annotations
     * @since 1.2.0
     */
    LanguageTag[] value();
  }
}
