import { HOBBIES } from '../constants';

export type GetAllHobbiesResponse = {
  message: string;
  data?: {
    hobbies: typeof HOBBIES;
  };
};
