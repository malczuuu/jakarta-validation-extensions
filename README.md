# Jakarta Validation Extensions

[![Build Status](https://github.com/malczuuu/jakarta-validation-extension/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/malczuuu/jakarta-validation-extension/actions/workflows/gradle-build.yml)
[![Sonatype](https://img.shields.io/maven-central/v/io.github.malczuuu.problem4j/jakarta-validation-extension)](https://central.sonatype.com/artifact/io.github.malczuuu.problem4j/jakarta-validation-extension)
[![License](https://img.shields.io/github/license/malczuuu/jakarta-validation-extension)](https://github.com/malczuuu/jakarta-validation-extension/blob/main/LICENSE)

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

## Versioning

This project follows [Semantic Versioning](https://semver.org/).

## Building from source

<details>
<summary><b>Expand...</b></summary>

To build the project from source, ensure you have **Java 17** or higher. Yes, Java 17 is required to build the project,
but it should produce artifacts compatible with **Java 11**.

```bash
./gradlew clean build
```

```bash
./gradlew -Pversion=XXXX clean build publishToMavenLocal
```

</details>

[maven-central]: https://central.sonatype.com/artifact/io.github.malczuuu/jakarta-validation-extensions
