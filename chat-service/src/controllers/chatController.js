import "@babel/polyfill";
import jwt from "jsonwebtoken";
import ChatService from "../service/chatService";

export const getChatMessage = async (req, res) => {
	const { roomId } = req.params;
	try {
		const messages = await ChatService.retrieveChatMessage(roomId);
		res.json({ messages });
	} catch (error) {
		console.log(error);
		return res.status(500).json({
			isSuccess: false,
			message: "서버처리 오류",
		});
	}
};

export const getChatRoom = async (req, res) => {
	const { article_id, user_id } = req.query;
	if (!user_id && !article_id) {
		return res.status(400).json({
			isSuccess: false,
			message: "옳바른 요청값이 아닙니다.",
		});
	}
	try {
		const chatRoom = await ChatService.retrieveChatRoom(article_id, user_id);
		res.json({ isSuccess: true, chatRoom });
	} catch (error) {
		console.log(error);
		return res.status(500).json({
			isSuccess: false,
			message: "서버처리 오류",
		});
	}
};

export const createChatRoom = async (req, res) => {
	const { authUser } = req;
	const { productId } = req.body;
	
	if (!productId || !Number(productId)) {
		return res
			.status(400)
			.json({ isSuccess: false, message: "잘못된 요청입니다." });
	}

	try {
		const { userId: buyerId } = authUser;
		const chatRoomId = await ChatService.createChatRoom(buyerId, productId);
		return res.status(201).json({
			isSuccess: true,
			code: 201,
			chatRoomId,
		});
	} catch (error) {
		console.dir(error);
		if (error.name === "SequelizeUniqueConstraintError") {
			return res.status(409).json({
				isSuccess: false,
				code: 409,
				message: "이미 생성된 채팅방이 있습니다.",
			});
		} else if (error.name === "ForbiddenCreateChatRoom") {
			return res.status(403).json({
				isSuccess: false,
				code: 403,
				message: error.message,
			});
		}
		return res.json({
			isSuccess: false,
			code: 200,
			message: "채팅방 생성에 실패하였습니다.",
		});
	}
};
