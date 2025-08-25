"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.authenticateUser = void 0;
const auth_service_1 = require("../services/auth.service");
const logger_1 = __importDefault(require("../utils/logger"));
const authenticateUser = async (req, res, next) => {
    try {
        const { idToken } = req.body;
        const data = await auth_service_1.authService.authenticateWithGoogle(idToken);
        return res.status(200).json({
            message: 'Authentication successful',
            data,
        });
    }
    catch (error) {
        logger_1.default.error('Google authentication error:', error);
        if (error instanceof Error) {
            if (error.message === 'Invalid Google token') {
                return res.status(401).json({
                    message: 'Invalid Google token'
                });
            }
            if (error.message === 'Failed to process user') {
                return res.status(500).json({
                    message: 'Failed to process user information'
                });
            }
        }
        // Let the global error handler deal with unknown errors
        next(error);
    }
};
exports.authenticateUser = authenticateUser;
