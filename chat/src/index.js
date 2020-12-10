import "@babel/polyfill";
import express from "express";
import socketIo from "socket.io";
import dotenv from "dotenv";
import logger from "morgan";
import helmet from "helmet";
import cookieParser from "cookie-parser";
import socketIoCookieParser from "socket.io-cookie-parser";
import bodyParser from "body-parser";
import jwt from "jsonwebtoken";
import axios from "axios";
import { Op } from "sequelize";
import chatRouter from "./roters/chatRouter";
import { ChatMessage, ChatRoom } from "./models";
import redis from "socket.io-redis";

dotenv.config();
const app = express();
const SERVICE_PRODUCT_HOST = process.env.GREENMARKET_PRODUCT_APP_SERVICE_HOST || 'localhost';
const SERVICE_PRODUCT_PORT = process.env.GREENMARKET_PRODUCT_APP_SERVICE_PORT || '8080';
const REDIS_MASTER_HOST = process.env.REDIS_MASTER_SERVICE_HOST || 'localhost';
const REDIS_MASTER_PORT = process.env.REDIS_MASTER_SERVICE_PORT || '6379'; 

app.use(helmet());
app.use(logger("dev"));
app.use(cookieParser());
app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());

app.use("/api/chat", chatRouter);

const server = app.listen(process.env.PORT, () => {
	console.log(`✅ server listen http://localhost:${process.env.PORT}`);
});

const io = socketIo(server, {path: "/ws/chat"});
// TODO redis adapter 적용시 socket.emit에서 발생하는 에러 해결
// io.adapter(redis({ host: REDIS_MASTER_HOST, port: Number(REDIS_MASTER_PORT) }));
io.use(socketIoCookieParser());
io.use((socket, next)=>{
	try{
		const token = socket.request.cookies.x_auth;
		console.log(token, process.env.SECRET_TOKEN);
		const decoded = jwt.verify(token, process.env.SECRET_TOKEN);
		const { id } = decoded;
		socket.userId = id;
		next();
	} catch(error) {
		const err = new Error("NotAuthorized");
		err.data = { type: "NotAuthorized"};
		next(err);
	}
});

io.on("connection", async (socket) => {
	console.log("✅ An user connected");
	try{
		const response = await axios.get(`http://${SERVICE_PRODUCT_HOST}:${SERVICE_PRODUCT_PORT}/api/products?user_id=${socket.userId}`);
		const articleIds = response.data.productArticles.map((e)=>{
			return e.id;
		});
		const chatRoom = await ChatRoom.findAll({
			where: { 
				[Op.or] : [ 
					{article_id : {[Op.in] : articleIds}} , 
					{user_id_buyer : socket.userId}
				]
			},
			order: [
				['article_id']
			]
			
		});
		socket.join(`user_${socket.userId}`);
		chatRoom.forEach( room => {
			socket.join(`room_${room.id}`);
		});
	} catch (error) {
		console.error(error);
	}

	socket.on("sendMessage", async ({message, userId, roomId}) => {
		console.log(message, userId, roomId);
		try{
			const response = await ChatMessage.create({ room_id: roomId, user_id: userId, message });
			console.log(response);
			socket.to(`room_${roomId}`).emit("sendMessage", { ...response.dataValues });
		} catch(error){
			console.log("❌",error);
		}
	});

	socket.on("disconnect", ()=>{
		console.log("❕ An user disconnected");
		socket.leaveAll();
	});

});
