import express from 'express';
import { getProfile } from '../controllers/profileController';
import { authenticateToken } from '../middleware/auth';

const router = express.Router();

// GET /api/profile - Get user profile
router.get('/', authenticateToken, getProfile);

export default router;
