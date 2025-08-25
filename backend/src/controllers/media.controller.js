"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.uploadImage = void 0;
const media_service_1 = require("../services/media.service");
const uploadImage = async (req, res) => {
    try {
        if (!req.file) {
            return res.status(400).json({
                success: false,
                message: 'No file uploaded'
            });
        }
        const userId = req.user?._id;
        if (!userId) {
            return res.status(401).json({
                success: false,
                message: 'User not authenticated'
            });
        }
        const image = await media_service_1.MediaService.saveImage(req.file.path, userId);
        res.status(200).json({
            success: true,
            message: 'Image uploaded successfully',
            data: {
                image
            }
        });
    }
    catch (error) {
        console.error('Error uploading profile picture:', error);
        res.status(500).json({
            success: false,
            message: 'Failed to upload profile picture',
            error: error instanceof Error ? error.message : 'Unknown error'
        });
    }
};
exports.uploadImage = uploadImage;
