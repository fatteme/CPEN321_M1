import { Router } from "express";

import { authenticateToken } from "../middleware/auth.middleware";
import authRoutes from "./auth.routes";
import hobbiesRoutes from "./hobbies.routes";
import usersRoutes from "./user.routes";

const router = Router();

router.use('/auth', authRoutes);

router.use('/hobbies', authenticateToken, hobbiesRoutes);

router.use('/user', authenticateToken, usersRoutes);


export default router;