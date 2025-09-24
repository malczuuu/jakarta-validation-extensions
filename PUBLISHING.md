# Publishing

Publishing memo for setting up CI/CD to publish artifacts to Maven Central via Sonatype.

## Table of Contents

- [CI/CD Setup](#cicd-setup)
- [Setting up PGP Key](#setting-up-pgp-key)

## CI/CD Setup

See [`gradle-release.yml`](.github/workflows/gradle-release.yml).

Set the following environment variables in your CI/CD (GitHub Actions, etc.):

```txt
# generated on Sonatype account
PUBLISHING_USERNAME=<username>
PUBLISHING_PASSWORD=<password>

# generated PGP key for signing artifacts
SIGNING_KEY=<PGP key>
SIGNING_PASSWORD=<PGP password>
```

Artifacts are published to Maven Central via Sonatype, using following Gradle task.

```bash
./gradlew -Pversion=<version> -Psign publishAggregationToCentralPortal
```

This command uses `nmcp` Gradle plugin - [link](https://github.com/GradleUp/nmcp).

**Note** that this only uploads the artifacts to Sonatype repository. You need to manually log in to Sonatype to push
the artifacts to Maven Central.

## Setting up PGP Key

1. Generate a new GPG key:
   ```bash
   gpg --full-generate-key
   ```
    - Choose `RSA and RSA` (default)
    - Key size: `4096`
    - Expiration: as you prefer
    - Provide your name and email
    - Choose a passphrase
2. List your keys and fingerprint:
   ```bash
   gpg --list-keys --keyid-format LONG
   ```
   Example output:
   ```txt
   pub   rsa4096/ABCDEF1234567890 2025-09-24 [SC]
         11223344556677889900AABBCCDDEEFF11223344
   uid                      Name <email@example.com>
   ```
   The short hex string is your key ID (`ABCDEF1234567890`).
   The long hex string is your fingerprint (`112233...`).
3. Publish your public key to a keyserver:
   ```bash
   gpg --keyserver keyserver.ubuntu.com --send-keys 11223344556677889900AABBCCDDEEFF11223344
   gpg --keyserver keys.openpgp.org --send-keys 11223344556677889900AABBCCDDEEFF11223344
   ```
   Wait a few minutes for propagation.
4. Export your public key to a .asc file (optional)
   ```bash
   gpg --armor --export 11223344556677889900AABBCCDDEEFF11223344 > publickey.asc
   gpg --armor --export-secret-keys 11223344556677889900AABBCCDDEEFF11223344 > privatekey.asc
   ```
5. Use the key in Gradle for signing (`build.gradle.kts`). In your Gradle build, you can reference environment variables
   for CI:
   ```kotlin
   publishing {
       publications {
           create<MavenPublication>("maven") {
               // ...
           }
       }
   }
   signing {
       useInMemoryPgpKeys(
           System.getenv("SIGNING_KEY"),      // content of privatekey.asc file from step 4
           System.getenv("SIGNING_PASSWORD")  // passphrase for the PGP key selected in step 1
       )
       sign(publishing.publications["maven"])
   }
   ```
