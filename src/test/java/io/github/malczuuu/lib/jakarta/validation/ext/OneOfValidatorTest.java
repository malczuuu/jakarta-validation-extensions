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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OneOfValidatorTest {

  private Validator validator;

  @BeforeEach
  void beforeEach() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  private static final class GetterBean {

    private final @Nullable String value;

    private GetterBean(@Nullable String value) {
      this.value = value;
    }

    @OneOf(values = {"A", "B", "C"})
    public @Nullable String getValue() {
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

  private static final class FieldBean {

    @OneOf(values = {"X", "Y", "Z"})
    private final @Nullable String value;

    private FieldBean(@Nullable String value) {
      this.value = value;
    }

    public @Nullable String getValue() {
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

  private static final class GetterBeanIgnoreCase {

    private final @Nullable String value;

    private GetterBeanIgnoreCase(@Nullable String value) {
      this.value = value;
    }

    @OneOf(
        values = {"A", "B", "C"},
        ignoreCase = true)
    public @Nullable String getValue() {
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

  private static final class FieldBeanIgnoreCase {

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

  private static final class EnumBean {

    @OneOf(values = {"A", "B", "C"})
    private final @Nullable TestEnum value;

    private EnumBean(@Nullable TestEnum value) {
      this.value = value;
    }

    public @Nullable TestEnum getValue() {
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
    assertEquals("must be one of [A, B, C]", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullEnum_whenValidating_thenNoViolation() {
    EnumBean bean = new EnumBean(null);

    Set<ConstraintViolation<EnumBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static final class EnumBeanIgnoreCase {

    @OneOf(
        values = {"A", "B", "C"},
        ignoreCase = true)
    private final @Nullable TestEnum value;

    private EnumBeanIgnoreCase(@Nullable TestEnum value) {
      this.value = value;
    }

    public @Nullable TestEnum getValue() {
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
    assertEquals("must be one of [A, B, C]", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullEnumIgnoreCase_whenValidating_thenNoViolation() {
    EnumBeanIgnoreCase bean = new EnumBeanIgnoreCase(null);

    Set<ConstraintViolation<EnumBeanIgnoreCase>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static final class EnumCaseSensitiveBean {

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
    assertEquals("must be one of [a]", violations.iterator().next().getMessage());
  }

  private static final class EnumCaseInsensitiveBean {

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

  private static final class NumberBean {

    @OneOf(values = {"42"})
    private final @Nullable Integer value;

    private NumberBean(@Nullable Integer value) {
      this.value = value;
    }

    public @Nullable Integer getValue() {
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
    assertEquals("must be one of [42]", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullNumber_whenValidating_thenNoViolation() {
    NumberBean bean = new NumberBean(null);

    Set<ConstraintViolation<NumberBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static final class CharBean {

    @OneOf(values = {"A"})
    private final @Nullable Character value;

    private CharBean(@Nullable Character value) {
      this.value = value;
    }

    public @Nullable Character getValue() {
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
    assertEquals("must be one of [A]", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullCharacter_whenValidating_thenNoViolation() {
    CharBean bean = new CharBean(null);

    Set<ConstraintViolation<CharBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static final class CharBeanIgnoreCase {

    @OneOf(
        values = {"A"},
        ignoreCase = true)
    private final @Nullable Character value;

    private CharBeanIgnoreCase(@Nullable Character value) {
      this.value = value;
    }

    public @Nullable Character getValue() {
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
    assertEquals("must be one of [A]", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullCharacterIgnoreCase_whenValidating_thenNoViolation() {
    CharBeanIgnoreCase bean = new CharBeanIgnoreCase(null);

    Set<ConstraintViolation<CharBeanIgnoreCase>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static final class UnsupportedBean {

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

    assertNotNull(e.getMessage());
    assertTrue(e.getMessage().contains("Unexpected exception during isValid call"));
    assertInstanceOf(IllegalArgumentException.class, e.getCause());
    assertEquals("OneOf not supported for java.lang.Object type", e.getCause().getMessage());
  }

  private static final class RepeatableBean {

    @OneOf(values = {"A", "B"})
    @OneOf(values = {"1", "2"})
    private final @Nullable String value;

    private RepeatableBean(@Nullable String value) {
      this.value = value;
    }

    public @Nullable String getValue() {
      return value;
    }
  }

  @Test
  void givenRepeatableAnnotations_whenValidating_thenBothEnforced() {
    RepeatableBean bean = new RepeatableBean("X");

    Set<ConstraintViolation<RepeatableBean>> violations = validator.validate(bean);

    assertEquals(2, violations.size());
    assertTrue(violations.stream().anyMatch(v -> "must be one of [A, B]".equals(v.getMessage())));
    assertTrue(violations.stream().anyMatch(v -> "must be one of [1, 2]".equals(v.getMessage())));
  }

  @Test
  void givenNullRepeatable_whenValidating_thenNoViolation() {
    RepeatableBean bean = new RepeatableBean(null);

    Set<ConstraintViolation<RepeatableBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static final class EmptyValuesBean {

    @OneOf(values = {})
    private final @Nullable String value;

    private EmptyValuesBean(@Nullable String value) {
      this.value = value;
    }

    public @Nullable String getValue() {
      return value;
    }
  }

  @Test
  void givenEmptyValues_whenValidatingNonNull_thenViolation() {
    EmptyValuesBean bean = new EmptyValuesBean("anything");

    Set<ConstraintViolation<EmptyValuesBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be one of []", violations.iterator().next().getMessage());
  }

  @Test
  void givenEmptyValues_whenValidatingNull_thenNoViolation() {
    EmptyValuesBean bean = new EmptyValuesBean(null);

    Set<ConstraintViolation<EmptyValuesBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static final class EnumTypeStringBean {

    @OneOf(enumType = TestEnum.class)
    private final @Nullable String value;

    private EnumTypeStringBean(@Nullable String value) {
      this.value = value;
    }

    public @Nullable String getValue() {
      return value;
    }
  }

  @Test
  void givenEnumTypeAndValidConstantName_whenValidating_thenNoViolation() {
    EnumTypeStringBean bean = new EnumTypeStringBean("A");

    Set<ConstraintViolation<EnumTypeStringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenEnumTypeAndInvalidConstantName_whenValidating_thenViolation() {
    EnumTypeStringBean bean = new EnumTypeStringBean("D");

    Set<ConstraintViolation<EnumTypeStringBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be one of []", violations.iterator().next().getMessage());
  }

  @Test
  void givenEnumTypeAndNullValue_whenValidating_thenNoViolation() {
    EnumTypeStringBean bean = new EnumTypeStringBean(null);

    Set<ConstraintViolation<EnumTypeStringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  private static final class EnumTypeIgnoreCaseStringBean {

    @OneOf(enumType = TestEnum.class, ignoreCase = true)
    private final String value;

    private EnumTypeIgnoreCaseStringBean(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @Test
  void givenEnumTypeIgnoreCaseAndLowercaseConstantName_whenValidating_thenNoViolation() {
    EnumTypeIgnoreCaseStringBean bean = new EnumTypeIgnoreCaseStringBean("a");

    Set<ConstraintViolation<EnumTypeIgnoreCaseStringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenEnumTypeIgnoreCaseAndInvalidName_whenValidating_thenViolation() {
    EnumTypeIgnoreCaseStringBean bean = new EnumTypeIgnoreCaseStringBean("d");

    Set<ConstraintViolation<EnumTypeIgnoreCaseStringBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be one of []", violations.iterator().next().getMessage());
  }

  private static final class ValuesOverEnumTypeBean {

    @OneOf(
        values = {"1", "2"},
        enumType = TestEnum.class)
    private final String value;

    private ValuesOverEnumTypeBean(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @Test
  void givenValuesAndEnumType_whenValidatingValueFromValues_thenNoViolation() {
    ValuesOverEnumTypeBean bean = new ValuesOverEnumTypeBean("1");

    Set<ConstraintViolation<ValuesOverEnumTypeBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenValuesAndEnumType_whenValidatingEnumConstantNotInValues_thenViolation() {
    ValuesOverEnumTypeBean bean = new ValuesOverEnumTypeBean("A");

    Set<ConstraintViolation<ValuesOverEnumTypeBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be one of [1, 2]", violations.iterator().next().getMessage());
  }

  private static final class NonEnumTypeBean {

    @OneOf(enumType = String.class)
    private final @Nullable String value;

    private NonEnumTypeBean(@Nullable String value) {
      this.value = value;
    }

    public @Nullable String getValue() {
      return value;
    }
  }

  @Test
  void givenNonEnumEnumType_whenValidating_thenNoExceptionThrown() {
    NonEnumTypeBean bean = new NonEnumTypeBean("anything");

    Set<ConstraintViolation<NonEnumTypeBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be one of []", violations.iterator().next().getMessage());
  }

  @Test
  void givenNonEnumEnumTypeAndNullValue_whenValidating_thenNoViolation() {
    NonEnumTypeBean bean = new NonEnumTypeBean(null);

    Set<ConstraintViolation<NonEnumTypeBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  // ----- List -----

  private static final class ListBean {

    private final @Nullable List<@OneOf(values = {"A", "B", "C"}) String> values;

    private ListBean(@Nullable List<@OneOf(values = {"A", "B", "C"}) String> values) {
      this.values = values;
    }

    public @Nullable List<@OneOf(values = {"A", "B", "C"}) String> getValues() {
      return values;
    }
  }

  @Test
  void givenListWithAllValidElements_whenValidating_thenNoViolation() {
    ListBean bean = new ListBean(List.of("A", "B", "C"));

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenListWithInvalidElement_whenValidating_thenViolation() {
    ListBean bean = new ListBean(List.of("A", "X"));

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be one of [A, B, C]", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullList_whenValidating_thenNoViolation() {
    ListBean bean = new ListBean(null);

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }
}
