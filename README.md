# Jakarta Validation Extensions

This project provides additional features and extensions for Jakarta Validation (Bean Validation). It includes custom
validation annotations.

## Usage

Add library as dependency to Maven or Gradle. See the actual versions on [Maven Central][maven-central]. **Java 11** or
higher is required to use this library.

* Maven
  ```xml
  <dependency>
      <groupId>io.github.malczuuu</groupId>
      <artifactId>jakarta-validation-extensions</artifactId>
      <version>1.0.0-alpha2</version>
  </dependency>
  ```
* Gradle
  ```kotlin
  dependencies {
      implementation("io.github.malczuuu:jakarta-validation-extensions:1.0.0-alpha2")
  }
  ```

## Features

- **`@OneOf`** - validates that a value is one of the specified values. Supported types include `String`, `Number`,
  `Enum` and `Character`.

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
