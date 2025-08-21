import { Request, Response, NextFunction } from 'express';

export const validateGoogleLogin = (req: Request, res: Response, next: NextFunction): void => {
  const { idToken } = req.body;

  if (!idToken) {
    res.status(400).json({
      success: false,
      message: 'Google ID token is required',
    });
    return;
  }

  if (typeof idToken !== 'string' || idToken.trim().length === 0) {
    res.status(400).json({
      success: false,
      message: 'Invalid Google ID token format',
    });
    return;
  }

  next();
};
