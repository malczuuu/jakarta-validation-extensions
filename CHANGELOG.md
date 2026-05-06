# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog][keepachangelog], and this project adheres to [Semantic Versioning][semver].

## [Unreleased]

### Added

- Add `enumType` to `@OneOf` annotation to allow users to specify an `enum` class instead of listing all values.
- Add support for Java Platform Module System (JPMS) by adding `module-info.java` and exporting the main package.

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
