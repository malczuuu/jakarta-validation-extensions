package io.github.malczuuu.lib.jakarta.validation.ext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AlphanumericValidatorTest {

  private Validator validator;

  @BeforeAll
  static void beforeAll() {
    Logger.getLogger("org.hibernate.validator").setLevel(Level.OFF);
  }

  @BeforeEach
  void beforeEach() {
    try (ValidatorFactory factory =
        Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  private static class StringBean {

    @Alphanumeric private final String value;

    private StringBean(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc123", "ABC123"})
  void givenValidString_whenValidating_thenNoViolation(String value) {
    StringBean bean = new StringBean(value);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc-123!", "ABC-123!"})
  void givenInvalidString_whenValidating_thenViolation(String value) {
    StringBean bean = new StringBean(value);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  @Test
  void givenNullString_whenValidating_thenNoViolation() {
    StringBean bean = new StringBean(null);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static class StringBeanWithIgnore {

    @Alphanumeric(ignoreChars = "-_ ")
    private final String value;

    private StringBeanWithIgnore(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @Test
  void givenStringWithIgnoredChars_whenValidating_thenNoViolation() {
    StringBeanWithIgnore bean = new StringBeanWithIgnore("abc-123_ ");

    Set<ConstraintViolation<StringBeanWithIgnore>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenStringWithNonIgnoredInvalidChars_whenValidating_thenViolation() {
    StringBeanWithIgnore bean = new StringBeanWithIgnore("abc-123!");

    Set<ConstraintViolation<StringBeanWithIgnore>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  private static class CharBean {

    @Alphanumeric private final Character value;

    private CharBean(Character value) {
      this.value = value;
    }

    public Character getValue() {
      return value;
    }
  }

  @Test
  void givenValidCharacter_whenValidating_thenNoViolation() {
    CharBean bean = new CharBean('a');

    Set<ConstraintViolation<CharBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidCharacter_whenValidating_thenViolation() {
    CharBean bean = new CharBean('#');

    Set<ConstraintViolation<CharBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  @Test
  void givenNullCharacter_whenValidating_thenNoViolation() {
    CharBean bean = new CharBean(null);

    Set<ConstraintViolation<CharBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  // ------------------ Repeatable annotations ------------------

  private static class RepeatableBean {

    @Alphanumeric
    @Alphanumeric(ignoreChars = "-_")
    private final String value;

    private RepeatableBean(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @Test
  void givenRepeatableAnnotations_whenValidating_thenBothEnforced() {
    RepeatableBean bean = new RepeatableBean("abc-123");

    Set<ConstraintViolation<RepeatableBean>> violations = validator.validate(bean);
    assertEquals(1, violations.size());

    RepeatableBean bean2 = new RepeatableBean("abc-123!");
    violations = validator.validate(bean2);
    assertEquals(2, violations.size());
  }

  private static class UnsupportedBean {

    @Alphanumeric private final Object value;

    private UnsupportedBean(Object value) {
      this.value = value;
    }

    public Object getValue() {
      return value;
    }
  }

  @Test
  void givenUnsupportedType_whenValidating_thenThrowsException() {
    UnsupportedBean bean = new UnsupportedBean(new Object());

    ValidationException e = assertThrows(ValidationException.class, () -> validator.validate(bean));

    assertTrue(e.getMessage().contains("Unexpected exception during isValid call"));
    assertInstanceOf(IllegalArgumentException.class, e.getCause());
    assertEquals("Alphanumeric not supported for java.lang.Object type", e.getCause().getMessage());
  }

  private static class EmptyStringBean {

    @Alphanumeric private final String value;

    private EmptyStringBean(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @Test
  void givenEmptyString_whenValidating_thenNoViolation() {
    EmptyStringBean bean = new EmptyStringBean("");

    Set<ConstraintViolation<EmptyStringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }
}
