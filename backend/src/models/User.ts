import mongoose, { Document, Schema } from 'mongoose';

export interface IUser extends Document {
  googleId: string;
  email: string;
  name: string;
  profilePicture?: string;
  createdAt: Date;
  updatedAt: Date;
}

const UserSchema: Schema = new Schema({
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
    default: null,
  },
}, {
  timestamps: true,
});

// Index for efficient queries
UserSchema.index({ googleId: 1 });
UserSchema.index({ email: 1 });

export const User = mongoose.model<IUser>('User', UserSchema);
