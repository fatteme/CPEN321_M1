import { Router } from 'express';

import { validateBody } from '../middleware/validation.middleware';
import {
  updateUserHobbiesSchema,
  updateUserProfilePictureSchema,
} from '../types/user.types';
import { UserController } from '../controllers/user.controller';

const router = Router();
const userController = new UserController();

router.get('/profile', userController.getProfile);

router.post(
  '/hobbies',
  validateBody(updateUserHobbiesSchema),
  userController.updateUserHobbies
);

router.post(
  '/profile-picture',
  validateBody(updateUserProfilePictureSchema),
  userController.updateUserProfilePicture
);

router.delete('/profile-picture', userController.deleteUserProfilePicture);

export default router;
