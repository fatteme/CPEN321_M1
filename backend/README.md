# Backend Setup

## Requirements

- [Node.js](https://nodejs.org/en/download/) 18+ installed on your machine
- [MongoDB](https://www.mongodb.com/) instance running locally or remotely

## Setup

1. **Install dependencies**:

   ```
   npm install
   ```

2. **Environment Configuration**: Create a `.env` file in the root directory following the `.env.example` format.

3. **Start development server**: Start development server with ts-node with auto-reload
   ```
   npm run dev
   ```

## Build and Run

- **Build**: Compile TypeScript to JavaScript
  ```
  npm run build
  ```
- **Start production**: Run compiled JavaScript
  ```
  npm start
  ```

## API Endpoints

The server runs on port 3000 (configurable via PORT env var) with the following routes:

- `/api/auth/*` - Authentication
- `/api/users/*` - User management
- `/api/hobbies/*` - Hobby management
- `/api/media/*` - Media uploads
  - Uploaded files are stored in the `uploads/` directory. Ensure this directory exists and has write permissions.

## Code Quality Tools

### ESLint Setup

ESLint is configured to enforce code quality and style consistency. The configuration is in `eslint.config.js`.

- **Run linting**:
  ```
  npm run lint
  ```
- **Auto-fix issues**:
  ```
  npm run lint:fix
  ```

### Prettier Setup

Prettier is configured to automatically format your code. The configuration is in `.prettierrc`.

- **Run format checking**:
  ```
  npm run format
  ```
- **Format code**:
  ```
  npm run format:fix
  ```

**VS Code Integration**: Install the Prettier and ESLint extensions for automatic formatting and linting on save.
