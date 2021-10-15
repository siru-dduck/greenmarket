import jwt from "jsonwebtoken";
import axios from "axios";
import { Op } from "sequelize";
import { ChatRoom } from "../models";

const SERVICE_PRODUCT_HOST =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_HOST;
const SERVICE_PRODUCT_PORT =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_PORT;

export const authJwt = (socket, next) => {
	try {
		const token = socket.request.cookies.x_auth;
		console.dir(socket.request);
		console.log(token.cookies);
		const decoded = jwt.verify(token, process.env.SECRET_TOKEN);
		const { id } = decoded;
		socket.userId = id;
		next();
	} catch (error) {
		console.error(error);
		const err = new Error("NotAuthorized");
		err.data = { type: "NotAuthorized" };
		next(err);
	}
};

// @@@ 소켓을 초기화하는 미들웨어
// 초기화작업 : socket에 user id 할당 및 채팅방 연결
export const initializeSocket = async (socket, next) => {
	console.log("init socket", next);
	try {
		const response = await axios.get(
			`http://${SERVICE_PRODUCT_HOST}:${SERVICE_PRODUCT_PORT}/api/products?user_id=${socket.userId}`
		);
		const articleIds = response.data.productArticles.map((e) => {
			return e.id;
		});
		const chatRoom = await ChatRoom.findAll({
			where: {
				[Op.or]: [
					{ article_id: { [Op.in]: articleIds } },
					{ user_id_buyer: socket.userId },
				],
			},
			order: [["article_id"]],
		});
		socket.join(`user_${socket.userId}`);
		chatRoom.forEach((room) => {
			socket.join(`room_${room.id}`);
		});

		next();
	} catch (error) {
		console.log(error);
		const err = new Error("Can Not Found User Chat Inform");
		err.data = { type: "CanNotFoundUserChatInform" };
		next(err);
	}
};
