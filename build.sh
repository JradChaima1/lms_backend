#!/usr/bin/env bash
# exit on error
set -o errexit

# Build the application
./mvnw clean install -DskipTests

# The JAR file will be in target/
echo "Build completed successfully"
