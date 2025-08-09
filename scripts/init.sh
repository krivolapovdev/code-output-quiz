#!/usr/bin/env bash
set -euo pipefail

echo "🔧 Initializing development environment..."

check_tool() {
  if ! command -v "$1" &> /dev/null; then
    echo "❌ $1 is not installed. Please install it."
    exit 1
  fi
  echo "✔ $1 version: $($1 --version || true)"
}

echo "🔧 Verifying required tools..."
check_tool java
check_tool node
check_tool lefthook
echo "✅ All required tools are available."

echo "🪝 Installing Git hooks with Lefthook..."
lefthook install
echo "✅ Git hooks installed successfully."

echo "📦 Installing frontend dependencies..."
(cd frontend && npm install)
echo "✅ Frontend dependencies installed successfully."

echo "⚙️ Installing backend dependencies..."
(cd backend && ./gradlew clean assemble)
echo "✅ Backend assemble completed."
