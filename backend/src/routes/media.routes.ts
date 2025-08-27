import { NextFunction, Request, Response, Router } from 'express';

import { upload } from '../config/storage';
import { authenticateToken } from '../middleware/auth.middleware';

import { MediaService } from '../services/media.service';
import logger from '../utils/logger';

import { UploadImageRequest, UploadImageResponse } from '../types/media.types';

const router = Router();

router.post(
  '/upload',
  authenticateToken,
  upload.single('media'),
  async (
    req: Request<{}, {}, UploadImageRequest>,
    res: Response<UploadImageResponse>,
    next: NextFunction
  ) => {
    try {
      console.log('req.file', req.file);
      if (!req.file) {
        return res.status(400).json({
          message: 'No file uploaded',
        });
      }

      const user = req.user!;
      const image = await MediaService.saveImage(
        req.file.path,
        user._id.toString()
      );

      console.log('image', image);

      res.status(200).json({
        message: 'Image uploaded successfully',
        data: {
          image,
        },
      });
    } catch (error) {
      logger.error('Error uploading profile picture:', error);

      if (error instanceof Error) {
        return res.status(500).json({
          message: error.message || 'Failed to upload profile picture',
        });
      }

      next(error);
    }
  }
);

export default router;
