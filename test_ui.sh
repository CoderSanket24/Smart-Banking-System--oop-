#!/bin/bash

echo "🎨 Testing Enhanced Interactive UI..."
echo "======================================"

# Create build directory
mkdir -p build/classes

# Compile for UI testing
echo "Compiling enhanced UI components..."
find src -name "*.java" -print0 | xargs -0 javac -cp "lib/mysql-connector-j-8.2.0.jar" -d build/classes 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ UI compilation successful!"
    echo ""
    echo "🚀 Starting Interactive Banking UI..."
    echo ""
    echo "✨ New Interactive Features:"
    echo "   • Quick Login buttons (Manager/Customer)"
    echo "   • Hover effects on buttons"
    echo "   • Loading animations"
    echo "   • Keyboard shortcuts (F5, Ctrl+D, Ctrl+W, Ctrl+T)"
    echo "   • Right-click context menus"
    echo "   • Auto-refresh indicators"
    echo "   • Status bar with live updates"
    echo "   • Dashboard widgets (Manager)"
    echo ""
    echo "Note: Database errors are expected without MySQL, but UI works perfectly!"
    echo ""
    
    # Run the enhanced LoginFrame
    java -cp "build/classes:lib/mysql-connector-j-8.2.0.jar" gui.LoginFrame
else
    echo "❌ UI compilation failed!"
    echo "Make sure all source files are present."
fi