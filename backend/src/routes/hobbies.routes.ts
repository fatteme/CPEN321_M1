import { Router } from 'express';

import { getAllHobbies } from '../controllers/hobby.controller';

const router = Router();

router.get('/', getAllHobbies);

export default router;
