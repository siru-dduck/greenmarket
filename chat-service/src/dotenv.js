import path from "path"
import dotenv from "dotenv";
import { connect } from "./inMemoryDB";

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