import jwt from "jsonwebtoken";

export const authJwt = (req, res, next) => {
	try {
		const authHeader = req.header("Authorization");
		const token = authHeader.substring("Bearer ".length);
		const authUser = jwt.verify(token, process.env.SECRET_TOKEN);
		req.authUser = authUser;
		next();
	} catch (error) {
		console.error(error);
		const err = new Error("NotAuthorized");
		err.data = { type: "NotAuthorized" };
		next(err);
	}
};