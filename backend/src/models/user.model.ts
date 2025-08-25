import mongoose, { Schema } from 'mongoose';

import type { IUser } from '../types/user.types';

const userSchema = new Schema<IUser>({
  googleId: {
    type: String,
    required: true,
    unique: true,
    index: true
  },
  email: {
    type: String,
    required: true,
    unique: true,
    lowercase: true,
    trim: true
  },
  name: {
    type: String,
    required: true,
    trim: true
  },
  profilePictureUrl: {
    type: String,
    required: false,
    trim: true
  },
  hobbies: {
    type: [String],
    default: [],
    validate: {
      validator: function(hobbies: string[]) {
        return hobbies.every(hobby => typeof hobby === 'string' && hobby.trim().length > 0);
      },
      message: 'Hobbies must be non-empty strings'
    }
  }
}, {
  timestamps: true
});

export const User = mongoose.model<IUser>('User', userSchema); 