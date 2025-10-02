package io.github.malczuuu.lib.jakarta.validation.ext;

import static java.util.stream.Collectors.toSet;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.Set;

/**
 * Validator for the {@link OneOf} annotation.
 *
 * <p>Checks if the value of the annotated element is one of the specified allowed values.
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
   */
  @Override
  public void initialize(OneOf constraintAnnotation) {
    values = Set.of(constraintAnnotation.values());
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
   *   <li>{@code Number} (compared to {@code values} with {@code Number::toString})
   *   <li>{@code Character}
   * </ul>
   *
   * @param value the value to validate
   * @param context the context in which the constraint is evaluated
   * @return {@code true} if the value is valid, {@code false} otherwise
   * @throws IllegalArgumentException if the value type is not supported
   */
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context)
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
