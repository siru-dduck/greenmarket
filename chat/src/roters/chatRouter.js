import express from "express";
import {
	createChatRoom,
	getChatMessage,
	getChatRoom,
} from "../controllers/chatController";

const router = express.Router();

router.get("/room", getChatRoom);
router.post("/room", createChatRoom);
router.get("/room/:roomId/messages", getChatMessage);

export default router;
