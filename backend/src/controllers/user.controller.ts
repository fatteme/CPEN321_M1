import { Request, Response } from 'express';
import { User } from '../models/user.model';
import { AVAILABLE_HOBBIES } from '../constants/hobbies';
import { AddHobbyRequest, RemoveHobbyRequest } from '../types/user.types';

export const getProfile = async (req: Request, res: Response) => {
  const { user } = req;

  res.status(200).json({ 
    message: 'Profile fetched successfully',
    data: { user },
  });
};

export const getAvailableHobbies = async (req: Request, res: Response) => {
  try {
    res.status(200).json({
      message: 'Available hobbies fetched successfully',
      data: { hobbies: AVAILABLE_HOBBIES }
    });
  } catch (error) {
    res.status(500).json({
      message: 'Failed to fetch available hobbies',
      error: error instanceof Error ? error.message : 'Unknown error'
    });
  }
};

export const addHobby = async (req: Request, res: Response) => {
  try {
    const { hobby } = req.body as AddHobbyRequest;
    const userId = req.user?._id;

    if (!userId) {
      return res.status(401).json({
        message: 'User not authenticated'
      });
    }

    // Validate that the hobby is in the available list
    if (!AVAILABLE_HOBBIES.includes(hobby as any)) {
      return res.status(400).json({
        message: 'Invalid hobby. Please choose from the available hobbies list.',
        data: { availableHobbies: AVAILABLE_HOBBIES }
      });
    }

    // Check if user already has this hobby
    const user = await User.findById(userId);
    if (user?.hobbies.includes(hobby as any)) {
      return res.status(400).json({
        message: 'Hobby already exists in your profile'
      });
    }

    // Add hobby to user's profile
    const updatedUser = await User.findByIdAndUpdate(
      userId,
      { $addToSet: { hobbies: hobby } },
      { new: true, runValidators: true }
    );

    if (!updatedUser) {
      return res.status(404).json({
        message: 'User not found'
      });
    }

    res.status(200).json({
      message: 'Hobby added successfully',
      data: { user: updatedUser }
    });
  } catch (error) {
    res.status(500).json({
      message: 'Failed to add hobby',
      error: error instanceof Error ? error.message : 'Unknown error'
    });
  }
};

export const removeHobby = async (req: Request, res: Response) => {
  try {
    const { hobby } = req.body as RemoveHobbyRequest;
    const userId = req.user?._id;

    if (!userId) {
      return res.status(401).json({
        message: 'User not authenticated'
      });
    }

    // Remove hobby from user's profile
    const updatedUser = await User.findByIdAndUpdate(
      userId,
      { $pull: { hobbies: hobby } },
      { new: true, runValidators: true }
    );

    if (!updatedUser) {
      return res.status(404).json({
        message: 'User not found'
      });
    }

    res.status(200).json({
      message: 'Hobby removed successfully',
      data: { user: updatedUser }
    });
  } catch (error) {
    res.status(500).json({
      message: 'Failed to remove hobby',
      error: error instanceof Error ? error.message : 'Unknown error'
    });
  }
};