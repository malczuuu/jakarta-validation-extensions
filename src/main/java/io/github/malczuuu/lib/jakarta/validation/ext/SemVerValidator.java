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
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;

/**
 * Validator for the {@link SemVer} annotation.
 *
 * <p>Checks if the value of the annotated element is a valid semantic version string per
 * semver.org.
 *
 * @since 1.2.0
 */
public class SemVerValidator implements ConstraintValidator<SemVer, Object> {

  // Official regex from https://semver.org/#is-there-a-suggested-regexp-for-semver
  private static final Pattern PATTERN =
      Pattern.compile(
          "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)"
              + "(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)"
              + "(?:\\.(0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?"
              + "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$");

  /**
   * Validates that the given value is a valid semantic version string.
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
          SemVer.class.getSimpleName()
              + " not supported for "
              + value.getClass().getName()
              + " type");
    }
    return PATTERN.matcher((CharSequence) value).matches();
  }
}
