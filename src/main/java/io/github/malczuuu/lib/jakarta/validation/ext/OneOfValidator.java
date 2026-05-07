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

import static java.util.stream.Collectors.toSet;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * Validator for the {@link OneOf} annotation.
 *
 * <p>Checks if the value of the annotated element is one of the specified allowed values.
 *
 * @since 1.0.0
 */
public class OneOfValidator implements ConstraintValidator<OneOf, Object> {

  private Set<String> values = Set.of();
  private Set<String> valuesIgnoreCase = Set.of();
  private boolean ignoreCase = false;

  /**
   * Initializes the validator by extracting the allowed values and case-sensitivity flag from the
   * annotation.
   *
   * @param constraintAnnotation the annotation instance for a given constraint declaration
   * @since 1.0.0
   */
  @Override
  public void initialize(OneOf constraintAnnotation) {
    if (constraintAnnotation.values().length > 0) {
      values = Set.of(constraintAnnotation.values());
    } else if (constraintAnnotation.enumType().isEnum()) {
      values =
          Arrays.stream(constraintAnnotation.enumType().getEnumConstants())
              .map(c -> ((Enum<?>) c).name())
              .collect(toSet());
    } else {
      values = Set.of();
    }
    valuesIgnoreCase = values.stream().map(s -> s.toLowerCase(Locale.ROOT)).collect(toSet());
    ignoreCase = constraintAnnotation.ignoreCase();
  }

  /**
   * Validates that the given value is one of the specified values in the annotation.
   *
   * <p>Supported types are:
   *
   * <ul>
   *   <li>{@code CharSequence} ({@code String} in particular, but also {@code StringBuilder} etc.)
   *   <li>{@code Enum}
   *   <li>{@code Number} (compared to the allowed values using {@code Number::toString})
   *   <li>{@code Character}
   * </ul>
   *
   * @param value the value to validate
   * @param context the context in which the constraint is evaluated
   * @return {@code true} if the value is valid, {@code false} otherwise
   * @throws IllegalArgumentException if the value type is not supported
   * @since 1.0.0
   */
  @Override
  public boolean isValid(@Nullable Object value, ConstraintValidatorContext context)
      throws IllegalArgumentException {
    if (value == null) {
      return true;
    }

    String valueAsString = toValidableString(value);

    return ignoreCase
        ? valuesIgnoreCase.contains(valueAsString.toLowerCase(Locale.ROOT))
        : values.contains(valueAsString);
  }

  /**
   * Converts the given value to a String suitable for validation.
   *
   * @param value the value to convert
   * @return the string representation of the value
   * @throws IllegalArgumentException if the value type is not supported
   */
  private String toValidableString(Object value) throws IllegalArgumentException {
    if (value instanceof CharSequence || value instanceof Number || value instanceof Character) {
      return value.toString();
    }
    if (value instanceof Enum<?>) {
      return ((Enum<?>) value).name();
    }
    throw new IllegalArgumentException(
        OneOf.class.getSimpleName() + " not supported for " + value.getClass().getName() + " type");
  }
}
