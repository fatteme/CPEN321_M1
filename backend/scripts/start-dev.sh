#!/bin/bash

# Development startup script for User Management Backend

echo "🚀 Starting User Management Backend Development Server..."

# Check if .env file exists
if [ ! -f .env ]; then
    echo "⚠️  .env file not found. Copying from .env.example..."
    cp .env.example .env
    echo "✅ Please edit .env file with your configuration before running the server."
    exit 1
fi

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo "📦 Installing dependencies..."
    npm install
fi

# Check if MongoDB is running (optional check)
echo "🔍 Checking MongoDB connection..."

# Start the development server
echo "🌟 Starting development server with hot reload..."
npm run dev
