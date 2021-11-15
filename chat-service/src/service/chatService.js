
import ChatRoom from "../models/ChatRoom";
import User from "../models/User";
import { getUsersBy } from "../service/userService";
import { startSession, Types } from "mongoose";

const ChatService = {
	retrieveChatMessage: async (roomId) => {
		// const messages = await ChatMessage.findAll({
		// 	where: {
		// 		room_id: roomId,
		// 	},
		// 	order: [["create_date"], ["id"]],
		// });
		// return messages;
	},
	// => front 서버로 이전
	retrieveChatRoom: async (articleId, userId) => {
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
	},
	createChatRoom: async (buyerUserId, sellerUserId, productId) => {
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
		} catch(error) {
			await session.abortTransaction();
			throw error;
		} finally {
			session.endSession();
		}
	},
	createChatMessage: async (roomId, userId, message) => {
		const session = await startSession();
		session.startTransaction();
		try {
			// 채팅방 조회
			const chatRoom = await ChatRoom.findById(roomId).session(session);
			if(chatRoom === null) {
				const error = new Error(
					"Not found chat room"
				);
				error.name = "NotFoundChatRoom";
				throw error;
			}

			// 채팅 메세지 삽입
			chatRoom.messages.push({message, userId, type: "SIMPLE_CHAT" });
			await chatRoom.save();
	
			// 사용자 채팅방 리스트 갱신
			const [buyer, seller] = await Promise.all([
				User.findOne({ userId: chatRoom.buyerUserId}).session(session),
				User.findOne({ userId: chatRoom.sellerUserId}).session(session)
			]);

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
			return chatRoom.messages[chatRoom.messages.length-1]._id.toString();
		} catch(error) {
			await session.abortTransaction();
			throw error;
		} finally {
			session.endSession();
		}
	}
};

export default ChatService;
