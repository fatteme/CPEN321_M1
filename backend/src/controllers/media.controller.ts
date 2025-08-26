import { NextFunction, Request, Response } from 'express';

import { MediaService } from '../services/media.service';
import logger from '../utils/logger';

export const uploadImage = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  try {
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
        message: 'Failed to upload profile picture',
        error: error.message,
      });
    }

    next(error);
  }
};
