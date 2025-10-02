package io.github.malczuuu.lib.jakarta.validation.ext;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validator for the {@link Alphanumeric} annotation.
 *
 * <p>Checks if the value of the annotated element is alphanumeric.
 */
public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, Object> {

  private static final Pattern PATTERN = Pattern.compile("^[a-z0-9]*$", Pattern.CASE_INSENSITIVE);

  private Set<Character> ignoredChars = Set.of();

  /**
   * Initializes the validator by compiling {@link Pattern} object for alphanumeric strings.
   *
   * @param constraintAnnotation the annotation instance for a given constraint declaration
   */
  @Override
  public void initialize(Alphanumeric constraintAnnotation) {
    if (!constraintAnnotation.ignoreChars().isEmpty()) {
      ignoredChars = new HashSet<>();
      for (char ch : constraintAnnotation.ignoreChars().toCharArray()) {
        ignoredChars.add(ch);
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
   */
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context)
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
      for (char ch : valueAsString.toCharArray()) {
        if (!ignoredChars.contains(ch)) {
          builder.append(ch);
        }
      }

      valueAsString = builder.toString();
    }
    return valueAsString;
  }
}
