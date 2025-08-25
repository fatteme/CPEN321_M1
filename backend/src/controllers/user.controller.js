"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.updateUserHobbies = exports.getProfile = void 0;
const user_repository_1 = require("../repositories/user.repository");
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
        const { hobbies } = req.body;
        const user = req.user;
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
