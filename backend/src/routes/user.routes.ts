import { Router } from 'express';

import { deleteUserProfilePicture, getProfile, updateUserHobbies, updateUserProfilePicture } from '../controllers/user.controller';
import { validateBody } from '../middleware/validation.middleware';
import { updateUserHobbiesSchema, updateUserProfilePictureSchema } from '../types/user.types';

const router = Router();

router.get('/profile', getProfile);
router.post('/hobbies', validateBody(updateUserHobbiesSchema), updateUserHobbies);
router.post('/profile-picture', validateBody(updateUserProfilePictureSchema), updateUserProfilePicture);
router.delete('/profile-picture', deleteUserProfilePicture);

export default router;
