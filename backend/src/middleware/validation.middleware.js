"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.validateParams = exports.validateQuery = exports.validateBody = void 0;
const validateBody = (schema) => {
    return (req, res, next) => {
        try {
            const validatedData = schema.parse(req.body);
            req.body = validatedData;
            next();
        }
        catch (error) {
            if (error?.issues) {
                return res.status(400).json({
                    error: 'Validation error',
                    message: 'Invalid input data',
                    details: error.issues.map((issue) => ({
                        field: issue.path.join('.'),
                        message: issue.message
                    }))
                });
            }
            return res.status(500).json({
                error: 'Internal server error',
                message: 'Validation processing failed'
            });
        }
    };
};
exports.validateBody = validateBody;
const validateQuery = (schema) => {
    return (req, res, next) => {
        try {
            const validatedData = schema.parse(req.query);
            // Type assertion for validated data
            req.query = validatedData;
            next();
        }
        catch (error) {
            if (error?.issues) {
                return res.status(400).json({
                    error: 'Query validation error',
                    message: 'Invalid query parameters',
                    details: error.issues.map((issue) => ({
                        field: issue.path.join('.'),
                        message: issue.message
                    }))
                });
            }
            return res.status(500).json({
                error: 'Internal server error',
                message: 'Query validation processing failed'
            });
        }
    };
};
exports.validateQuery = validateQuery;
const validateParams = (schema) => {
    return (req, res, next) => {
        try {
            const validatedData = schema.parse(req.params);
            // Type assertion for validated data
            req.params = validatedData;
            next();
        }
        catch (error) {
            if (error?.issues) {
                return res.status(400).json({
                    error: 'Parameter validation error',
                    message: 'Invalid URL parameters',
                    details: error.issues.map((issue) => ({
                        field: issue.path.join('.'),
                        message: issue.message
                    }))
                });
            }
            return res.status(500).json({
                error: 'Internal server error',
                message: 'Parameter validation processing failed'
            });
        }
    };
};
exports.validateParams = validateParams;
