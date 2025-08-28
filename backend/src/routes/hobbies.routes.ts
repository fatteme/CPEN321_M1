import { NextFunction, Request, Response, Router } from 'express';

import { HOBBIES } from '../constants';
import { GetAllHobbiesResponse } from '../types/hobby.types';
import logger from '../utils/logger';

const router = Router();

router.get(
  '/',
  (req: Request, res: Response<GetAllHobbiesResponse>, next: NextFunction) => {
    try {
      res.status(200).json({
        message: 'All hobbies fetched successfully',
        data: { hobbies: HOBBIES },
      });
    } catch (error) {
      logger.error('Failed to fetch available hobbies:', error);

      if (error instanceof Error) {
        return res.status(500).json({
          message: error.message || 'Failed to fetch available hobbies',
        });
      }

      next(error);
    }
  }
);

export default router;
