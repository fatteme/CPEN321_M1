import mongoose, { Document } from 'mongoose';
import z from 'zod';
import { HOBBIES, Hobby } from '../constants/hobbies';

export interface IUser extends Document {
  _id: mongoose.Types.ObjectId;
  googleId: string;
  email: string;
  name: string;
  profilePicture?: string;
  hobbies: Hobby[];
  createdAt: Date;
  updatedAt: Date;
}

export type GoogleUserInfo = {
  googleId: string;
  email: string;
  name: string;
  profilePicture?: string;
};

export const userSchema = z.object({
  email: z.string().email(),
  name: z.string().min(1),
  googleId: z.string().min(1),
  profilePicture: z.string().optional(),
  hobbies: z.array(z.string()).default([]),
});

// Create user
export type CreateUserSchema = z.infer<typeof userSchema>[
  | 'email'
  | 'name'
  | 'googleId'
  | 'profilePicture'];
export type UpdateUserSchema = z.infer<typeof userSchema>[
  | 'email'
  | 'name'
  | 'googleId'
  | 'profilePicture'];

// Update user profile picture
export const updateUserProfilePictureSchema = z.object({
  profilePicture: z.string().min(1),
});

export type UpdateUserProfilePictureSchema = z.infer<
  typeof updateUserProfilePictureSchema
>;

// Update user hobbies
export const updateUserHobbiesSchema = z.object({
  hobbies: z
    .array(z.string())
    .min(1)
    .refine(val => val.every(v => HOBBIES.includes(v)), {
      message: 'Hobby must be in the available hobbies list',
    }),
});

export type UpdateUserHobbiesSchema = z.infer<typeof updateUserHobbiesSchema>;
