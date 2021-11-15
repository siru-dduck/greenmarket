import jwt from "jsonwebtoken";
import User from "../models/User";

const SERVICE_PRODUCT_HOST =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_HOST;
const SERVICE_PRODUCT_PORT =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_PORT;

export const authJwt = (socket, next) => {
	try {
		const authHeader = socket.handshake.headers.authorization;
		const token = authHeader.substring("Bearer ".length);
		const authUser = jwt.verify(token, process.env.SECRET_TOKEN);
		socket.authUser = authUser;
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
	console.log(`init socket ${socket.authUser.userId}`);
	const { authUser } = socket;
	try {
		const user = await User.findOne({ userId: authUser.userId});
		socket.join(`user_${socket.authUser.userId}`);
		if(user !== null) {
			user.chatRooms.forEach((room) => {
				socket.join(`room_${room._id.toString()}`);
			});
		}

		next();
	} catch (error) {
		console.log(error);
		const err = new Error("Socket Init Fail");
		err.data = { type: "SocketInitFail" };
		next(err);
	}
};
