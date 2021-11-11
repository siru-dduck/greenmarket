import express from "express";
import {
	createChatRoom,
	getChatMessage,
	getChatRoom,
} from "../controllers/chatController";
import { authJwt } from "../middlewares/authMiddleware";

const router = express.Router();

router.get("/rooms", getChatRoom);
router.post("/rooms", authJwt, createChatRoom);
router.get("/rooms/:roomId/messages", getChatMessage);

router.use(function(err, req, res, next) {
	console.error(err.stack);
	res.status(500).send({status:500, message: 'internal error', type:'internal'});
});

export default router;
