import { Router } from 'express';

import { getProfile, updateUserHobbies } from '../controllers/user.controller';
import { validateBody } from '../middleware/validation.middleware';
import { updateUserHobbiesSchema } from '../types/user.types';

const router = Router();

router.get('/profile', getProfile);
router.post('/hobbies', validateBody(updateUserHobbiesSchema), updateUserHobbies);

export default router;
