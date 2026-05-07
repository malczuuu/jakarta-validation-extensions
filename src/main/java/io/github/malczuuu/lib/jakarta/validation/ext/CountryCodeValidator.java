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

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * Validator for the {@link CountryCode} annotation.
 *
 * <p>Checks if the value of the annotated element is a recognized ISO 3166-1 alpha-2 country code.
 *
 * @since 1.2.0
 */
public class CountryCodeValidator implements ConstraintValidator<CountryCode, Object> {

  private static final Set<String> ISO_COUNTRIES = Set.of(Locale.getISOCountries());

  private boolean ignoreCase;

  /**
   * Initializes the validator with the constraint annotation attributes.
   *
   * @param constraintAnnotation the annotation instance for a given constraint declaration
   * @since 1.2.0
   */
  @Override
  public void initialize(CountryCode constraintAnnotation) {
    ignoreCase = constraintAnnotation.ignoreCase();
  }

  /**
   * Validates that the given value is a recognized ISO 3166-1 alpha-2 country code.
   *
   * <p>Supported types are:
   *
   * <ul>
   *   <li>{@code CharSequence} ({@code String} in particular, but also {@code StringBuilder} etc.)
   * </ul>
   *
   * @param value the value to validate
   * @param context the context in which the constraint is evaluated
   * @return {@code true} if the value is valid, {@code false} otherwise
   * @throws IllegalArgumentException if the value type is not supported
   * @since 1.2.0
   */
  @Override
  public boolean isValid(@Nullable Object value, ConstraintValidatorContext context)
      throws IllegalArgumentException {
    if (value == null) {
      return true;
    }
    if (!(value instanceof CharSequence)) {
      throw new IllegalArgumentException(
          CountryCode.class.getSimpleName()
              + " not supported for "
              + value.getClass().getName()
              + " type");
    }
    String str = value.toString();
    if (ignoreCase) {
      str = str.toUpperCase(Locale.ROOT);
    }
    return ISO_COUNTRIES.contains(str);
  }
}
