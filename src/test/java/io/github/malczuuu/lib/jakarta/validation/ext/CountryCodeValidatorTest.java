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

class CountryCodeValidatorTest {

  private Validator validator;

  @BeforeEach
  void beforeEach() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  private static final class StringBean {

    @CountryCode private final @Nullable String value;

    private StringBean(@Nullable String value) {
      this.value = value;
    }

    public @Nullable String getValue() {
      return value;
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"US", "PL", "DE", "FR", "GB", "JP"})
  void givenValidCountryCode_whenValidating_thenNoViolation(String value) {
    StringBean bean = new StringBean(value);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"us", "XX", "USA", "ZZ", "123"})
  void givenInvalidCountryCode_whenValidating_thenViolation(String value) {
    StringBean bean = new StringBean(value);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid country code", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullValue_whenValidating_thenNoViolation() {
    StringBean bean = new StringBean(null);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  // ----- ignoreCase -----

  private static final class IgnoreCaseBean {

    @CountryCode(ignoreCase = true)
    private final String value;

    private IgnoreCaseBean(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"us", "pl", "de", "US", "PL", "DE"})
  void givenIgnoreCase_whenValidating_thenAcceptsBothCases(String value) {
    IgnoreCaseBean bean = new IgnoreCaseBean(value);

    Set<ConstraintViolation<IgnoreCaseBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"xx", "XX", "ZZ"})
  void givenIgnoreCaseWithUnrecognizedCode_whenValidating_thenViolation(String value) {
    IgnoreCaseBean bean = new IgnoreCaseBean(value);

    Set<ConstraintViolation<IgnoreCaseBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid country code", violations.iterator().next().getMessage());
  }

  // ----- Repeatable annotations -----

  private static final class RepeatableBean {

    @CountryCode
    @CountryCode(message = "country code is invalid")
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
    RepeatableBean validBean = new RepeatableBean("US");
    assertTrue(validator.validate(validBean).isEmpty());

    RepeatableBean invalidBean = new RepeatableBean("XX");
    Set<ConstraintViolation<RepeatableBean>> violations = validator.validate(invalidBean);
    assertEquals(2, violations.size());
    assertTrue(
        violations.stream().anyMatch(v -> "must be a valid country code".equals(v.getMessage())));
    assertTrue(violations.stream().anyMatch(v -> "country code is invalid".equals(v.getMessage())));
  }

  // ----- Unsupported type -----

  private static final class UnsupportedBean {

    @CountryCode private final Object value;

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
    assertEquals("CountryCode not supported for java.lang.Object type", e.getCause().getMessage());
  }

  // ----- Message -----

  @Test
  void givenInvalidCountryCode_whenValidating_thenDefaultMessage() {
    StringBean bean = new StringBean("XX");

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid country code", violations.iterator().next().getMessage());
  }

  // ----- List -----

  private static final class ListBean {

    private final @Nullable List<@CountryCode String> values;

    private ListBean(@Nullable List<@CountryCode String> values) {
      this.values = values;
    }

    public @Nullable List<@CountryCode String> getValues() {
      return values;
    }
  }

  @Test
  void givenListWithAllValidElements_whenValidating_thenNoViolation() {
    ListBean bean = new ListBean(List.of("US", "PL", "DE"));

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenListWithInvalidElement_whenValidating_thenViolation() {
    ListBean bean = new ListBean(List.of("US", "XX"));

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid country code", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullList_whenValidating_thenNoViolation() {
    ListBean bean = new ListBean(null);

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }
}
