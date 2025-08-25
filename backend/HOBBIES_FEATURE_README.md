# Hobbies Feature Implementation

This document describes the implementation of the hobbies feature for user profiles.

## Overview

The hobbies feature allows users to:
- View a list of available hobbies
- Add hobbies to their profile
- Remove hobbies from their profile
- See their hobbies in their profile

## Implementation Details

### 1. Data Model Changes

**User Model (`src/models/user.model.ts`)**
- Added `hobbies` field as an array of strings
- Field has validation to ensure all hobbies are non-empty strings
- Default value is an empty array

**User Types (`src/types/user.types.ts`)**
- Updated `IUser` interface to include `hobbies: Hobby[]`
- Added validation schemas for add/remove hobby operations
- Added new types for hobby management requests

### 2. Constants

**Available Hobbies (`src/constants/hobbies.ts`)**
- Hardcoded list of 50+ available hobbies
- Includes activities like Reading, Writing, Photography, Cooking, etc.
- Type-safe with TypeScript const assertions

### 3. API Endpoints

**New Routes (`src/routes/user.routes.ts`)**
- `GET /api/users/hobbies/available` - Get list of available hobbies
- `POST /api/users/hobbies/add` - Add hobby to user profile
- `DELETE /api/users/hobbies/remove` - Remove hobby from user profile

**Existing Routes Updated**
- `GET /api/users/profile` - Now includes hobbies in response

### 4. Controller Methods

**New Methods (`src/controllers/user.controller.ts`)**
- `getAvailableHobbies()` - Returns list of available hobbies
- `addHobby()` - Adds hobby to user profile with validation
- `removeHobby()` - Removes hobby from user profile

**Features:**
- Validation that hobby exists in available list
- Prevents duplicate hobbies
- Proper error handling and responses
- Authentication required for profile modifications

### 5. Database Migration

**Migration Utility (`src/utils/migrate.ts`)**
- Automatically runs on app startup
- Ensures existing users get `hobbies` field initialized
- Safe to run multiple times

## API Usage Examples

### Get Available Hobbies
```bash
GET /api/users/hobbies/available
```
**Response:**
```json
{
  "message": "Available hobbies fetched successfully",
  "data": {
    "hobbies": ["Reading", "Writing", "Photography", ...]
  }
}
```

### Add Hobby to Profile
```bash
POST /api/users/hobbies/add
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "hobby": "Reading"
}
```
**Response:**
```json
{
  "message": "Hobby added successfully",
  "data": {
    "user": {
      "_id": "...",
      "name": "John Doe",
      "email": "john@example.com",
      "hobbies": ["Reading"],
      ...
    }
  }
}
```

### Remove Hobby from Profile
```bash
DELETE /api/users/hobbies/remove
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "hobby": "Reading"
}
```

### Get User Profile (Updated)
```bash
GET /api/users/profile
Authorization: Bearer <jwt_token>
```
**Response now includes hobbies:**
```json
{
  "message": "Profile fetched successfully",
  "data": {
    "user": {
      "_id": "...",
      "name": "John Doe",
      "email": "john@example.com",
      "hobbies": ["Reading", "Photography"],
      ...
    }
  }
}
```

## Validation Rules

1. **Hobby Addition:**
   - Must be authenticated
   - Hobby must exist in available hobbies list
   - Cannot add duplicate hobbies
   - Hobby name must be non-empty string

2. **Hobby Removal:**
   - Must be authenticated
   - Hobby will be removed if it exists (no error if not found)

3. **Data Integrity:**
   - Hobbies are stored as strings
   - Array validation ensures all elements are non-empty strings
   - MongoDB `$addToSet` prevents duplicates

## Error Handling

- **400 Bad Request:** Invalid hobby, duplicate hobby, validation errors
- **401 Unauthorized:** Missing or invalid authentication token
- **404 Not Found:** User not found
- **500 Internal Server Error:** Database or server errors

## Security Features

- All profile modification endpoints require authentication
- Input validation using Zod schemas
- MongoDB injection protection through Mongoose
- JWT token validation middleware

## Testing

Run the test script to see API usage examples:
```bash
node src/test-hobbies.js
```

## Future Enhancements

Potential improvements could include:
- Hobby categories/tags
- Hobby popularity metrics
- User hobby matching
- Hobby-based recommendations
- Hobby groups/communities
