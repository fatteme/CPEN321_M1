import { Router } from 'express';

import { getProfile, getAvailableHobbies, addHobby, removeHobby } from '../controllers/user.controller';
import { authenticateToken } from '../middleware/auth.middleware';
import { validateBody } from '../middleware/validation.middleware';
import { addHobbySchema, removeHobbySchema } from '../types/user.types';

const router = Router();

router.get('/profile', authenticateToken, getProfile);
router.get('/hobbies/available', getAvailableHobbies);
router.post('/hobbies/add', authenticateToken, validateBody(addHobbySchema), addHobby);
router.delete('/hobbies/remove', authenticateToken, validateBody(removeHobbySchema), removeHobby);

export default router;
