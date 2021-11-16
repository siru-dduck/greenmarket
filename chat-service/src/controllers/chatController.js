import "@babel/polyfill";
import { getChatRoomsByUserId, getChatMessagesByRoomId, createChatRoom } from "../service/chatService";
import { getProductBy, getProductsBy } from "../service/productService";
import { emit } from "../service/socketService";

export const getChatMessages = async (req, res) => {
	const { roomId } = req.params;
	try {
		const chatMessageInfo = await getChatMessagesByRoomId(roomId);
		res.json(chatMessageInfo);
	} catch (error) {
		console.log(error);
		return res.status(500).json({
			isSuccess: false,
			message: "서버처리 오류",
		});
	}
};

export const getChatRooms = async (req, res) => {
	const { userId } = req.query;
	try {
		const chatRooms = await getChatRoomsByUserId(userId);
		console.log(chatRooms);
		return res.send(chatRooms);
	} catch (error) {
		console.log(error);
		return res.status(500).json({
			isSuccess: false,
			message: "서버처리 오류",
		});
	}
};

export const postChatRoom = async (req, res) => {
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
		const chatRoomId = await createChatRoom(buyerId, sellerId, productId);

		// 판매자에게 채팅방 생성 이벤트 발송
		emit(`user_${sellerId}`, "createChatRoom", { chatRoomId });

		return res.status(201).json({
			isSuccess: true,
			code: 201,
			chatRoomId,
		});
	} catch (error) {
		// 이미 채팅방이 생성된 예외
		if (error.name === "MongoServerError" && error.code === 11000) {
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
		console.dir(error);
		return res.status(500).json({
			isSuccess: false,
			code: 500,
			message: "채팅방 생성에 실패하였습니다.",
		});
	}
};
