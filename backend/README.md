# Backend Setup

## Requirements

- [Node.js](https://nodejs.org/en/download/) 18+ installed on your machine
- [MongoDB](https://www.mongodb.com/) instance running locally or remotely

## Setup

1. **Install dependencies**: `npm install`

2. **Environment Configuration**: Create a `.env` file in the root directory following the `.env.example` format.

3. **Start development server**: `npm run dev`

## Build and Run

- **Build**: `npm run build`
- **Start production**: `npm start`

## Available Scripts

- `npm run dev` - Start development server with ts-node with auto-reload
- `npm run build` - Compile TypeScript to JavaScript
- `npm start` - Run compiled JavaScript
- `npm run lint` - Run ESLint
- `npm run format` - Format code with Prettier

## API Endpoints

The server runs on port 3000 (configurable via PORT env var) with the following routes:

- `/api/auth/*` - Authentication
- `/api/users/*` - User management
- `/api/hobbies/*` - Hobby management
- `/api/media/*` - Media uploads
  - Uploaded files are stored in the `uploads/` directory. Ensure this directory exists and has write permissions.
