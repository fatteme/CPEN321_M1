import { Request, Response, NextFunction, RequestHandler } from 'express';
import { z } from 'zod';

export const validateBody = <T>(schema: z.ZodSchema<T>): RequestHandler => {
  return (req: Request, res: Response, next: NextFunction) => {
    try {
      const validatedData = schema.parse(req.body);

      (req as any).body = validatedData;
      next();
    } catch (error: any) {
      if (error?.issues) {
        return res.status(400).json({
          error: 'Validation error',
          message: 'Invalid input data',
          details: error.issues.map((issue: any) => ({
            field: issue.path.join('.'),
            message: issue.message,
          })),
        });
      }

      return res.status(500).json({
        error: 'Internal server error',
        message: 'Validation processing failed',
      });
    }
  };
};

export const validateQuery = <T>(schema: z.ZodSchema<T>) => {
  return (req: Request, res: Response, next: NextFunction) => {
    try {
      const validatedData = schema.parse(req.query);
      // Type assertion for validated data
      (req as any).query = validatedData;
      next();
    } catch (error: any) {
      if (error?.issues) {
        return res.status(400).json({
          error: 'Query validation error',
          message: 'Invalid query parameters',
          details: error.issues.map((issue: any) => ({
            field: issue.path.join('.'),
            message: issue.message,
          })),
        });
      }

      return res.status(500).json({
        error: 'Internal server error',
        message: 'Query validation processing failed',
      });
    }
  };
};

export const validateParams = <T>(schema: z.ZodSchema<T>) => {
  return (req: Request, res: Response, next: NextFunction) => {
    try {
      const validatedData = schema.parse(req.params);
      // Type assertion for validated data
      (req as any).params = validatedData;
      next();
    } catch (error: any) {
      if (error?.issues) {
        return res.status(400).json({
          error: 'Parameter validation error',
          message: 'Invalid URL parameters',
          details: error.issues.map((issue: any) => ({
            field: issue.path.join('.'),
            message: issue.message,
          })),
        });
      }

      return res.status(500).json({
        error: 'Internal server error',
        message: 'Parameter validation processing failed',
      });
    }
  };
};
