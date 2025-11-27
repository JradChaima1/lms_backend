#!/usr/bin/env bash
# exit on error
set -o errexit

# Use Maven from Render's environment instead of mvnw
mvn clean install -DskipTests

# The JAR file will be in target/
echo "Build completed successfully"
