import { Request, Response } from 'express';
import { userRepository } from '../repositories/user.repository';
import { MediaService } from '../services/media.service';
import {
  UpdateUserHobbiesSchema,
  UpdateUserProfilePictureSchema,
} from '../types/user.types';

export const getProfile = async (req: Request, res: Response) => {
  const { user } = req;

  res.status(200).json({
    message: 'Profile fetched successfully',
    data: { user },
  });
};

export const updateUserHobbies = async (
  req: Request<{}, {}, UpdateUserHobbiesSchema>,
  res: Response
) => {
  try {
    const user = req.user!;
    const { hobbies } = req.body;

    const updatedUser = await userRepository.update(user._id, { hobbies });

    if (!updatedUser) {
      return res.status(404).json({
        message: 'User not found',
      });
    }

    res.status(200).json({
      message: 'User hobbies updated successfully',
      data: { user: updatedUser },
    });
  } catch (error) {
    res.status(500).json({
      message: 'Failed to update user hobbies',
      error: error instanceof Error ? error.message : 'Unknown error',
    });
  }
};

export const updateUserProfilePicture = async (
  req: Request<{}, {}, UpdateUserProfilePictureSchema>,
  res: Response
) => {
  try {
    const user = req.user!;
    const { profilePicture } = req.body;

    if (user.profilePicture) {
      await MediaService.deleteImage(user.profilePicture);
    }

    const updatedUser = await userRepository.update(user._id, {
      profilePicture,
    });

    if (!updatedUser) {
      return res.status(404).json({
        message: 'User not found',
      });
    }

    res.status(200).json({
      message: 'User profile picture updated successfully',
      data: { user: updatedUser },
    });
  } catch (error) {
    res.status(500).json({
      message: 'Failed to update user profile picture',
      error: error instanceof Error ? error.message : 'Unknown error',
    });
  }
};

export const deleteUserProfilePicture = async (req: Request, res: Response) => {
  try {
    const user = req.user!;

    if (user.profilePicture) {
      await MediaService.deleteImage(user.profilePicture);
    }

    const updatedUser = await userRepository.update(user._id, {
      profilePicture: undefined,
    });

    if (!updatedUser) {
      return res.status(404).json({
        message: 'User not found',
      });
    }

    res.status(200).json({
      message: 'User profile picture deleted successfully',
      data: { user: updatedUser },
    });
  } catch (error) {
    res.status(500).json({
      message: 'Failed to delete user profile picture',
      error: error instanceof Error ? error.message : 'Unknown error',
    });
  }
};
