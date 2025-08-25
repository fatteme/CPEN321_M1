"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const dotenv_1 = __importDefault(require("dotenv"));
const express_1 = __importDefault(require("express"));
const path_1 = __importDefault(require("path"));
const database_1 = require("./config/database");
const errorHandler_middleware_1 = require("./middleware/errorHandler.middleware");
const routes_1 = __importDefault(require("./routes"));
const logger_1 = __importDefault(require("./utils/logger"));
dotenv_1.default.config();
const app = (0, express_1.default)();
const PORT = process.env.PORT || 3000;
logger_1.default.info(process.env.GOOGLE_CLIENT_ID || 'No GOOGLE_CLIENT_ID');
app.use(express_1.default.json());
app.use('/api', routes_1.default);
app.use('/uploads', express_1.default.static(path_1.default.join(__dirname, '../uploads')));
app.use('*', errorHandler_middleware_1.notFoundHandler);
(0, database_1.connectDB)();
// TODO: add better logs
app.listen(PORT, () => {
    console.log(`🚀 Server running on port ${PORT}`);
});
