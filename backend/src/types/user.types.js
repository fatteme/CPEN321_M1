"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.updateUserHobbiesSchema = exports.updateUserProfilePictureSchema = exports.userSchema = void 0;
const zod_1 = __importDefault(require("zod"));
const hobbies_1 = require("../constants/hobbies");
exports.userSchema = zod_1.default.object({
    email: zod_1.default.string().email(),
    name: zod_1.default.string().min(1),
    googleId: zod_1.default.string().min(1),
    profilePictureUrl: zod_1.default.string().optional(),
    hobbies: zod_1.default.array(zod_1.default.string()).default([]),
});
// Update user profile picture
exports.updateUserProfilePictureSchema = zod_1.default.object({
    profilePictureUrl: zod_1.default.string().min(1)
});
// Update user hobbies
exports.updateUserHobbiesSchema = zod_1.default.object({
    hobbies: zod_1.default.array(zod_1.default.string()).min(1).refine((val) => val.every(v => hobbies_1.HOBBIES.includes(v)), { message: "Hobby must be in the available hobbies list" })
});
