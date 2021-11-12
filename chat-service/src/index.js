import "./dotenv";
import express from "express";
import socketIo from "socket.io";
import logger from "morgan";
import helmet from "helmet";
import cookieParser from "cookie-parser";
import socketIoCookieParser from "socket.io-cookie-parser";
import chatApiRouter from "./roters/chatRouter";
import redis from "socket.io-redis";
import socketController from "./controllers/socketController";
import { authJwt, initializeSocket } from "./middlewares/socketMiddlewares";

/* express setting */
const app = express();

app.use(helmet());
app.use(logger("dev"));
app.use(cookieParser());
app.use(express.urlencoded({ extended: false }));
app.use(express.json());

app.use("/api", chatApiRouter);

const server = app.listen(process.env.PORT, () => console.log(`✅ server listen http://localhost:${process.env.PORT}`));

/* socket.io setting */
const io = socketIo(server, { path: "/ws/chat" });

io.adapter(redis({ 
	host: process.env.REDIS_MASTER_SERVICE_HOST, 
	port: Number(process.env.REDIS_MASTER_SERVICE_PORT) 
}));

io.use(socketIoCookieParser());
io.use(authJwt);
io.use(initializeSocket);

io.on("connection", socketController);