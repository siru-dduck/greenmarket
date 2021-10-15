import express from "express";
import path from "path";
import morgan from "morgan";
import helmet from "helmet";
import dotenv from "dotenv";
import cookieParser from "cookie-parser";
import bodyParser from "body-parser";
import userRouter from "./routers/userRouter";

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
}
console.dir(process.env);

/* express setting */
const app = express();

app.use(helmet());
app.use(morgan("dev"));
app.use("/resources", express.static("resources"));
app.use(cookieParser());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use("/api/user", userRouter);

app.listen(process.env.PORT, () => {
	console.log(`✅ Server Listen : http://localhost:${process.env.PORT}`);
});
