package io.github.malczuuu.lib.jakarta.validation.ext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OneOfValidatorTest {

  private static Validator validator;

  @BeforeAll
  static void beforeAll() {
    try (ValidatorFactory factory =
        Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @Test
  void givenValidValueOnGetter_whenValidating_thenNoViolation() {
    GetterBean bean = new GetterBean("A");

    Set<ConstraintViolation<GetterBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidValueOnGetter_whenValidating_thenViolation() {
    GetterBean bean = new GetterBean("X");

    Set<ConstraintViolation<GetterBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("value", violations.iterator().next().getPropertyPath().toString());
    assertEquals("must be one of [A, B, C]", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullValueOnGetter_whenValidating_thenNoViolation() {
    GetterBean bean = new GetterBean(null);

    Set<ConstraintViolation<GetterBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenValidValueOnField_whenValidating_thenNoViolation() {
    FieldBean bean = new FieldBean("Z");

    Set<ConstraintViolation<FieldBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidValueOnField_whenValidating_thenViolation() {
    FieldBean bean = new FieldBean("A");

    Set<ConstraintViolation<FieldBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("value", violations.iterator().next().getPropertyPath().toString());
    assertEquals("must be one of [X, Y, Z]", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullValueOnField_whenValidating_thenNoViolation() {
    FieldBean bean = new FieldBean(null);

    Set<ConstraintViolation<FieldBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenValidValueOnGetterIgnoreCase_whenValidating_thenNoViolation() {
    GetterBeanIgnoreCase bean = new GetterBeanIgnoreCase("a");

    Set<ConstraintViolation<GetterBeanIgnoreCase>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidValueOnGetterIgnoreCase_whenValidating_thenViolation() {
    GetterBeanIgnoreCase bean = new GetterBeanIgnoreCase("X");

    Set<ConstraintViolation<GetterBeanIgnoreCase>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("value", violations.iterator().next().getPropertyPath().toString());
    assertEquals("must be one of [A, B, C]", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullValueOnGetterIgnoreCase_whenValidating_thenNoViolation() {
    GetterBeanIgnoreCase bean = new GetterBeanIgnoreCase(null);

    Set<ConstraintViolation<GetterBeanIgnoreCase>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenValidValueOnFieldIgnoreCase_whenValidating_thenNoViolation() {
    FieldBeanIgnoreCase bean = new FieldBeanIgnoreCase("y");

    Set<ConstraintViolation<FieldBeanIgnoreCase>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidValueOnFieldIgnoreCase_whenValidating_thenViolation() {
    FieldBeanIgnoreCase bean = new FieldBeanIgnoreCase("A");

    Set<ConstraintViolation<FieldBeanIgnoreCase>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("value", violations.iterator().next().getPropertyPath().toString());
    assertEquals("must be one of [X, Y, Z]", violations.iterator().next().getMessage());
  }

  private static class GetterBean {

    private final String value;

    private GetterBean(String value) {
      this.value = value;
    }

    @OneOf(values = {"A", "B", "C"})
    public String getValue() {
      return value;
    }
  }

  private static class FieldBean {

    @OneOf(values = {"X", "Y", "Z"})
    private final String value;

    private FieldBean(String value) {
      this.value = value;
    }
  }

  private static class GetterBeanIgnoreCase {
    private final String value;

    private GetterBeanIgnoreCase(String value) {
      this.value = value;
    }

    @OneOf(
        values = {"A", "B", "C"},
        ignoreCase = true)
    public String getValue() {
      return value;
    }
  }

  private static class FieldBeanIgnoreCase {

    @OneOf(
        values = {"X", "Y", "Z"},
        ignoreCase = true)
    private final String value;

    private FieldBeanIgnoreCase(String value) {
      this.value = value;
    }
  }
}
