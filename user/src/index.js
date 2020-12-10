import express from "express";
import morgan from "morgan";
import helmet from "helmet";
import dotenv from "dotenv";
import cookieParser from "cookie-parser";
import bodyParser from "body-parser";
import userRouter from "./routers/userRouter";

const app = express();

dotenv.config();

app.use(helmet());
app.use(morgan("dev"));
app.use(cookieParser());
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

app.use("/api/user", userRouter);

app.listen(process.env.PORT, () => {
	console.log(`✅ Server Listen : http://localhost:${process.env.PORT}`);
});