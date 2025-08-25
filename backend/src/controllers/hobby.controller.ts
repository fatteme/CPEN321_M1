import { Request, Response } from 'express';

import { HOBBIES } from "../constants/hobbies";

export const getAllHobbies = async (req: Request, res: Response) => {
    try {
      res.status(200).json({
        message: 'All hobbies fetched successfully',
        data: { hobbies: HOBBIES }
      });
    } catch (error) {
      res.status(500).json({
        message: 'Failed to fetch available hobbies',
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    }
  };