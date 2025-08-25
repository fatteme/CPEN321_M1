"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.MediaService = void 0;
const fs_1 = __importDefault(require("fs"));
const path_1 = __importDefault(require("path"));

class MediaService {
    static uploadsDir = 'uploads';
    static profilePicturesDir = path_1.default.join(this.uploadsDir, 'profile-pictures');
    
    static async saveImage(filePath, userId) {
        try {
            const fileExtension = path_1.default.extname(filePath);
            const fileName = `profile-${userId}-${Date.now()}${fileExtension}`;
            const newPath = path_1.default.join(this.profilePicturesDir, fileName);
            fs_1.default.renameSync(filePath, newPath);
            
            // Return the URL path that the frontend can use to access the image
            // The backend serves static files at /uploads, so we return the relative path from there
            return `/uploads/profile-pictures/${fileName}`;
        }
        catch (error) {
            if (fs_1.default.existsSync(filePath)) {
                fs_1.default.unlinkSync(filePath);
            }
            throw new Error(`Failed to save profile picture: ${error}`);
        }
    }
    static async deleteImage(url) {
        try {
            if (url && url.startsWith('/uploads/')) {
                const filePath = path_1.default.join(process.cwd(), url.substring(1));
                if (fs_1.default.existsSync(filePath)) {
                    fs_1.default.unlinkSync(filePath);
                }
            }
        }
        catch (error) {
            console.error('Failed to delete old profile picture:', error);
        }
    }
}
exports.MediaService = MediaService;
