import com.diffplug.spotless.LineEnding

plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.diffplug.spotless").version("7.2.1")
    id("com.gradleup.nmcp.aggregation").version("1.1.0")
}

group = "io.github.malczuuu"

/**
 * In order to avoid hardcoding snapshot versions, we derive the version from the current Git commit hash. For CI/CD add
 * -Pversion={releaseVersion} parameter to match Git tag.
 */
version =
    if (version == "unspecified")
        getSnapshotVersion(rootProject.rootDir)
    else
        version

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    api("jakarta.validation:jakarta.validation-api:3.0.2")

    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.hibernate.validator:hibernate-validator:8.0.3.Final")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])

            pom {
                name.set(project.name)
                description.set("A tiny set of annotations that extend Jakarta Bean Validation.")
                url.set("https://github.com/malczuuu/${project.name}")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id = "malczuuu"
                        name = "Damian Malczewski"
                        email = "damian.m.malczewski@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/malczuuu/${project.name}.git"
                    developerConnection = "scm:git:git@github.com:malczuuu/${project.name}.git"
                    url = "https://github.com/malczuuu/${project.name}"
                }
            }
        }
    }
}

nmcpAggregation {
    centralPortal {
        username = System.getenv("PUBLISHING_USERNAME")
        password = System.getenv("PUBLISHING_PASSWORD")

        publishingType = "USER_MANAGED"
    }
    publishAllProjectsProbablyBreakingProjectIsolation()
}

signing {
    if (project.hasProperty("sign")) {
        useInMemoryPgpKeys(
            System.getenv("SIGNING_KEY"),
            System.getenv("SIGNING_PASSWORD")
        )
        sign(publishing.publications["maven"])
    }
}

spotless {
    format("misc") {
        target("**/*.gradle.kts", "**/.gitattributes", "**/.gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    java {
        target("src/**/*.java")

        googleJavaFormat("1.28.0")
        lineEndings = LineEnding.UNIX
    }
}

tasks.register("printVersion") {
    doLast {
        println("Project version: $version")
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

/**
 * Disable doclint to avoid errors and warnings on missing JavaDoc comments.
 */
tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).apply {
        addStringOption("Xdoclint:none", "-quiet")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
