import { NextFunction, Request, Response } from 'express';

import { HOBBIES } from '../constants';
import logger from '../utils/logger';

export const getAllHobbies = async (req: Request, res: Response, next: NextFunction) => {
  try {
    res.status(200).json({
      message: 'All hobbies fetched successfully',
      data: { hobbies: HOBBIES },
    });
  } catch (error) {
    logger.error('Failed to fetch available hobbies:', error);

    if (error instanceof Error) {
      return res.status(500).json({
        message: 'Failed to fetch available hobbies',
        error: error.message,
      });
    }

    next(error);
  }
};
