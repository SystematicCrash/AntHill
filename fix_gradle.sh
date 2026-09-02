#!/bin/bash
# Bypass the wrapper entirely by using the system gradle directly
alias gradlew='gradle'
# Force the project to use the system gradle
# We can't easily change the wrapper script itself, but we can point the project to the system one.
# Since build.gradle.kts is present, we should configure it there.
echo 'gradle {
    gradleVersion = "9.2.1"
}' >> build.gradle.kts

# If that fails, the most robust way is to just use "gradle build" instead of "./gradlew build"
# Let us try running "gradle build" instead.
gradle build --offline
