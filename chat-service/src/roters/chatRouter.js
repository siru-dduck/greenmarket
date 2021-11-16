import express from "express";
import {
	postChatRoom,
	getChatMessages,
	getChatRooms,
} from "../controllers/chatController";
import { authJwt } from "../middlewares/authMiddleware";
import { query, validationResult } from "express-validator";
import { numberArrayParser } from "./util/arrayParser";

const router = express.Router();

// validation error handling
const validationCheck = (req, res, next) => {
	const errors = validationResult(req);
	if (!errors.isEmpty()) {
		return res.status(400).json({ errors: errors.array() });
	}
	next();
};

router.get("/rooms",
	query("userId")
		.toInt()
		.isNumeric()
		.notEmpty(),
	validationCheck,
	getChatRooms);
router.post("/rooms", authJwt, postChatRoom);
router.get("/rooms/:roomId/messages", getChatMessages);

// global error handling
router.use((err, _, res, __) => {
	if (err.name === "NotAuthorized") {
		return res.status(401)
			.json({ code: 401, message: "not authorized" });
	}
	console.error(err.stack);
	return res.status(500)
		.json({ status: 500, message: 'internal error', type: 'internal' });
});

export default router;
