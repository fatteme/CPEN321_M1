"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const storage_1 = require("../config/storage");
const auth_middleware_1 = require("../middleware/auth.middleware");
const media_controller_1 = require("../controllers/media.controller");
const router = (0, express_1.Router)();
router.post('/upload', auth_middleware_1.authenticateToken, storage_1.upload.single('media'), media_controller_1.uploadImage);
exports.default = router;
