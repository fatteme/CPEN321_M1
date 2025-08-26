import { Router } from 'express';

import { AuthController } from '../controllers/auth.controller';
import { validateBody } from '../middleware/validation.middleware';
import { authenticateUserSchema } from '../types/auth.types';

const router = Router();
const authController = new AuthController();

router.post(
  '/',
  validateBody(authenticateUserSchema),
  authController.authenticateUser
);

export default router;
