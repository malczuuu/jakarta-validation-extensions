package io.github.malczuuu.lib.jakarta.validation.ext;

import static java.util.stream.Collectors.toSet;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.Set;

public class OneOfValidator implements ConstraintValidator<OneOf, Object> {

  private Set<String> values = Set.of();
  private Set<String> valuesIgnoreCase = Set.of();

  private boolean ignoreCase = false;

  @Override
  public void initialize(OneOf constraintAnnotation) {
    values = Set.of(constraintAnnotation.values());
    valuesIgnoreCase = values.stream().map(s -> s.toLowerCase(Locale.ROOT)).collect(toSet());
    ignoreCase = constraintAnnotation.ignoreCase();
  }

  /**
   * Validates that the given value is one of the specified values in the annotation. Supported
   * types are String, Enum, Number and Character.
   *
   * @param value the value to validate
   * @param context the context in which the constraint is evaluated
   * @return true if the value is valid, false otherwise
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

  private String toValidableString(Object value) throws IllegalArgumentException {
    if (value instanceof String) {
      return (String) value;
    }
    if (value instanceof Enum<?>) {
      return ((Enum<?>) value).name();
    }
    if (value instanceof Number || value instanceof Character) {
      return value.toString();
    }
    throw new IllegalArgumentException(
        OneOf.class.getSimpleName() + " not supported for " + value.getClass() + " type");
  }
}
