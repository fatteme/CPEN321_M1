import mongoose from 'mongoose';
import { z } from 'zod';

import { User } from '../models/user.model';
import type { GoogleUserInfo, IUser } from '../types/user.types';
import { userSchema } from '../types/user.types';
import logger from '../utils/logger';

export class UserRepository {
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
        console.error('Validation error:', error.issues);
        throw new Error('Invalid update data');
      }
      console.error('Error updating user:', error);
      throw new Error('Failed to update user');
    }
  }

  async update(
    userId: mongoose.Types.ObjectId,
    user: Partial<IUser>
  ): Promise<IUser | null> {
    try {
      const validatedData = userSchema.partial().parse(user);

      const updatedUser = await User.findByIdAndUpdate(userId, validatedData, {
        new: true,
      });
      return updatedUser;
    } catch (error) {
      logger.error('Error updating user:', error);
      throw new Error('Failed to update user');
    }
  }

  async delete(userId: mongoose.Types.ObjectId): Promise<void> {
    try {
      await User.findByIdAndDelete(userId);
    } catch (error) {
      logger.error('Error deleting user:', error);
      throw new Error('Failed to delete user');
    }
  }

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
}

export const userRepository = new UserRepository();
