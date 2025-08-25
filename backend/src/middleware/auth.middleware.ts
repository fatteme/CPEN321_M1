import { NextFunction, Request, RequestHandler, Response } from 'express';
import jwt from 'jsonwebtoken';

import { userRepository } from '../repositories/user.repository';
import { IUser } from '../types/user.types';
import logger from '../utils/logger';

export const authenticateToken: RequestHandler = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  try {
    const authHeader = req.headers.authorization
    logger.info('Auth header:', authHeader);
    const token = authHeader && authHeader.split(' ')[1];
    logger.info('Token:', token);

    if (!token) {
      res.status(401).json({
        error: 'Access denied',
        message: 'No token provided'
      });
      return;
    }

    const decoded = jwt.verify(token, process.env.JWT_SECRET!) as IUser;
    logger.info('Decoded token:', decoded);

    if (!decoded || !decoded._id) {
      res.status(401).json({
        error: 'Invalid token',
        message: 'Token verification failed'
      });
      return;
    }

    const user = await userRepository.findById(decoded._id);
    
    if (!user) {
      res.status(401).json({
        error: 'User not found',
        message: 'Token is valid but user no longer exists'
      });
      return;
    }

    req.user = user;

    next();
  } catch (error) {
    if (error instanceof jwt.JsonWebTokenError) {
      res.status(401).json({
        error: 'Invalid token',
        message: 'Token is malformed or expired'
      });
      return;
    }

    if (error instanceof jwt.TokenExpiredError) {
      res.status(401).json({
        error: 'Token expired',
        message: 'Please login again'
      });
      return;
    }

    console.error('Auth middleware error:', error);
    res.status(500).json({
      error: 'Authentication error',
      message: 'Internal server error during authentication'
    });
    return;
  }
};
