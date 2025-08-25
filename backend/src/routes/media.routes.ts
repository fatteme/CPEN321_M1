import { Router } from 'express';

import { upload } from '../config/storage';
import { authenticateToken } from '../middleware/auth.middleware';
import { uploadImage } from '../controllers/media.controller';

const router = Router();

router.post('/upload', authenticateToken, upload.single('media'), uploadImage);

export default router;
