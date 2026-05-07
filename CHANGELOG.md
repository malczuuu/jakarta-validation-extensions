# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog][keepachangelog], and this project adheres to [Semantic Versioning][semver].

## [Unreleased]

### Added

- Add `enumType` to `@OneOf` annotation to allow users to specify an `enum` class instead of listing all values.
- Add support for Java Platform Module System (JPMS) by adding `module-info.java` and exporting the main package.
- Add `@IsoDate` annotation to validate ISO 8601 date strings (e.g. `2024-01-15`).
- Add `@IsoDateTime` annotation to validate ISO 8601 date-time strings (e.g. `2024-01-15T10:30:00`), with an optional
  `offsetRequired` attribute to mandate a timezone offset.
- Add `@IsoTime` annotation to validate ISO 8601 time strings (e.g. `10:30:00`), with an optional `offsetRequired`
  attribute to mandate a timezone offset.
- Add `@Hex` annotation to validate that a string contains only hexadecimal characters (`0–9`, `a–f`, `A–F`).
- Add `@SemVer` annotation to validate semantic version strings per the semver.org specification.
- Add `@CountryCode` annotation to validate ISO 3166-1 alpha-2 country codes, with an optional `ignoreCase` attribute.
- Add `@LanguageTag` annotation to validate syntactically valid BCP 47 language tags.

## [1.1.1] - 2025-12-20

Release performed for testing new Gradle setup and Actions workflows. No functional changes were made.

## [1.1.0] - 2025-10-02

### Added

- Add `@Alphanumeric` annotation to validate (quite obviously) that a given text only contains alphanumerics.

### Changed

- Make `@OneOf` validate generic `CharSequence` instead of just `String`s.

## [1.0.0] - 2025-09-25

### Added

- Add `@OneOf` annotation for `enum`-alike validation.

[keepachangelog]: https://keepachangelog.com/en/1.1.0/

[semver]: https://semver.org/spec/v2.0.0.html
