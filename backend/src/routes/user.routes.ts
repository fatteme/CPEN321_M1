import { Router } from 'express';

import { validateBody } from '../middleware/validation.middleware';
import { updateProfileSchema } from '../types/user.types';
import { UserController } from '../controllers/user.controller';

const router = Router();
const userController = new UserController();

router.get('/profile', userController.getProfile);

router.post(
  '/profile',
  validateBody(updateProfileSchema),
  userController.updateProfile
);

export default router;
