#!/bin/bash

echo "Smart Banking System - Compilation Script"
echo "========================================"

# Create necessary directories
mkdir -p build/classes
mkdir -p lib

# Download MySQL JDBC driver if not present
if [ ! -f "lib/mysql-connector-j-8.2.0.jar" ]; then
    echo "Downloading MySQL JDBC driver..."
    curl -L -o lib/mysql-connector-j-8.2.0.jar https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar
    if [ $? -ne 0 ]; then
        echo "Failed to download MySQL JDBC driver. Please download manually and place in lib/ directory."
        echo "Alternative: You can run without MySQL by using the simple file-based version."
        exit 1
    fi
fi

# Set classpath
CLASSPATH="lib/mysql-connector-j-8.2.0.jar"

# Compile Java files
echo "Compiling Java source files..."
find src -name "*.java" -print0 | xargs -0 javac -cp "$CLASSPATH" -d build/classes

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "To run the application:"
    echo "  ./run.sh"
    echo ""
    echo "Or manually:"
    echo "  java -cp \"build/classes:lib/mysql-connector-j-8.2.0.jar\" BankingApplication"
    echo ""
    echo "Make sure MySQL is running and configured according to DatabaseConfig.java"
else
    echo "Compilation failed!"
    exit 1
fi