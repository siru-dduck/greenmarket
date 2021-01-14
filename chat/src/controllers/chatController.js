import "@babel/polyfill";
import axios from "axios";
import jwt from "jsonwebtoken";
import { Op } from "sequelize";
import { ChatRoom, ChatMessage } from "../models";

const SERVICE_USER_HOST =
	process.env.GREENMARKET_USER_APP_SERVICE_HOST || "localhost";
const SERVICE_USER_PORT =
	process.env.GREENMARKET_USER_APP_SERVICE_PORT || "5000";
const SERVICE_PRODUCT_HOST =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_HOST || "localhost";
const SERVICE_PRODUCT_PORT =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_PORT || "8080";

export const getChatMessage = async (req, res) => {
	const { roomId } = req.params;
	try {
		const messages = await ChatMessage.findAll({
			where: {
				room_id: roomId,
			},
			order: [["create_date"], ["id"]],
		});
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
		const sellingProductResponse = await axios.get(
			`http://${SERVICE_PRODUCT_HOST}:${SERVICE_PRODUCT_PORT}/api/products?userId=${user_id}`
		);
		const articles = {};
		Array.from(sellingProductResponse.data.productArticles).forEach((e) => {
			articles[e.id] = e;
		});
		const whereCondition = {
			[Op.or]: [
				{
					user_id_buyer: user_id,
				},
				{
					article_id: { [Op.in]: Object.keys(articles) },
				},
			],
		};
		if (article_id) {
			whereCondition.article_id = article_id;
		}
		const chatRoom = await ChatRoom.findAll({
			include: [
				{
					model: ChatMessage,
					order: [
						["create_date", "DESC"],
						["id", "DESC"],
					],
					limit: 1,
				},
			],
			where: whereCondition,
			order: [["article_id"]],
		});
		const articleIds = chatRoom.map((e) => e.dataValues.article_id);
		const userIds = new Set(chatRoom.map((e) => e.dataValues.user_id_buyer));

		const users = {};
		if (userIds.size > 0) {
			const userResponse = await axios.get(
				`http://${SERVICE_USER_HOST}:${SERVICE_USER_PORT}/api/user?user_ids=${[
					...userIds,
				]}`
			);
			Array.from(userResponse.data.users).forEach((e) => {
				users[e.id] = e;
			});
		}

		const productResponse = await axios.get(
			`http://${SERVICE_PRODUCT_HOST}:${SERVICE_PRODUCT_PORT}/api/products?articleIds=${articleIds}`
		);
		Array.from(productResponse.data.productArticles).forEach((e) => {
			articles[e.id] = e;
		});

		chatRoom.forEach((e) => {
			e.dataValues.user_buyer = users[e.dataValues.user_id_buyer];
			e.dataValues.article = articles[e.dataValues.article_id];
		});
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
	const token = req.cookies.x_auth;
	const { article_id } = req.body;

	if (!article_id || !Number(article_id)) {
		return res
			.status(400)
			.json({ isSuccess: false, message: "잘못된 요청입니다." });
	}

	try {
		const decoded = jwt.verify(token, process.env.SECRET_TOKEN);
		const { id: buyerId } = decoded;
		const response = await axios.get(
			`http://${SERVICE_PRODUCT_HOST}:${SERVICE_PRODUCT_PORT}/api/products/${article_id}`
		);
		const { id: sellerId } = response.data.productArticle.user;
		if (buyerId !== sellerId) {
			const chatRoom = await ChatRoom.create({
				article_id,
				user_id_buyer: buyerId,
				user_id_seller: sellerId,
			});
			console.dir(chatRoom);
			return res.json({
				isSuccess: true,
				code: 200,
				chatRoomId: chatRoom.dataValues.id,
			});
		} else {
			return res.json({
				isSuccess: false,
				code: 200,
				message: "게시글작성자는 자신의 게시글에 대한 채팅이 불가능합니다.",
			});
		}
	} catch (error) {
		console.dir(error);
		if (error.name === "TokenExpiredError") {
			return res.status(401).json({
				isSuccess: false,
				code: 401,
				message: "토큰이 만료되었습니다.",
			});
		} else if (error.name === "JsonWebTokenError") {
			return res.status(401).json({
				isSuccess: false,
				code: 401,
				message: "전송된 토큰이 없습니다.",
			});
		} else if (error.name === "SequelizeUniqueConstraintError") {
			return res.json({
				isSuccess: false,
				code: 200,
				message: "이미 생성된 채팅방이 있습니다.",
			});
		}
		return res.json({
			isSuccess: false,
			code: 200,
			message: "채팅방 생성에 실패하였습니다.",
		});
	}
};
