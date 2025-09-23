package io.github.malczuuu.lib.jakarta.validation.ext;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class OneOfValidator implements ConstraintValidator<OneOf, String> {

  private String[] values = {};
  private boolean ignoreCase = false;

  @Override
  public void initialize(OneOf constraintAnnotation) {
    values = constraintAnnotation.values();
    ignoreCase = constraintAnnotation.ignoreCase();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    return ignoreCase
        ? Arrays.stream(values).anyMatch(v -> v.equalsIgnoreCase(value))
        : Arrays.asList(values).contains(value);
  }
}
