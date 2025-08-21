import express from 'express';
import { googleLogin, logout } from '../controllers/authController';
import { authenticateToken } from '../middleware/auth';
import { validateGoogleLogin } from '../middleware/validation';

const router = express.Router();

// POST /api/auth/google - Google OAuth login/register
router.post('/google', validateGoogleLogin, googleLogin);

// POST /api/auth/logout - Logout user
router.post('/logout', authenticateToken, logout);

export default router;
