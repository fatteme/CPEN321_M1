import { User } from '../models/user.model';

export const migrateUsersToIncludeHobbies = async (): Promise<void> => {
  try {
    // Find all users that don't have the hobbies field
    const usersWithoutHobbies = await User.find({
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
    const result = await User.updateMany(
      {
        $or: [
          { hobbies: { $exists: false } },
          { hobbies: null }
        ]
      },
      {
        $set: { hobbies: [] }
      }
    );

    console.log(`✅ Migrated ${result.modifiedCount} users to include hobbies field`);
  } catch (error) {
    console.error('❌ Error during migration:', error);
    throw error;
  }
};
