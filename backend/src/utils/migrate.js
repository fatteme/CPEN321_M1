"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.migrateUsersToIncludeHobbies = void 0;
const user_model_1 = require("../models/user.model");
const migrateUsersToIncludeHobbies = async () => {
    try {
        // Find all users that don't have the hobbies field
        const usersWithoutHobbies = await user_model_1.User.find({
            $or: [
                { hobbies: { $exists: false } },
                { hobbies: null }
            ]
        });
        if (usersWithoutHobbies.length === 0) {
            console.log('✅ All users already have hobbies field');
            return;
        }
        // Update users to include hobbies field with empty array
        const result = await user_model_1.User.updateMany({
            $or: [
                { hobbies: { $exists: false } },
                { hobbies: null }
            ]
        }, {
            $set: { hobbies: [] }
        });
        console.log(`✅ Migrated ${result.modifiedCount} users to include hobbies field`);
    }
    catch (error) {
        console.error('❌ Error during migration:', error);
        throw error;
    }
};
exports.migrateUsersToIncludeHobbies = migrateUsersToIncludeHobbies;
