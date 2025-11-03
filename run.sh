#!/bin/bash

echo "Smart Banking System - Starting Application"
echo "=========================================="

# Check if compiled classes exist
if [ ! -d "build/classes" ]; then
    echo "Classes not found. Please run ./compile.sh first"
    exit 1
fi

# Check if MySQL JDBC driver exists
if [ ! -f "lib/mysql-connector-j-8.2.0.jar" ]; then
    echo "MySQL JDBC driver not found. Please run ./compile.sh first"
    exit 1
fi

# Set classpath
CLASSPATH="build/classes:lib/mysql-connector-j-8.2.0.jar"

echo "Starting Banking Application..."
echo "Make sure MySQL server is running on localhost:3306"
echo "Default database credentials: root/password"
echo ""

# Run the application
java -cp "$CLASSPATH" BankingApplication