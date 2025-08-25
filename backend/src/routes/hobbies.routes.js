"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const hobby_controller_1 = require("../controllers/hobby.controller");
const router = (0, express_1.Router)();
router.get('/', hobby_controller_1.getAllHobbies);
exports.default = router;
