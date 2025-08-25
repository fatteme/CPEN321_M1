"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.authenticateToken = void 0;
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
const user_repository_1 = require("../repositories/user.repository");
const logger_1 = __importDefault(require("../utils/logger"));
const authenticateToken = async (req, res, next) => {
    try {
        const authHeader = req.headers.authorization;
        logger_1.default.info('Auth header:', authHeader);
        const token = authHeader && authHeader.split(' ')[1];
        logger_1.default.info('Token:', token);
        if (!token) {
            res.status(401).json({
                error: 'Access denied',
                message: 'No token provided'
            });
            return;
        }
        const decoded = jsonwebtoken_1.default.verify(token, process.env.JWT_SECRET);
        logger_1.default.info('Decoded token:', decoded);
        if (!decoded || !decoded._id) {
            res.status(401).json({
                error: 'Invalid token',
                message: 'Token verification failed'
            });
            return;
        }
        const user = await user_repository_1.userRepository.findById(decoded._id);
        if (!user) {
            res.status(401).json({
                error: 'User not found',
                message: 'Token is valid but user no longer exists'
            });
            return;
        }
        req.user = user;
        next();
    }
    catch (error) {
        if (error instanceof jsonwebtoken_1.default.JsonWebTokenError) {
            res.status(401).json({
                error: 'Invalid token',
                message: 'Token is malformed or expired'
            });
            return;
        }
        if (error instanceof jsonwebtoken_1.default.TokenExpiredError) {
            res.status(401).json({
                error: 'Token expired',
                message: 'Please login again'
            });
            return;
        }
        console.error('Auth middleware error:', error);
        res.status(500).json({
            error: 'Authentication error',
            message: 'Internal server error during authentication'
        });
        return;
    }
};
exports.authenticateToken = authenticateToken;
