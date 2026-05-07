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
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;

/**
 * Validator for the {@link Alphanumeric} annotation.
 *
 * <p>Checks if the value of the annotated element is alphanumeric.
 *
 * @since 1.1.0
 */
public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, Object> {

  private static final Pattern PATTERN = Pattern.compile("^[a-z0-9]*$", Pattern.CASE_INSENSITIVE);

  private Set<Character> ignoredChars = Set.of();

  /**
   * Initializes the validator by compiling {@link Pattern} object for alphanumeric strings.
   *
   * @param constraintAnnotation the annotation instance for a given constraint declaration
   * @since 1.1.0
   */
  @Override
  public void initialize(Alphanumeric constraintAnnotation) {
    if (!constraintAnnotation.ignoreChars().isEmpty()) {
      ignoredChars = new HashSet<>();
      String ignoreChars = constraintAnnotation.ignoreChars();
      for (int i = 0; i < ignoreChars.length(); i++) {
        ignoredChars.add(ignoreChars.charAt(i));
      }
    }
  }

  /**
   * Validates that the given value is alphanumeric.
   *
   * <p>Supported types are:
   *
   * <ul>
   *   <li>{@code CharSequence} ({@code String} in particular, but also {@code StringBuilder} etc.)
   *   <li>{@code Character}
   * </ul>
   *
   * @param value the value to validate
   * @param context the context in which the constraint is evaluated
   * @return {@code true} if the value is valid, {@code false} otherwise
   * @throws IllegalArgumentException if the value type is not supported
   * @since 1.1.0
   */
  @Override
  public boolean isValid(@Nullable Object value, ConstraintValidatorContext context)
      throws IllegalArgumentException {
    if (value == null) {
      return true;
    }

    String valueAsString = toValidableString(value);
    valueAsString = removeIgnoredChars(valueAsString);

    if (valueAsString.isEmpty()) {
      return true;
    }

    return PATTERN.matcher(valueAsString).matches();
  }

  /**
   * Converts the given value to a String suitable for validation.
   *
   * @param value the value to convert
   * @return the string representation of the value
   * @throws IllegalArgumentException if the value type is not supported
   */
  private String toValidableString(Object value) throws IllegalArgumentException {
    if (value instanceof CharSequence || value instanceof Character) {
      return value.toString();
    }
    throw new IllegalArgumentException(
        Alphanumeric.class.getSimpleName()
            + " not supported for "
            + value.getClass().getName()
            + " type");
  }

  /**
   * Removes all characters from the given string that are present in the {@link #ignoredChars} set.
   *
   * <p>If {@code ignoredChars} is empty, the original string is returned unchanged. Otherwise, each
   * character in the input string is checked, and only characters not in {@code ignoredChars} are
   * included in the result.
   *
   * @param valueAsString the input string from which to remove ignored characters
   * @return a new string with all ignored characters removed
   */
  private String removeIgnoredChars(String valueAsString) {
    if (!ignoredChars.isEmpty()) {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < valueAsString.length(); i++) {
        char ch = valueAsString.charAt(i);
        if (!ignoredChars.contains(ch)) {
          builder.append(ch);
        }
      }

      valueAsString = builder.toString();
    }
    return valueAsString;
  }
}
