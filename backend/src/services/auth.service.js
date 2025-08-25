"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.authService = exports.AuthService = void 0;
const google_auth_library_1 = require("google-auth-library");
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
const user_repository_1 = require("../repositories/user.repository");
const logger_1 = __importDefault(require("../utils/logger"));
class AuthService {
    googleClient;
    constructor() {
        this.googleClient = new google_auth_library_1.OAuth2Client(process.env.GOOGLE_CLIENT_ID);
    }
    async verifyGoogleToken(idToken) {
        try {
            const ticket = await this.googleClient.verifyIdToken({
                idToken,
                audience: process.env.GOOGLE_CLIENT_ID
            });
            const payload = ticket.getPayload();
            if (!payload) {
                throw new Error('Invalid token payload');
            }
            if (!payload.email || !payload.name) {
                throw new Error('Missing required user information from Google');
            }
            return {
                googleId: payload.sub,
                email: payload.email,
                name: payload.name,
                profilePictureUrl: payload.picture,
            };
        }
        catch (error) {
            logger_1.default.error('Google token verification failed:', error);
            throw new Error('Invalid Google token');
        }
    }
    generateAccessToken(user) {
        return jsonwebtoken_1.default.sign(JSON.stringify(user), process.env.JWT_SECRET);
    }
    async authenticateWithGoogle(idToken) {
        try {
            const googleUserInfo = await this.verifyGoogleToken(idToken);
            const user = await user_repository_1.userRepository.findOrCreateUser(googleUserInfo);
            logger_1.default.info('User authenticated:', user);
            const token = this.generateAccessToken(user);
            return { token, user };
        }
        catch (error) {
            logger_1.default.error('Authentication failed:', error);
            throw error;
        }
    }
}
exports.AuthService = AuthService;
exports.authService = new AuthService();
