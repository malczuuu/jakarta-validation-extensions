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

class IsoDateTimeValidatorTest {

  private Validator validator;

  @BeforeEach
  void beforeEach() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  // ----- offsetRequired = false (default) -----

  private static final class StringBean {

    @IsoDateTime private final @Nullable String value;

    private StringBean(@Nullable String value) {
      this.value = value;
    }

    public @Nullable String getValue() {
      return value;
    }
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "2024-01-15T10:30:00",
        "2024-01-15T10:30:00Z",
        "2024-01-15T10:30:00+05:30",
        "2024-01-15T10:30:00-03:00",
        "2024-01-15T10:30:00.123Z",
        "2024-01-15T10:30:00.123456789Z"
      })
  void givenValidDateTime_whenValidating_thenNoViolation(String value) {
    StringBean bean = new StringBean(value);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "2024-01-15",
        "2024-01-15 10:30:00",
        "not-a-date-time",
        "2024-13-15T10:30:00",
        "2024-01-15T25:00:00"
      })
  void givenInvalidDateTime_whenValidating_thenViolation(String value) {
    StringBean bean = new StringBean(value);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid ISO 8601 date-time", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullDateTime_whenValidating_thenNoViolation() {
    StringBean bean = new StringBean(null);

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  // ----- offsetRequired = true -----

  private static final class OffsetRequiredBean {

    @IsoDateTime(offsetRequired = true)
    private final @Nullable String value;

    private OffsetRequiredBean(@Nullable String value) {
      this.value = value;
    }

    public @Nullable String getValue() {
      return value;
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"2024-01-15T10:30:00Z", "2024-01-15T10:30:00+05:30"})
  void givenValidDateTimeWithOffset_whenOffsetRequired_thenNoViolation(String value) {
    OffsetRequiredBean bean = new OffsetRequiredBean(value);

    Set<ConstraintViolation<OffsetRequiredBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenDateTimeWithoutOffset_whenOffsetRequired_thenViolation() {
    OffsetRequiredBean bean = new OffsetRequiredBean("2024-01-15T10:30:00");

    Set<ConstraintViolation<OffsetRequiredBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid ISO 8601 date-time", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullDateTime_whenOffsetRequired_thenNoViolation() {
    OffsetRequiredBean bean = new OffsetRequiredBean(null);

    Set<ConstraintViolation<OffsetRequiredBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  // ----- Repeatable annotations -----

  private static final class RepeatableBean {

    @IsoDateTime
    @IsoDateTime(offsetRequired = true)
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
    RepeatableBean beanWithOffset = new RepeatableBean("2024-01-15T10:30:00Z");
    assertTrue(validator.validate(beanWithOffset).isEmpty());

    RepeatableBean beanWithoutOffset = new RepeatableBean("2024-01-15T10:30:00");
    Set<ConstraintViolation<RepeatableBean>> violations = validator.validate(beanWithoutOffset);
    assertEquals(1, violations.size());
    assertEquals("must be a valid ISO 8601 date-time", violations.iterator().next().getMessage());
  }

  // ----- Unsupported type -----

  private static final class UnsupportedBean {

    @IsoDateTime private final Object value;

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
    assertEquals("IsoDateTime not supported for java.lang.Object type", e.getCause().getMessage());
  }

  // ----- Message -----

  @Test
  void givenInvalidDateTime_whenValidating_thenDefaultMessage() {
    StringBean bean = new StringBean("not-a-date-time");

    Set<ConstraintViolation<StringBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid ISO 8601 date-time", violations.iterator().next().getMessage());
  }

  // ----- List -----

  private static final class ListBean {

    private final @Nullable List<@IsoDateTime String> values;

    private ListBean(@Nullable List<@IsoDateTime String> values) {
      this.values = values;
    }

    public @Nullable List<@IsoDateTime String> getValues() {
      return values;
    }
  }

  @Test
  void givenListWithAllValidElements_whenValidating_thenNoViolation() {
    ListBean bean = new ListBean(List.of("2024-01-15T10:30:00", "2024-01-15T10:30:00Z"));

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }

  @Test
  void givenListWithInvalidElement_whenValidating_thenViolation() {
    ListBean bean = new ListBean(List.of("2024-01-15T10:30:00", "not-a-date-time"));

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertEquals(1, violations.size());
    assertEquals("must be a valid ISO 8601 date-time", violations.iterator().next().getMessage());
  }

  @Test
  void givenNullList_whenValidating_thenNoViolation() {
    ListBean bean = new ListBean(null);

    Set<ConstraintViolation<ListBean>> violations = validator.validate(bean);

    assertTrue(violations.isEmpty());
  }
}
