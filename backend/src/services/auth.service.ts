import { OAuth2Client } from 'google-auth-library';
import jwt from 'jsonwebtoken';

import { userRepository } from '../repositories/user.repository';
import type { AuthResult } from '../types/auth.types';
import type { GoogleUserInfo, IUser } from '../types/user.types';
import logger from '../utils/logger';

export class AuthService {
  private googleClient: OAuth2Client;

  constructor() {
    this.googleClient = new OAuth2Client(process.env.GOOGLE_CLIENT_ID);
  }

  private async verifyGoogleToken(idToken: string): Promise<GoogleUserInfo> {
    try {
      const ticket = await this.googleClient.verifyIdToken({
        idToken,
        audience: process.env.GOOGLE_CLIENT_ID,
      });

      const payload = ticket.getPayload();
      if (!payload) {
        throw new Error('Invalid token payload');
      }

      if (!payload.email || !payload.name) {
        throw new Error('Missing required user information from Google');
      }

      return {
        googleId: payload.sub,
        email: payload.email,
        name: payload.name,
        profilePicture: payload.picture,
      };
    } catch (error) {
      logger.error('Google token verification failed:', error);
      throw new Error('Invalid Google token');
    }
  }

  private generateAccessToken(user: IUser): string {
    return jwt.sign(JSON.stringify(user), process.env.JWT_SECRET!);
  }

  async authenticateWithGoogle(idToken: string): Promise<AuthResult> {
    try {
      const googleUserInfo = await this.verifyGoogleToken(idToken);

      const user = await this.findOrCreateUser(googleUserInfo);

      const token = this.generateAccessToken(user);

      return { token, user };
    } catch (error) {
      logger.error('Authentication failed:', error);
      throw error;
    }
  }

  private async findOrCreateUser(
    googleUserInfo: GoogleUserInfo
  ): Promise<IUser> {
    try {
      const user = await userRepository.findByGoogleId(googleUserInfo.googleId);

      if (user) {
        return user;
      }

      return await userRepository.create(googleUserInfo);
    } catch (error) {
      logger.error('Error in findOrCreateUser:', error);
      throw new Error('Failed to process user');
    }
  }
}

export const authService = new AuthService();
