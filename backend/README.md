# Backend API

## Profile Picture Management

This backend now supports profile picture upload, retrieval, and deletion using local file system storage.

### API Endpoints

#### 1. Upload Profile Picture
- **POST** `/api/profile-picture/upload`
- **Authentication**: Required (Bearer token)
- **Content-Type**: `multipart/form-data`
- **Body**: 
  - `profilePicture`: Image file (JPG, PNG, GIF, etc.)
- **File Size Limit**: 5MB
- **Response**:
```json
{
  "success": true,
  "message": "Profile picture uploaded successfully",
  "data": {
    "profilePictureUrl": "/uploads/profile-pictures/profile-123-1234567890.jpg"
  }
}
```

#### 2. Get Profile Picture URL
- **GET** `/api/profile-picture`
- **Authentication**: Required (Bearer token)
- **Response**:
```json
{
  "success": true,
  "data": {
    "profilePictureUrl": "/uploads/profile-pictures/profile-123-1234567890.jpg"
  }
}
```

#### 3. Delete Profile Picture
- **DELETE** `/api/profile-picture`
- **Authentication**: Required (Bearer token)
- **Response**:
```json
{
  "success": true,
  "message": "Profile picture deleted successfully"
}
```

### File Storage

- Profile pictures are stored in the `uploads/profile-pictures/` directory
- Files are automatically named with format: `profile-{userId}-{timestamp}.{extension}`
- Old profile pictures are automatically deleted when a new one is uploaded
- Images are served statically at `/uploads/profile-pictures/{filename}`

### Security Features

- Only authenticated users can upload/access profile pictures
- File type validation (images only)
- File size limit (5MB)
- Automatic cleanup of old files
- Unique file naming to prevent conflicts

### Usage Example

```javascript
// Frontend example using FormData
const formData = new FormData();
formData.append('profilePicture', fileInput.files[0]);

const response = await fetch('/api/profile-picture/upload', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`
  },
  body: formData
});

const result = await response.json();
console.log('Profile picture URL:', result.data.profilePictureUrl);
```

### File Structure

```
uploads/
└── profile-pictures/
    ├── profile-123-1234567890.jpg
    ├── profile-456-1234567891.png
    └── ...
```

### Notes

- The `uploads/` directory is automatically created if it doesn't exist
- Uploaded files are excluded from git (see .gitignore)
- Profile pictures are automatically resized and optimized
- Old profile pictures are automatically cleaned up to save disk space
