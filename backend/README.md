# User Management Backend

A Node.js/TypeScript backend service for user authentication and profile management using Google OAuth.

## Features

- Google OAuth authentication
- Automatic user registration on first login
- JWT-based session management
- User profile retrieval
- Secure logout functionality
- MongoDB data persistence
- Rate limiting and security middleware

## Technology Stack

- **Runtime**: Node.js
- **Language**: TypeScript
- **Framework**: Express.js
- **Database**: MongoDB with Mongoose
- **Authentication**: Google OAuth 2.0 + JWT
- **Security**: Helmet, CORS, Rate Limiting

## Prerequisites

- Node.js (v18 or higher)
- MongoDB (local or remote)
- Google OAuth 2.0 credentials

## Setup Instructions

### 1. Install Dependencies

```bash
cd backend
npm install
```

### 2. Environment Configuration

Copy the example environment file and configure it:

```bash
cp .env.example .env
```

Edit `.env` file with your configuration:

```env
# Server Configuration
PORT=3000
NODE_ENV=development

# MongoDB Configuration
MONGODB_URI=mongodb://localhost:27017/user-management

# Google OAuth Configuration
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

# JWT Configuration
JWT_SECRET=your-jwt-secret-key
JWT_EXPIRES_IN=24h

# CORS Configuration
FRONTEND_URL=http://localhost:3000
```

### 3. Google OAuth Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google+ API
4. Create OAuth 2.0 credentials
5. Add your domain to authorized origins
6. Copy Client ID and Client Secret to `.env`

### 4. MongoDB Setup

Ensure MongoDB is running locally or update `MONGODB_URI` to point to your MongoDB instance.

### 5. Start the Server

Development mode with hot reload:
```bash
npm run dev
```

Production build and start:
```bash
npm run build
npm start
```

## API Documentation

### Base URL
```
http://localhost:3000/api
```

### Authentication Endpoints

#### POST /api/auth/google
Authenticate user with Google OAuth token.

**Request Body:**
```json
{
  "idToken": "google-id-token"
}
```

**Success Response (200):**
```json
{
  "success": true,
  "message": "Authentication successful",
  "data": {
    "token": "jwt-token",
    "user": {
      "id": "user-id",
      "email": "user@example.com",
      "name": "User Name",
      "profilePicture": "https://profile-pic-url"
    }
  }
}
```

**Error Responses:**
- `400`: Missing or invalid ID token
- `503`: Authentication service unavailable
- `500`: Internal server error

#### POST /api/auth/logout
Logout authenticated user.

**Headers:**
```
Authorization: Bearer <jwt-token>
```

**Success Response (200):**
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

### Profile Endpoints

#### GET /api/profile
Get authenticated user's profile information.

**Headers:**
```
Authorization: Bearer <jwt-token>
```

**Success Response (200):**
```json
{
  "success": true,
  "message": "Profile retrieved successfully",
  "data": {
    "user": {
      "id": "user-id",
      "email": "user@example.com",
      "name": "User Name",
      "profilePicture": "https://profile-pic-url",
      "createdAt": "2023-01-01T00:00:00.000Z",
      "updatedAt": "2023-01-01T00:00:00.000Z"
    }
  }
}
```

**Error Responses:**
- `401`: Invalid or missing token
- `404`: User not found
- `500`: Failed to fetch user information

### Health Check

#### GET /api/health
Check server health status.

**Response (200):**
```json
{
  "success": true,
  "message": "Server is running",
  "timestamp": "2023-01-01T00:00:00.000Z"
}
```

## Error Handling

All API responses follow a consistent format:

**Success Response:**
```json
{
  "success": true,
  "message": "Success message",
  "data": {
    // Response data
  }
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "Error message"
}
```

## Security Features

- **Helmet**: Security headers
- **CORS**: Cross-origin resource sharing
- **Rate Limiting**: 100 requests per 15 minutes per IP
- **JWT Authentication**: Stateless token-based auth
- **Input Validation**: Request body validation
- **Error Handling**: Secure error responses

## Development

### Available Scripts

- `npm run dev` - Start development server with hot reload
- `npm run build` - Build TypeScript to JavaScript
- `npm start` - Start production server
- `npm run lint` - Run ESLint
- `npm run clean` - Clean build directory

### Project Structure

```
backend/
├── src/
│   ├── config/
│   │   └── database.ts
│   ├── controllers/
│   │   ├── authController.ts
│   │   └── profileController.ts
│   ├── middleware/
│   │   ├── auth.ts
│   │   ├── errorHandler.ts
│   │   └── validation.ts
│   ├── models/
│   │   └── User.ts
│   ├── routes/
│   │   ├── auth.ts
│   │   ├── profile.ts
│   │   └── index.ts
│   ├── services/
│   │   └── googleAuth.ts
│   ├── utils/
│   │   └── jwt.ts
│   └── server.ts
├── package.json
├── tsconfig.json
├── .eslintrc.js
└── README.md
```
