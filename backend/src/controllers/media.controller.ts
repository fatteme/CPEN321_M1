import { Request, Response } from 'express';
import { MediaService } from '../services/media.service';

export const uploadImage = async (req: Request, res: Response) => {
  try {
    if (!req.file) {
      return res.status(400).json({
        success: false,
        message: 'No file uploaded',
      });
    }

    const userId = (req as any).user?._id;
    if (!userId) {
      return res.status(401).json({
        success: false,
        message: 'User not authenticated',
      });
    }

    const image = await MediaService.saveImage(req.file.path, userId);

    res.status(200).json({
      success: true,
      message: 'Image uploaded successfully',
      data: {
        image,
      },
    });
  } catch (error) {
    console.error('Error uploading profile picture:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to upload profile picture',
      error: error instanceof Error ? error.message : 'Unknown error',
    });
  }
};
