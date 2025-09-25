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

class OneOfValidatorTest {

  private Validator validator;

  /**
   * Disables the INFO banner ("HV000001: Hibernate Validator ...") that Hibernate Validator logs
   * when the {@link org.hibernate.validator.internal.util.Version} class is initialized.
   *
   * <p>Hibernate Validator uses {@link java.util.logging} (JUL) for this particular message, so we
   * silence that specific logger before any {@code ValidatorFactory} is created.
   *
   * <p>This logging is irrelevant in unit tests.
   */
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

  private static class FieldBean {

    @OneOf(values = {"X", "Y", "Z"})
    private final String value;

    private FieldBean(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
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

  private static class FieldBeanIgnoreCase {

    @OneOf(
        values = {"X", "Y", "Z"},
        ignoreCase = true)
    private final String value;

    private FieldBeanIgnoreCase(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
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

  private enum TestEnum {
    A,
    B,
    C,
    X
  }

  private static class EnumBean {

    @OneOf(values = {"A", "B", "C"})
    private final TestEnum value;

    private EnumBean(TestEnum value) {
      this.value = value;
    }

    public TestEnum getValue() {
      return value;
    }
  }

  @Test
  void givenValidEnum_whenValidating_thenNoViolation() {
    EnumBean bean = new EnumBean(TestEnum.A);

    Set<ConstraintViolation<EnumBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidEnum_whenValidating_thenViolation() {
    EnumBean bean = new EnumBean(TestEnum.X);

    Set<ConstraintViolation<EnumBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  @Test
  void givenNullEnum_whenValidating_thenNoViolation() {
    EnumBean bean = new EnumBean(null);

    Set<ConstraintViolation<EnumBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static class EnumBeanIgnoreCase {

    @OneOf(
        values = {"A", "B", "C"},
        ignoreCase = true)
    private final TestEnum value;

    private EnumBeanIgnoreCase(TestEnum value) {
      this.value = value;
    }

    public TestEnum getValue() {
      return value;
    }
  }

  @Test
  void givenValidEnumIgnoreCase_whenValidating_thenNoViolation() {
    EnumBeanIgnoreCase bean = new EnumBeanIgnoreCase(TestEnum.A);

    Set<ConstraintViolation<EnumBeanIgnoreCase>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidEnumIgnoreCase_whenValidating_thenViolation() {
    EnumBeanIgnoreCase bean = new EnumBeanIgnoreCase(TestEnum.X);

    Set<ConstraintViolation<EnumBeanIgnoreCase>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  @Test
  void givenNullEnumIgnoreCase_whenValidating_thenNoViolation() {
    EnumBeanIgnoreCase bean = new EnumBeanIgnoreCase(null);

    Set<ConstraintViolation<EnumBeanIgnoreCase>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static class EnumCaseSensitiveBean {

    @OneOf(values = {"a"})
    private final TestEnum value;

    private EnumCaseSensitiveBean(TestEnum value) {
      this.value = value;
    }

    public TestEnum getValue() {
      return value;
    }
  }

  @Test
  void givenEnumCaseSensitiveMismatch_whenValidating_thenViolation() {
    EnumCaseSensitiveBean bean = new EnumCaseSensitiveBean(TestEnum.A);

    Set<ConstraintViolation<EnumCaseSensitiveBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  private static class EnumCaseInsensitiveBean {

    @OneOf(
        values = {"a"},
        ignoreCase = true)
    private final TestEnum value;

    private EnumCaseInsensitiveBean(TestEnum value) {
      this.value = value;
    }

    public TestEnum getValue() {
      return value;
    }
  }

  @Test
  void givenEnumCaseInsensitiveMatch_whenValidating_thenNoViolation() {
    EnumCaseInsensitiveBean bean = new EnumCaseInsensitiveBean(TestEnum.A);

    Set<ConstraintViolation<EnumCaseInsensitiveBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static class NumberBean {

    @OneOf(values = {"42"})
    private final Integer value;

    private NumberBean(Integer value) {
      this.value = value;
    }

    public Integer getValue() {
      return value;
    }
  }

  @Test
  void givenValidNumber_whenValidating_thenNoViolation() {
    NumberBean bean = new NumberBean(42);

    Set<ConstraintViolation<NumberBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidNumber_whenValidating_thenViolation() {
    NumberBean bean = new NumberBean(99);

    Set<ConstraintViolation<NumberBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  @Test
  void givenNullNumber_whenValidating_thenNoViolation() {
    NumberBean bean = new NumberBean(null);

    Set<ConstraintViolation<NumberBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static class CharBean {

    @OneOf(values = {"A"})
    private final Character value;

    private CharBean(Character value) {
      this.value = value;
    }

    public Character getValue() {
      return value;
    }
  }

  @Test
  void givenValidCharacter_whenValidating_thenNoViolation() {
    CharBean bean = new CharBean('A');

    Set<ConstraintViolation<CharBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidCharacter_whenValidating_thenViolation() {
    CharBean bean = new CharBean('Z');

    Set<ConstraintViolation<CharBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  @Test
  void givenNullCharacter_whenValidating_thenNoViolation() {
    CharBean bean = new CharBean(null);

    Set<ConstraintViolation<CharBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static class CharBeanIgnoreCase {

    @OneOf(
        values = {"A"},
        ignoreCase = true)
    private final Character value;

    private CharBeanIgnoreCase(Character value) {
      this.value = value;
    }

    public Character getValue() {
      return value;
    }
  }

  @Test
  void givenValidCharacterIgnoreCase_whenValidating_thenNoViolation() {
    CharBeanIgnoreCase bean = new CharBeanIgnoreCase('a');

    Set<ConstraintViolation<CharBeanIgnoreCase>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenInvalidCharacterIgnoreCase_whenValidating_thenViolation() {
    CharBeanIgnoreCase bean = new CharBeanIgnoreCase('z');

    Set<ConstraintViolation<CharBeanIgnoreCase>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  @Test
  void givenNullCharacterIgnoreCase_whenValidating_thenNoViolation() {
    CharBeanIgnoreCase bean = new CharBeanIgnoreCase(null);

    Set<ConstraintViolation<CharBeanIgnoreCase>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static class UnsupportedBean {

    @OneOf(values = {"A"})
    private final Object value;

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
    assertEquals("OneOf not supported for java.lang.Object type", e.getCause().getMessage());
  }

  private static class RepeatableBean {

    @OneOf(values = {"A", "B"})
    @OneOf(values = {"1", "2"})
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
    RepeatableBean bean = new RepeatableBean("X");

    Set<ConstraintViolation<RepeatableBean>> violations = validator.validate(bean);

    assertEquals(2, violations.size());
  }

  @Test
  void givenNullRepeatable_whenValidating_thenNoViolation() {
    RepeatableBean bean = new RepeatableBean(null);

    Set<ConstraintViolation<RepeatableBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static class EmptyValuesBean {

    @OneOf(values = {})
    private final String value;

    private EmptyValuesBean(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @Test
  void givenEmptyValues_whenValidatingNonNull_thenViolation() {
    EmptyValuesBean bean = new EmptyValuesBean("anything");

    Set<ConstraintViolation<EmptyValuesBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
  }

  @Test
  void givenEmptyValues_whenValidatingNull_thenNoViolation() {
    EmptyValuesBean bean = new EmptyValuesBean(null);

    Set<ConstraintViolation<EmptyValuesBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }
}
