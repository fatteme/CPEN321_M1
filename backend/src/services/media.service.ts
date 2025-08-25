import fs from 'fs';
import path from 'path';

export class MediaService {
  private static readonly uploadsDir = 'uploads';
  private static readonly profilePicturesDir = path.join(this.uploadsDir, 'profile-pictures');

  static async saveImage(filePath: string, userId: string): Promise<string> {
    try {
      const fileExtension = path.extname(filePath);
      const fileName = `profile-${userId}-${Date.now()}${fileExtension}`;
      const newPath = path.join(this.profilePicturesDir, fileName);

      fs.renameSync(filePath, newPath);
      
      // Return the URL path that the frontend can use to access the image
      // The backend serves static files at /uploads, so we return the relative path from there
      return `/uploads/profile-pictures/${fileName}`;
    } catch (error) {
      if (fs.existsSync(filePath)) {
        fs.unlinkSync(filePath);
      }
      throw new Error(`Failed to save profile picture: ${error}`);
    }
  }

  static async deleteImage(url: string): Promise<void> {
    try {
      if (url && url.startsWith('/uploads/')) {
        const filePath = path.join(process.cwd(), url.substring(1));
        if (fs.existsSync(filePath)) {
          fs.unlinkSync(filePath);
        }
      }
    } catch (error) {
      console.error('Failed to delete old profile picture:', error);
    }
  }
}
