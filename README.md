# Jakarta Validation Extensions

This project provides additional features and extensions for Jakarta Validation (Bean Validation). It includes custom
validation annotations.

## Usage

**Java 11** or higher is required to use this library.

- **`@OneOf`** - validates that a value is one of the specified values. Supported types include `String`, `Number`,
  `Enum` and `Character`.

## Versioning

This project follows [Semantic Versioning](https://semver.org/).

## Building from source

To build the project from source, ensure you have **Java 17** or higher. Yes, Java 17 is required to build the project,
but it should produce artifacts compatible with **Java 11**.

```bash
./gradlew clean build
```

```bash
./gradlew -Pversion=XXXX clean build publishToMavenLocal
```
