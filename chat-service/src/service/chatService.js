
import ChatRoom from "../models/ChatRoom";
import User from "../models/User";
import objectMapper from "object-mapper";
import { startSession, Types } from "mongoose";

/**
 * 채팅메세지 조회
 * @param {String} roomId 
 */
export const getChatMessagesByRoomId = async (roomId) => {
	const chatRoom = await ChatRoom.findById(roomId);
	if (chatRoom === null) {
		const error = new Error("Not found chat room");
		error.name = "NotFoundChatRoom";
		throw error;
	}
	return chatRoom;
};

/**
 * 사용자 채팅방 조회
 * @param {Number} userId
 * @returns 
 */
export const getChatRoomsByUserId = async (userId) => {
	const user = await User.findOne()
		.select("-_id userId chatRooms")
		.where("userId").equals(userId)
		.populate("chatRooms", "productId sellerUserId buyerUserId createDate");
	if (user === null) {
		return { userId, chatRooms: [] };
	}

	return objectMapper(user.toJSON(), userChatRoomMap);
}

const userChatRoomMap = {
	"chatRooms[]": {
		key: "chatRooms[]",
		transform: function (room) {
			room.id = room._id;
			delete room._id;
			return room;
		}
	}
};

// => front 서버로 이전
// export const retrieveChatRoom = async (articleId, userId) => {
// const whereCondition = {};
// if (articleId) {
// 	whereCondition.article_id = articleId;
// }
// if (userId) {
// 	whereCondition[Op.or] = [
// 		{
// 			user_id_buyer: userId,
// 		},
// 		{
// 			user_id_seller: userId,
// 		},
// 	];
// }
// const chatRoom = await ChatRoom.findAll({
// 	include: [
// 		{
// 			model: ChatMessage,
// 			order: [["id", "DESC"]],
// 			limit: 1,
// 		},
// 	],
// 	where: whereCondition,
// 	order: [["article_id"]],
// });
// const articleIdList = chatRoom.map((e) => e.dataValues.article_id);
// const buyerUserIdList = [
// 	...new Set(chatRoom.map((e) => e.dataValues.user_id_buyer)),
// ];
// const sellerUserIdList = [
// 	...new Set(chatRoom.map((e) => e.dataValues.user_id_seller)),
// ];
// const [
// 	productArticleList,
// 	buyerUserList,
// 	sellerUserList,
// ] = await Promise.all([
// 	getProductsBy(articleIdList),
// 	getUsersBy(buyerUserIdList),
// 	getUsersBy(sellerUserIdList),
// ]);
// const productArticles = {};
// const sellerUsers = {};
// const buyerUsers = {};
// productArticleList.forEach((e) => {
// 	productArticles[e.id] = e;
// });
// sellerUserList.forEach((e) => {
// 	sellerUsers[e.id] = e;
// });
// buyerUserList.forEach((e) => {
// 	buyerUsers[e.id] = e;
// });
// chatRoom.forEach((e) => {
// 	e.dataValues.article = productArticles[e.dataValues.article_id];
// 	e.dataValues.user_buyer = buyerUsers[e.dataValues.user_id_buyer];
// 	e.dataValues.user_seller = sellerUsers[e.dataValues.user_id_seller];
// });
// return chatRoom;
// }

/**
 * 채팅방 생성
 * @param {*} buyerUserId 
 * @param {*} sellerUserId 
 * @param {*} productId 
 * @returns 
 */
export const createChatRoom = async (buyerUserId, sellerUserId, productId) => {
	const session = await startSession();
	session.startTransaction();
	try {
		const [chatRoom] = await ChatRoom.create([{
			productId,
			sellerUserId,
			buyerUserId
		}], { session });

		await User.findOneAndUpdate(
			{ userId: sellerUserId },
			{ $push: { chatRooms: chatRoom.id, $position: 0 } },
			{ upsert: true })
			.session(session);
		await User.findOneAndUpdate(
			{ userId: buyerUserId },
			{ $push: { chatRooms: chatRoom.id, $position: 0 } },
			{ upsert: true })
			.session(session);

		await session.commitTransaction();
		return chatRoom.id;
	} catch (error) {
		await session.abortTransaction();
		throw error;
	} finally {
		session.endSession();
	}
}

/**
 * 채팅메세지 생성
 * @param {String} roomId 
 * @param {Number} userId 
 * @param {String} message 
 * @returns 
 */
export const createChatMessage = async (roomId, userId, message) => {
	const session = await startSession();
	session.startTransaction();
	try {
		// 채팅방 조회
		const chatRoom = await ChatRoom.findById(roomId).session(session);
		if (chatRoom === null) {
			const error = new Error(
				"Not found chat room"
			);
			error.name = "NotFoundChatRoom";
			throw error;
		}

		// 채팅 메세지 삽입
		chatRoom.messages.push({ message, userId, type: "SIMPLE_CHAT" });
		await chatRoom.save();

		// 사용자 채팅방 리스트 갱신
		const [buyer, seller] = await Promise.all([
			User.findOne({ userId: chatRoom.buyerUserId }).session(session),
			User.findOne({ userId: chatRoom.sellerUserId }).session(session)
		]);

		// 채팅메세지에 해당하는 채팅방을 상단에 노출
		buyer.chatRooms = buyer.chatRooms
			.filter(r => r.toString() !== roomId);
		seller.chatRooms = seller.chatRooms
			.filter(r => r.toString() !== roomId);
		buyer.chatRooms.unshift(Types.ObjectId(roomId));
		seller.chatRooms.unshift(Types.ObjectId(roomId));
		await Promise.all([
			buyer.save(),
			seller.save()
		]);
		await session.commitTransaction();
		return chatRoom.messages[chatRoom.messages.length - 1]._id.toString();
	} catch (error) {
		await session.abortTransaction();
		throw error;
	} finally {
		session.endSession();
	}
}