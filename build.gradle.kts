import com.diffplug.spotless.LineEnding

plugins {
    id("internal.convention-java-library")
    id("internal.convention-publishing")
    id("com.diffplug.spotless").version("8.0.0")
    id("com.gradleup.nmcp").version("1.3.0")
}

group = "io.github.malczuuu"

dependencies {
    api("jakarta.validation:jakarta.validation-api:3.1.1")

    testImplementation(platform("org.junit:junit-bom:5.14.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.hibernate.validator:hibernate-validator:8.0.3.Final")
}

internalPublishing {
    displayName = "Jakarta Validation Extensions"
    description = "A tiny set of annotations that extend Jakarta Bean Validation."
}

nmcp {
    publishAllPublicationsToCentralPortal {
        username = System.getenv("PUBLISHING_USERNAME")
        password = System.getenv("PUBLISHING_PASSWORD")

        publishingType = "USER_MANAGED"
    }
}

spotless {
    java {
        target("**/src/**/*.java")

        // NOTE: decided not to upgrade Google Java Format, as versions 1.29+ require running it on Java 21
        googleJavaFormat("1.28.0")
        forbidWildcardImports()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    kotlin {
        target("**/src/**/*.kt")

        ktfmt("0.59").metaStyle()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    kotlinGradle {
        target("*.gradle.kts", "problem4j-*/*.kts", "buildSrc/*.kts", "buildSrc/src/**/*.kts")

        ktlint("1.8.0").editorConfigOverride(mapOf("max_line_length" to "120"))
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    format("yaml") {
        target("**/*.yml", "**/*.yaml")

        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    format("misc") {
        target("**/.gitattributes", "**/.gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces(4)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
}
