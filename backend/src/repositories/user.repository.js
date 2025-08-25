"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.userRepository = exports.UserRepository = void 0;
const zod_1 = require("zod");
const user_model_1 = require("../models/user.model");
const user_types_1 = require("../types/user.types");
const logger_1 = __importDefault(require("../utils/logger"));
class UserRepository {
    async findById(_id) {
        try {
            const user = await user_model_1.User.findOne({ _id });
            if (!user) {
                return null;
            }
            return user;
        }
        catch (error) {
            console.error('Error finding user by Google ID:', error);
            throw new Error('Failed to find user');
        }
    }
    async findByGoogleId(googleId) {
        try {
            const user = await user_model_1.User.findOne({ googleId });
            if (!user) {
                return null;
            }
            return user;
        }
        catch (error) {
            console.error('Error finding user by Google ID:', error);
            throw new Error('Failed to find user');
        }
    }
    async create(userInfo) {
        try {
            const validatedData = user_types_1.userSchema.parse(userInfo);
            const user = await user_model_1.User.create(validatedData);
            if (!user) {
                throw new Error('User not found');
            }
            return user;
        }
        catch (error) {
            if (error instanceof zod_1.z.ZodError) {
                console.error('Validation error:', error.errors);
                throw new Error('Invalid update data');
            }
            console.error('Error updating user:', error);
            throw new Error('Failed to update user');
        }
    }
    async update(userId, user) {
        try {
            const updatedUser = await user_model_1.User.findByIdAndUpdate(userId, user);
            return updatedUser;
        }
        catch (error) {
            logger_1.default.error('Error updating user:', error);
            throw new Error('Failed to update user');
        }
    }
    async findOrCreateUser(googleUserInfo) {
        try {
            logger_1.default.info('Finding or creating user:', googleUserInfo);
            let user = await this.findByGoogleId(googleUserInfo.googleId);
            if (user) {
                return user;
            }
            return await this.create(googleUserInfo);
        }
        catch (error) {
            logger_1.default.error('Error in findOrCreateUser:', error);
            throw new Error('Failed to process user');
        }
    }
}
exports.UserRepository = UserRepository;
exports.userRepository = new UserRepository();
