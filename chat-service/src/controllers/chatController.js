import "@babel/polyfill";
import ChatService from "../service/chatService";
import { getProductBy, getProductsBy } from "../service/productService";
import { emit } from "../service/socketService";

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
	const { userId: buyerId } = authUser;
	
	/**
	 * 유효성 검증 TODO validation으로 분리
	 */
	if (!productId || !Number(productId)) {
		return res
			.status(400)
			.json({ isSuccess: false, message: "잘못된 요청입니다." });
	}

	/**
	 * TODO 예외처리 별도의 미들웨어에서 처리
	 */
	try {
		/**
		 * 상품 데이터 조회 및 데이터 검증
		 */
		const {
			userId: sellerId,
		} = await getProductBy(productId);
		if (buyerId === sellerId) {
			const error = new Error(
				"게시글작성자는 자신의 게시글에 대한 채팅이 불가능합니다."
			);
			error.name = "ForbiddenCreateChatRoom";
			throw error;
		}

		// 채팅방 생성
		const chatRoomId = await ChatService.createChatRoom(buyerId, sellerId, productId);

		// 판매자에게 채팅방 생성 이벤트 발송
		emit(`user_${sellerId}`, "createChatRoom", { chatRoomId });

		return res.status(201).json({
			isSuccess: true,
			code: 201,
			chatRoomId,
		});
	} catch (error) {
		console.dir(error);
		// TODO mongoDB unique constraint error로 변경
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
			code: 500,
			message: "채팅방 생성에 실패하였습니다.",
		});
	}
};
