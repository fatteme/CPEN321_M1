import { OAuth2Client } from 'google-auth-library';

interface GoogleUserInfo {
  sub: string; // Google ID
  email: string;
  name: string;
  picture?: string;
}

export class GoogleAuthService {
  private client: OAuth2Client;

  constructor() {
    const clientId = process.env.GOOGLE_CLIENT_ID;
    if (!clientId) {
      throw new Error('GOOGLE_CLIENT_ID is not defined in environment variables');
    }
    this.client = new OAuth2Client(clientId);
  }

  async verifyIdToken(idToken: string): Promise<GoogleUserInfo> {
    try {
      const ticket = await this.client.verifyIdToken({
        idToken,
        audience: process.env.GOOGLE_CLIENT_ID,
      });

      const payload = ticket.getPayload();
      if (!payload) {
        throw new Error('Invalid token payload');
      }

      return {
        sub: payload.sub,
        email: payload.email || '',
        name: payload.name || '',
        picture: payload.picture,
      };
    } catch (error) {
      throw new Error('Failed to verify Google ID token');
    }
  }
}

export const googleAuthService = new GoogleAuthService();
