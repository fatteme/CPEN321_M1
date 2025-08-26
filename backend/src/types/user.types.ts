import mongoose, { Document } from 'mongoose';
import z from 'zod';
import { HOBBIES } from '../constants';

// User model
// ------------------------------------------------------------
export interface IUser extends Document {
  _id: mongoose.Types.ObjectId;
  googleId: string;
  email: string;
  name: string;
  profilePicture?: string;
  hobbies: string[];
  createdAt: Date;
  updatedAt: Date;
}

// Zod schemas
// ------------------------------------------------------------
export const userSchema = z.object({
  email: z.string().email(),
  name: z.string().min(1),
  googleId: z.string().min(1),
  profilePicture: z.string().optional(),
  hobbies: z.array(z.string()).default([]),
});

export const updateUserProfilePictureSchema = z.object({
  profilePicture: z.string().min(1),
});

export const updateUserHobbiesSchema = z.object({
  hobbies: z
    .array(z.string())
    .min(1)
    .refine(val => val.every(v => HOBBIES.includes(v)), {
      message: 'Hobby must be in the available hobbies list',
    }),
});

// Request types
// ------------------------------------------------------------
export type GetProfileResponse = {
  message: string;
  data?: {
    user: IUser;
  };
};

export type UpdateUserHobbiesRequest = z.infer<typeof updateUserHobbiesSchema>;

export type UpdateUserProfilePictureRequest = z.infer<
  typeof updateUserProfilePictureSchema
>;

// Generic types
// ------------------------------------------------------------
export type GoogleUserInfo = {
  googleId: string;
  email: string;
  name: string;
  profilePicture?: string;
};
