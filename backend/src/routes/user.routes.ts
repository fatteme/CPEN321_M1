import { NextFunction, Request, Response, Router } from 'express';

import { validateBody } from '../middleware/validation.middleware';
import { userRepository } from '../repositories/user.repository';
import { MediaService } from '../services/media.service';
import {
  GetProfileResponse,
  UpdateProfileRequest,
  updateProfileSchema,
} from '../types/user.types';
import logger from '../utils/logger';

const router = Router();

router.get(
  '/profile',
  (req: Request<{}, {}, {}>, res: Response<GetProfileResponse>) => {
    const user = req.user!;

    res.status(200).json({
      message: 'Profile fetched successfully',
      data: { user },
    });
  }
);

router.post(
  '/profile',
  validateBody(updateProfileSchema),
  async (
    req: Request<{}, {}, UpdateProfileRequest>,
    res: Response<GetProfileResponse>,
    next: NextFunction
  ) => {
    try {
      const user = req.user!;

      const updatedUser = await userRepository.update(user._id, req.body);

      if (!updatedUser) {
        return res.status(404).json({
          message: 'User not found',
        });
      }

      res.status(200).json({
        message: 'User info updated successfully',
        data: { user: updatedUser },
      });
    } catch (error) {
      logger.error('Failed to update user info:', error);

      if (error instanceof Error) {
        return res.status(500).json({
          message: error.message || 'Failed to update user info',
        });
      }

      next(error);
    }
  }
);

router.delete(
  '/profile',
  async (req: Request, res: Response, next: NextFunction) => {
    try {
      const user = req.user!;

      await MediaService.deleteAllUserImages(user._id.toString());

      await userRepository.delete(user._id);

      res.status(200).json({
        message: 'User deleted successfully',
      });
    } catch (error) {
      logger.error('Failed to delete user:', error);

      if (error instanceof Error) {
        return res.status(500).json({
          message: error.message || 'Failed to delete user',
        });
      }

      next(error);
    }
  }
);

export default router;
