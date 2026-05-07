# `jakarta-validation-extensions` - Agent Instructions

Tiny Java library of custom Jakarta Bean Validation 3 constraints (annotations + validators).

## Build & Validate

- **Always run `./gradlew`** (default tasks: `spotlessApply build`) to format, compile, and test.
- If Spotless fails, run `./gradlew spotlessApply` to auto-fix, then re-run `./gradlew`.
- Java 17+ required for Gradle runtime; code compiles to Java 11 bytecode (`--release 11`).
- Dependencies: `gradle/libs.versions.toml`. Refresh with `./gradlew --refresh-dependencies`.
- Always validate changes with a full `./gradlew` run before considering a task complete.

## Project Layout

| Path                 | Contents                                         |
|----------------------|--------------------------------------------------|
| `src/main/java`      | Annotation types and `ConstraintValidator` impls |
| `src/test/java`      | Tests (JUnit Jupiter + Hibernate Validator)      |
| `src/main/resources` | `ValidationMessages*.properties` bundles         |
| `build.gradle.kts`   | Build config & Spotless setup                    |
| `buildSrc`           | Custom Gradle plugins/scripts                    |

## Agent Rules

- Do not use terminal commands (e.g., `cat`, `find`, `ls`) to read or list project files - use IDE/agent tools instead.
- Run tests once, save output to `build/test-run.log` inside the repo (`> build/test-run.log 2>&1`), then read from that
  file to extract errors. Never run the same test command multiple times, without changes in sources. Store test output
  in multiple files if you want to compare before/after changes (ex. `build/test-run-{i}.log`).

## Coding Rules

- No self-explaining comments - only add comments for non-obvious context.
- No wildcard imports.
- Follow existing code patterns and naming conventions.
- Let `spotlessApply` handle all formatting - never format manually.
- Do not use `CharSequence.isEmpty()` - it requires Java 15; use `length() == 0` instead.
- Do not use `String.toCharArray()` in loops - use `charAt(i)` with an index loop instead.
- ErrorProne + NullAway run on main sources only; test compilation has all checks disabled.
- Use JSpecify (`@NullMarked`, `@Nullable`) for null annotations on main sources.

## Adding a New Constraint

Each constraint requires:
1. Annotation interface (e.g., `Foo.java`) with `message`, `groups`, `payload`, and a `List` inner annotation.
2. Validator class (e.g., `FooValidator.java`) implementing `ConstraintValidator<Foo, Object>`.
3. A key in every `ValidationMessages*.properties` bundle under `src/main/resources`.
4. Tests covering valid values, invalid values, `null` (must be valid), and unsupported types.

## Test Conventions

- Method naming: `givenThis_whenThat_thenWhat`.
- No `// given`, `// when`, `// then` section comments.
- Cover both positive and negative cases.
- Use JUnit Jupiter assertions (`assertTrue`, `assertFalse`, `assertThrows`, etc.).
