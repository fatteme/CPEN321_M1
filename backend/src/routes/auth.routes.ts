import { NextFunction, Request, Response, Router } from 'express';

import { validateBody } from '../middleware/validation.middleware';
import { authService } from '../services/auth.service';
import {
  AuthenticateUserRequest,
  AuthenticateUserResponse,
  authenticateUserSchema,
} from '../types/auth.types';
import logger from '../utils/logger';

const router = Router();

router.post(
  '/',
  validateBody(authenticateUserSchema),
  async (
    req: Request<{}, {}, AuthenticateUserRequest>,
    res: Response<AuthenticateUserResponse>,
    next: NextFunction
  ) => {
    try {
      const { idToken } = req.body;

      const data = await authService.authenticateWithGoogle(idToken);

      return res.status(200).json({
        message: 'Authentication successful',
        data,
      });
    } catch (error) {
      logger.error('Google authentication error:', error);

      if (error instanceof Error) {
        if (error.message === 'Invalid Google token') {
          return res.status(401).json({
            message: 'Invalid Google token',
          });
        }

        if (error.message === 'Failed to process user') {
          return res.status(500).json({
            message: 'Failed to process user information',
          });
        }
      }

      next(error);
    }
  }
);

export default router;
