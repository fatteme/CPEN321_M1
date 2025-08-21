import { Request, Response } from 'express';
import { googleAuthService } from '../services/googleAuth';
import { User } from '../models/User';
import { generateToken } from '../utils/jwt';
import { AuthenticatedRequest } from '../middleware/auth';

export const googleLogin = async (req: Request, res: Response): Promise<void> => {
  try {
    const { idToken } = req.body;

    if (!idToken) {
      res.status(400).json({
        success: false,
        message: 'Google ID token is required',
      });
      return;
    }

    // Verify the Google ID token
    const googleUser = await googleAuthService.verifyIdToken(idToken);

    // Check if user exists, if not create new user
    let user = await User.findOne({ googleId: googleUser.sub });

    if (!user) {
      // Create new user (automatic registration)
      user = new User({
        googleId: googleUser.sub,
        email: googleUser.email,
        name: googleUser.name,
        profilePicture: googleUser.picture,
      });
      await user.save();
    } else {
      // Update existing user's profile picture and name in case they changed
      user.name = googleUser.name;
      user.profilePicture = googleUser.picture;
      await user.save();
    }

    // Generate JWT token
    const token = generateToken(user);

    res.status(200).json({
      success: true,
      message: 'Authentication successful',
      data: {
        token,
        user: {
          id: user._id,
          email: user.email,
          name: user.name,
          profilePicture: user.profilePicture,
        },
      },
    });
  } catch (error) {
    console.error('Google authentication error:', error);
    
    // Check if it's a Google service unavailable error
    if (error instanceof Error && error.message.includes('Failed to verify')) {
      res.status(503).json({
        success: false,
        message: 'Authentication service temporarily unavailable. Please try again later.',
      });
      return;
    }

    res.status(500).json({
      success: false,
      message: 'Internal server error during authentication',
    });
  }
};

export const logout = async (req: AuthenticatedRequest, res: Response): Promise<void> => {
  try {
    // Since we're using stateless JWT tokens, logout is handled client-side
    // by removing the token from storage. We just return a success response.
    res.status(200).json({
      success: true,
      message: 'Logged out successfully',
    });
  } catch (error) {
    console.error('Logout error:', error);
    res.status(500).json({
      success: false,
      message: 'Internal server error during logout',
    });
  }
};
