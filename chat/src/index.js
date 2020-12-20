import express from "express";
import socketIo from "socket.io";
import dotenv from "dotenv";
import logger from "morgan";
import helmet from "helmet";
import cookieParser from "cookie-parser";
import socketIoCookieParser from "socket.io-cookie-parser";
import bodyParser from "body-parser";
import chatRouter from "./roters/chatRouter";
import redis from "socket.io-redis";
import socketController from "./controllers/socketController";
import { authJwt, initializeSocket } from "./middlewares/socketMiddlewares";

dotenv.config();
const app = express();

const REDIS_MASTER_HOST = process.env.REDIS_MASTER_SERVICE_HOST || "localhost";
const REDIS_MASTER_PORT = process.env.REDIS_MASTER_SERVICE_PORT || "6379";

app.use(helmet());
app.use(logger("dev"));
app.use(cookieParser());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use("/api/chat", chatRouter);

const server = app.listen(process.env.PORT, () => {
	console.log(`✅ server listen http://localhost:${process.env.PORT}`);
});

const io = socketIo(server, { path: "/ws/chat" });

io.adapter(redis({ host: REDIS_MASTER_HOST, port: Number(REDIS_MASTER_PORT) }));

io.use(socketIoCookieParser());
io.use(authJwt);
io.use(initializeSocket);

io.on("connection", socketController);
