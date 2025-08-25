import { Router } from "express";

import authRoutes from "./auth.routes";
import usersRoutes from "./user.routes";

const router = Router();

router.use('/auth', authRoutes);

router.use('/user', usersRoutes);

export default router;