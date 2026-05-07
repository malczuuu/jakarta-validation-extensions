# Jakarta Validation Extensions

[![Build Status](https://github.com/malczuuu/jakarta-validation-extensions/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/malczuuu/jakarta-validation-extensions/actions/workflows/gradle-build.yml)
[![Sonatype](https://img.shields.io/maven-central/v/io.github.malczuuu/jakarta-validation-extensions)](https://central.sonatype.com/artifact/io.github.malczuuu/jakarta-validation-extensions)
[![License](https://img.shields.io/github/license/malczuuu/jakarta-validation-extensions)](https://github.com/malczuuu/jakarta-validation-extensions/blob/main/LICENSE)

This project provides additional features and extensions for Jakarta Validation (Bean Validation). It includes custom
validation annotations.

> A small project to experiment with publishing artifacts to Maven Central and see what eventually comes of it.

## Usage

Add library as dependency to Maven or Gradle. See the actual versions on [Maven Central][maven-central]. **Java 11** or
higher is required to use this library.

* Maven
  ```xml
  <dependency>
      <groupId>io.github.malczuuu</groupId>
      <artifactId>jakarta-validation-extensions</artifactId>
      <version>1.1.0</version>
  </dependency>
  ```
* Gradle
  ```kotlin
  dependencies {
      implementation("io.github.malczuuu:jakarta-validation-extensions:1.1.0")
  }
  ```

## Features

- **`@OneOf`** - validates that a value is one of the specified values. Supported types include `CharSequence` (generic
  for `String` in particular, but also `StringBuilder` etc.), `Number`, `Enum` and `Character`.
- **`@Alphanumeric`** - validates that a value contains only alphanumeric characters (`a-z`, `A-Z`, `0-9`), with
  optional characters to ignore. Supported types include `CharSequence`, and `Character`.
- **`@Hex`** - validates that a `CharSequence` contains only hexadecimal characters (`0–9`, `a–f`, `A–F`).
- **`@SemVer`** - validates that a `CharSequence` is a valid semantic version string per the
  [semver.org](https://semver.org/) specification.
- **`@CountryCode`** - validates that a `CharSequence` is a recognized ISO 3166-1 alpha-2 country code, with an
  optional `ignoreCase` attribute.
- **`@LanguageTag`** - validates that a `CharSequence` is a syntactically valid BCP 47 language tag.
- **`@IsoDate`** - validates that a `CharSequence` is a valid ISO 8601 date (e.g. `2024-01-15`).
- **`@IsoTime`** - validates that a `CharSequence` is a valid ISO 8601 time (e.g. `10:30:00`), with an optional
  `offsetRequired` attribute to mandate a timezone offset.
- **`@IsoDateTime`** - validates that a `CharSequence` is a valid ISO 8601 date-time (e.g. `2024-01-15T10:30:00`),
  with an optional `offsetRequired` attribute to mandate a timezone offset.

Messages are English only. No localization support is provided.

## Versioning

This project follows [Semantic Versioning](https://semver.org/).

## Building from source

<details>
<summary><b>Expand...</b></summary>

Gradle **9.x+** requires **Java 17+** to run, but higher Java versions can also be used. All modules of this project are
compiled using a **Java 11 toolchain**, so the produced artifacts are compatible with **Java 11**, regardless of the
Java version Gradle runs on.

```bash
./gradlew build
```

To execute tests use `test` task.

```bash
./gradlew test
```

To format the code according to the style defined in [`build.gradle.kts`](./build.gradle.kts) rules use `spotlessApply`
task. **Note** that **building will fail** if code is not properly formatted.

```bash
./gradlew spotlessApply
```

To publish the built artifacts to local Maven repository, run following command, replacing `XXXX` with the desired
version. By default, the version is derived from git commit hash.

```bash
./gradlew -Pversion=XXXX publishToMavenLocal
```

</details>

[maven-central]: https://central.sonatype.com/artifact/io.github.malczuuu/jakarta-validation-extensions
