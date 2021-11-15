import express from "express";
import {
	postChatRoom,
	getChatMessage,
	getChatRoom,
} from "../controllers/chatController";
import { authJwt } from "../middlewares/authMiddleware";

const router = express.Router();

router.get("/rooms", getChatRoom);
router.post("/rooms", authJwt, postChatRoom);
router.get("/rooms/:roomId/messages", getChatMessage);

router.use(function(err, req, res, next) {
	if(err.name === "NotAuthorized") {
		return res.status(401)
			.json({ code: 401, message: "not authorized"});
	}
	console.error(err.stack);
	return res.status(500)
		.json({status:500, message: 'internal error', type:'internal'});
});

export default router;
