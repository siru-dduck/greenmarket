import { Op } from "sequelize";
import { ChatRoom, ChatMessage } from "../models";
import { getUsersBy } from "../service/userService";
import { getProductBy, getProductsBy } from "../service/productService";

const ChatService = {
	retrieveChatMessage: async (roomId) => {
		const messages = await ChatMessage.findAll({
			where: {
				room_id: roomId,
			},
			order: [["create_date"], ["id"]],
		});
		return messages;
	},
	retrieveChatRoom: async (articleId, userId) => {
		const whereCondition = {};
		if (articleId) {
			whereCondition.article_id = articleId;
		}
		if (userId) {
			whereCondition[Op.or] = [
				{
					user_id_buyer: userId,
				},
				{
					user_id_seller: userId,
				},
			];
		}
		const chatRoom = await ChatRoom.findAll({
			include: [
				{
					model: ChatMessage,
					order: [["id", "DESC"]],
					limit: 1,
				},
			],
			where: whereCondition,
			order: [["article_id"]],
		});
		const articleIdList = chatRoom.map((e) => e.dataValues.article_id);
		const buyerUserIdList = [
			...new Set(chatRoom.map((e) => e.dataValues.user_id_buyer)),
		];
		const sellerUserIdList = [
			...new Set(chatRoom.map((e) => e.dataValues.user_id_seller)),
		];
		const [
			productArticleList,
			buyerUserList,
			sellerUserList,
		] = await Promise.all([
			getProductsBy(articleIdList),
			getUsersBy(buyerUserIdList),
			getUsersBy(sellerUserIdList),
		]);
		const productArticles = {};
		const sellerUsers = {};
		const buyerUsers = {};
		productArticleList.forEach((e) => {
			productArticles[e.id] = e;
		});
		sellerUserList.forEach((e) => {
			sellerUsers[e.id] = e;
		});
		buyerUserList.forEach((e) => {
			buyerUsers[e.id] = e;
		});
		chatRoom.forEach((e) => {
			e.dataValues.article = productArticles[e.dataValues.article_id];
			e.dataValues.user_buyer = buyerUsers[e.dataValues.user_id_buyer];
			e.dataValues.user_seller = sellerUsers[e.dataValues.user_id_seller];
		});
		return chatRoom;
	},
	createChatRoom: async (buyerId, articleId) => {
		const {
			user: { id: sellerId },
		} = await getProductBy(articleId);
		if (buyerId !== sellerId) {
			const chatRoom = await ChatRoom.create({
				article_id: articleId,
				user_id_buyer: buyerId,
				user_id_seller: sellerId,
			});

			return chatRoom.dataValues.id;
		} else {
			const error = new Error(
				"게시글작성자는 자신의 게시글에 대한 채팅이 불가능합니다."
			);
			error.name = "ForbiddenCreateChatRoom";
			throw error;
		}
	},
};

export default ChatService;
