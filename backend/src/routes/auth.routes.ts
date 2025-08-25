import { Router } from 'express';

import { authenticateUser } from '../controllers/auth.controller';
import { validateBody } from '../middleware/validation.middleware';
import { authenticateUserSchema } from '../types/auth.types';

const router = Router();

router.post('/', validateBody(authenticateUserSchema), authenticateUser);

export default router;
