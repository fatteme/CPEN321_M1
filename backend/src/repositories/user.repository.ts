import { z } from 'zod';

import { User } from '../models/user.model';
import type { GoogleUserInfo, IUser } from '../types/user.types';
import { userSchema } from '../types/user.types';
import logger from '../utils/logger';
import mongoose from 'mongoose';

export class UserRepository {

    async findById(_id: mongoose.Types.ObjectId): Promise<IUser | null> {
        try {
            const user = await User.findOne({ _id });

            if (!user) {
                return null;
            }

            return user;
        } catch (error) {
            console.error('Error finding user by Google ID:', error);
            throw new Error('Failed to find user');
        }
    }
    
    async findByGoogleId(googleId: string): Promise<IUser | null> {
        try {
            const user = await User.findOne({ googleId });

            if (!user) {
                return null;
            }

            return user;
        } catch (error) {
            console.error('Error finding user by Google ID:', error);
            throw new Error('Failed to find user');
        }
    }

    async create(userInfo: GoogleUserInfo): Promise<IUser> {
        try {
            const validatedData = userSchema.parse(userInfo);

            const user = await User.create(validatedData);

            if (!user) {
                throw new Error('User not found');
            }

            return user;
        } catch (error) {
            if (error instanceof z.ZodError) {
                console.error('Validation error:', (error as any).errors);
                throw new Error('Invalid update data');
            }
            console.error('Error updating user:', error);
            throw new Error('Failed to update user');
        }
    }

    async update(userId: mongoose.Types.ObjectId, user: Partial<IUser>): Promise<IUser | null> {
        try {
            const updatedUser = await User.findByIdAndUpdate(userId, user);
            return updatedUser;
        } catch (error) {
            logger.error('Error updating user:', error);
            throw new Error('Failed to update user');
        }
    }

    async findOrCreateUser(googleUserInfo: GoogleUserInfo): Promise<IUser> {
        try {
            logger.info('Finding or creating user:', googleUserInfo);

            let user = await this.findByGoogleId(googleUserInfo.googleId);
            
            if (user) {
                return user;
            }
            
            return await this.create(googleUserInfo);
        } catch (error) {
            logger.error('Error in findOrCreateUser:', error);
            throw new Error('Failed to process user');
        }
    }
}

export const userRepository = new UserRepository();
