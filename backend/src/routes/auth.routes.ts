import { Router } from 'express';

import { AuthController } from '../controllers/auth.controller';
import { validateBody } from '../middleware/validation.middleware';
import { authenticateUserSchema } from '../types/auth.types';

const router = Router();
const authController = new AuthController();

router.post(
  '/signup',
  validateBody(authenticateUserSchema),
  authController.signUp
);

router.post(
  '/signin',
  validateBody(authenticateUserSchema),
  authController.signIn
);

export default router;
