"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.deleteUserProfilePicture = exports.updateUserProfilePicture = exports.updateUserHobbies = exports.getProfile = void 0;
const user_repository_1 = require("../repositories/user.repository");
const media_service_1 = require("../services/media.service");
const getProfile = async (req, res) => {
    const { user } = req;
    res.status(200).json({
        message: 'Profile fetched successfully',
        data: { user },
    });
};
exports.getProfile = getProfile;
const updateUserHobbies = async (req, res) => {
    try {
        const user = req.user;
        const { hobbies } = req.body;
        const updatedUser = await user_repository_1.userRepository.update(user._id, { hobbies });
        if (!updatedUser) {
            return res.status(404).json({
                message: 'User not found'
            });
        }
        res.status(200).json({
            message: 'User hobbies updated successfully',
            data: { user: updatedUser }
        });
    }
    catch (error) {
        res.status(500).json({
            message: 'Failed to update user hobbies',
            error: error instanceof Error ? error.message : 'Unknown error'
        });
    }
};
exports.updateUserHobbies = updateUserHobbies;
const updateUserProfilePicture = async (req, res) => {
    try {
        const user = req.user;
        const { profilePictureUrl } = req.body;
        // Delete old profile picture if it exists
        if (user.profilePictureUrl) {
            await media_service_1.MediaService.deleteImage(user.profilePictureUrl);
        }
        const updatedUser = await user_repository_1.userRepository.update(user._id, { profilePictureUrl });
        if (!updatedUser) {
            return res.status(404).json({
                message: 'User not found'
            });
        }
        res.status(200).json({
            message: 'User profile picture updated successfully',
            data: { user: updatedUser }
        });
    }
    catch (error) {
        res.status(500).json({
            message: 'Failed to update user profile picture',
            error: error instanceof Error ? error.message : 'Unknown error'
        });
    }
};
exports.updateUserProfilePicture = updateUserProfilePicture;
const deleteUserProfilePicture = async (req, res) => {
    try {
        const user = req.user;
        // Delete old profile picture if it exists
        if (user.profilePictureUrl) {
            await media_service_1.MediaService.deleteImage(user.profilePictureUrl);
        }
        const updatedUser = await user_repository_1.userRepository.update(user._id, { profilePictureUrl: undefined });
        if (!updatedUser) {
            return res.status(404).json({
                message: 'User not found'
            });
        }
        res.status(200).json({
            message: 'User profile picture deleted successfully',
            data: { user: updatedUser }
        });
    }
    catch (error) {
        res.status(500).json({
            message: 'Failed to delete user profile picture',
            error: error instanceof Error ? error.message : 'Unknown error'
        });
    }
};
exports.deleteUserProfilePicture = deleteUserProfilePicture;
