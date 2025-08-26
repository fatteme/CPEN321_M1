import { z } from 'zod';
import { Request } from 'express';

import { IUser } from './user.types';

// Zod schemas
export const authenticateUserSchema = z.object({
  idToken: z.string().min(1, 'Google token is required'),
});

// Request types
export type AuthenticateUserRequest = z.infer<typeof authenticateUserSchema> &
  Request;

// Response types
export interface AuthenticateUserResponse {
  message: string;
  data?: AuthResult;
}

// Generic types
export interface AuthResult {
  token: string;
  user: IUser;
}

declare global {
  namespace Express {
    interface Request {
      user?: IUser;
    }
  }
}
