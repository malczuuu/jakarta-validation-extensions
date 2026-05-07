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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LanguageTagValidatorTest {

  private Validator validator;

  @BeforeEach
  void beforeEach() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  private static final class StringBean {

    @LanguageTag private final @Nullable String value;

    private StringBean(@Nullable String value) {
      this.value = value;
    }

    public @Nullable String getValue() {
      return value;
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"en", "en-US", "zh-Hant", "sr-Latn-RS", "fr", "de-DE"})
  void givenValidLanguageTag_whenValidating_thenNoViolation(String value) {
    StringBean bean = new StringBean(value);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"en US", "en!", "en--US", ""})
  void givenInvalidLanguageTag_whenValidating_thenViolation(String value) {
    StringBean bean = new StringBean(value);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid language tag", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullValue_whenValidating_thenNoViolation() {
    StringBean bean = new StringBean(null);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  // ----- Repeatable annotations -----

  private static final class RepeatableBean {

    @LanguageTag
    @LanguageTag(message = "language tag is invalid")
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
    RepeatableBean validBean = new RepeatableBean("en-US");
    assertTrue(validator.validate(validBean).isEmpty());

    RepeatableBean invalidBean = new RepeatableBean("en US");
    Set<ConstraintViolation<RepeatableBean>> violations = validator.validate(invalidBean);
    assertEquals(2, violations.size());
    assertTrue(
        violations.stream().anyMatch(v -> "must be a valid language tag".equals(v.getMessage())));
    assertTrue(violations.stream().anyMatch(v -> "language tag is invalid".equals(v.getMessage())));
  }

  // ----- Unsupported type -----

  private static final class UnsupportedBean {

    @LanguageTag private final Object value;

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
    assertEquals("LanguageTag not supported for java.lang.Object type", e.getCause().getMessage());
  }

  // ----- Message -----

  @Test
  void givenInvalidLanguageTag_whenValidating_thenDefaultMessage() {
    StringBean bean = new StringBean("en US");

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid language tag", violations.iterator().next().getMessage());
  }

  // ----- List -----

  private static final class ListBean {

    private final @Nullable List<@LanguageTag String> values;

    private ListBean(@Nullable List<@LanguageTag String> values) {
      this.values = values;
    }

    public @Nullable List<@LanguageTag String> getValues() {
      return values;
    }
  }

  @Test
  void givenListWithAllValidElements_whenValidating_thenNoViolation() {
    ListBean bean = new ListBean(List.of("en", "en-US", "zh-Hant"));

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenListWithInvalidElement_whenValidating_thenViolation() {
    ListBean bean = new ListBean(List.of("en-US", "en US"));

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid language tag", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullList_whenValidating_thenNoViolation() {
    ListBean bean = new ListBean(null);

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }
}
