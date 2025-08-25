import dotenv from 'dotenv';
import express from 'express';
import path from 'path';

import { connectDB } from './config/database';
import { notFoundHandler } from './middleware/errorHandler.middleware';
import router from './routes';
import logger from './utils/logger';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

logger.info(process.env.GOOGLE_CLIENT_ID || 'No GOOGLE_CLIENT_ID');

app.use(express.json());

app.use('/api', router);
app.use('/uploads', express.static(path.join(__dirname, '../uploads')));
app.use('*', notFoundHandler);

connectDB();
// TODO: add better logs
app.listen(PORT, () => {
  console.log(`🚀 Server running on port ${PORT}`);
});
