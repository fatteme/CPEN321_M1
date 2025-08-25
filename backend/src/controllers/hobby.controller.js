"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getAllHobbies = void 0;
const hobbies_1 = require("../constants/hobbies");
const getAllHobbies = async (req, res) => {
    try {
        res.status(200).json({
            message: 'All hobbies fetched successfully',
            data: { hobbies: hobbies_1.HOBBIES }
        });
    }
    catch (error) {
        res.status(500).json({
            message: 'Failed to fetch available hobbies',
            error: error instanceof Error ? error.message : 'Unknown error'
        });
    }
};
exports.getAllHobbies = getAllHobbies;
