import mongoose, { Schema } from 'mongoose';

import type { IUser } from '../types/user.types';
import { HOBBIES } from '../constants';

const userSchema = new Schema<IUser>(
  {
    googleId: {
      type: String,
      required: true,
      unique: true,
      index: true,
    },
    email: {
      type: String,
      required: true,
      unique: true,
      lowercase: true,
      trim: true,
    },
    name: {
      type: String,
      required: true,
      trim: true,
    },
    profilePicture: {
      type: String,
      required: false,
      trim: true,
    },
    bio: {
      type: String,
      required: false,
      trim: true,
      maxlength: 500,
    },
    hobbies: {
      type: [String],
      default: [],
      validate: {
        validator: function (hobbies: string[]) {
          return hobbies.every(hobby => HOBBIES.includes(hobby));
        },
        message:
          'Hobbies must be non-empty strings and must be in the available hobbies list',
      },
    },
  },
  {
    timestamps: true,
  }
);

export const User = mongoose.model<IUser>('User', userSchema);
