import { RequestHandler } from "express";

import { authService } from "../services/auth.service";
import logger from "../utils/logger";

export const authenticateUser: RequestHandler = async (req, res, next) => {
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
          message: 'Invalid Google token'
        });
      }
      
      if (error.message === 'Failed to process user') {
        return res.status(500).json({
          message: 'Failed to process user information'
        });
      }
    }

    // Let the global error handler deal with unknown errors
    next(error);
  }
};

