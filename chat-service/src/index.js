import express from "express";
import socketIo from "socket.io";
import path from "path"
import dotenv from "dotenv";
import logger from "morgan";
import helmet from "helmet";
import { connect } from "./inMemoryDB";
import cookieParser from "cookie-parser";
import socketIoCookieParser from "socket.io-cookie-parser";
import chatRouter from "./roters/chatRouter";
import redis from "socket.io-redis";
import socketController from "./controllers/socketController";
import { authJwt, initializeSocket } from "./middlewares/socketMiddlewares";

/* dotnet config */
const __dirname = path.resolve();
if (process.env.NODE_ENV === 'production') {
	dotenv.config({ path: path.join(__dirname, '.env.production') })
} else if (process.env.NODE_ENV === 'development') {
	console.log("⚙️ development env");
	dotenv.config({ path: path.join(__dirname, '.env.development') })
} else {
	console.log("⚙️ local env");
	dotenv.config({ path: path.join(__dirname, '.env') })
	connect();
}
console.dir(process.env);

/* express setting */
const app = express();

const REDIS_MASTER_HOST = process.env.REDIS_MASTER_SERVICE_HOST;
const REDIS_MASTER_PORT = process.env.REDIS_MASTER_SERVICE_PORT;

app.use(helmet());
app.use(logger("dev"));
app.use(cookieParser());
app.use(express.urlencoded({ extended: false }));
app.use(express.json());

app.use("/api/chat", chatRouter);

const server = app.listen(process.env.PORT, () => {
	console.log(`✅ server listen http://localhost:${process.env.PORT}`);
});

/* socket.io setting */
const io = socketIo(server, { path: "/ws/chat" });

io.adapter(redis({ host: REDIS_MASTER_HOST, port: Number(REDIS_MASTER_PORT) }));

io.use(socketIoCookieParser());
io.use(authJwt);
io.use(initializeSocket);

io.on("connection", socketController);