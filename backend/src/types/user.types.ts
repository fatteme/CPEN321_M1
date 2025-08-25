import mongoose, { Document } from "mongoose";
import z from "zod";
import { Hobby } from "../constants/hobbies";

export interface IUser extends Document {
  _id: mongoose.Types.ObjectId;
  googleId: string;
  email: string;
  name: string;
  profilePictureUrl: string;
  hobbies: Hobby[];
  createdAt: Date;
  updatedAt: Date;
}

export type GoogleUserInfo = {
  googleId: string;
  email: string;
  name: string;
  profilePictureUrl?: string;
}

export const userSchema = z.object({
  email: z.string().email(),
  name: z.string().min(1),
  googleId: z.string().min(1),
  profilePictureUrl: z.string().optional(),
  hobbies: z.array(z.string()).default([]),
});

export type CreateUserSchema = z.infer<typeof userSchema>['email' | 'name'| 'googleId'| 'profilePictureUrl'];
export type UpdateUserSchema = z.infer<typeof userSchema>['email' | 'name'| 'googleId'| 'profilePictureUrl'];

// Hobbies management types
export const addHobbySchema = z.object({
  hobby: z.string().min(1).refine((val) => true, { message: "Hobby validation will be done in controller" })
});

export const removeHobbySchema = z.object({
  hobby: z.string().min(1).refine((val) => true, { message: "Hobby validation will be done in controller" })
});

export type AddHobbyRequest = z.infer<typeof addHobbySchema>;
export type RemoveHobbyRequest = z.infer<typeof removeHobbySchema>;
