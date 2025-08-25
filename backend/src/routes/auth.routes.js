"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const auth_controller_1 = require("../controllers/auth.controller");
const validation_middleware_1 = require("../middleware/validation.middleware");
const auth_types_1 = require("../types/auth.types");
const router = (0, express_1.Router)();
router.post('/', (0, validation_middleware_1.validateBody)(auth_types_1.authenticateUserSchema), auth_controller_1.authenticateUser);
exports.default = router;
