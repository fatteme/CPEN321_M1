import { NextFunction, Request, Response, Router } from 'express';

import { validateBody } from '../middleware/validation.middleware';
import { updateUserHobbiesSchema } from '../types/user.types';

import { userRepository } from '../repositories/user.repository';
import {
  GetProfileResponse,
  UpdateUserHobbiesRequest,
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
  '/hobbies',
  validateBody(updateUserHobbiesSchema),
  async (
    req: Request<{}, {}, UpdateUserHobbiesRequest>,
    res: Response<GetProfileResponse>,
    next: NextFunction
  ) => {
    try {
      const user = req.user!;
      const { hobbies } = req.body;

      const updatedUser = await userRepository.update(user._id, { hobbies });

      if (!updatedUser) {
        return res.status(404).json({
          message: 'User not found',
        });
      }

      res.status(200).json({
        message: 'User hobbies updated successfully',
        data: { user: updatedUser },
      });
    } catch (error) {
      logger.error('Failed to update user hobbies:', error);

      if (error instanceof Error) {
        return res.status(500).json({
          message: error.message || 'Failed to update user hobbies',
        });
      }

      next(error);
    }
  }
);

export default router;
