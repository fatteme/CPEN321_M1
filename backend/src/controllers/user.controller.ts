import { Request, Response } from 'express';
import { userRepository } from '../repositories/user.repository';
import { UpdateUserHobbiesSchema } from '../types/user.types';

export const getProfile = async (req: Request, res: Response) => {
  const { user } = req;

  res.status(200).json({ 
    message: 'Profile fetched successfully',
    data: { user },
  });
};

export const updateUserHobbies = async (req: Request<{}, {}, UpdateUserHobbiesSchema>, res: Response) => {
  try {
    const { hobbies } = req.body;
    const user = req.user!;

    const updatedUser = await userRepository.update(user._id, { hobbies });

    if (!updatedUser) {
      return res.status(404).json({
        message: 'User not found'
      });
    }

    res.status(200).json({
      message: 'User hobbies updated successfully',
      data: { user: updatedUser }
  });
  } catch (error) {
    res.status(500).json({
      message: 'Failed to update user hobbies',
      error: error instanceof Error ? error.message : 'Unknown error'
    });
  }
};