import jwt from "jsonwebtoken";

export const decodeToken = (req, res, next) => {
	const token = req.cookies.x_auth;
	if (token) {
		try {
			const decodedToken = jwt.verify(token, process.env.SECRET_TOKEN);
			req.decodedToken = decodedToken;
		} catch (error) {
			console.error(error);
			if (error.name === "TokenExpiredError") {
				return json.status(401).json({
					isSuccess: false,
					code: 401,
					message: "토큰이 만료되었습니다.",
				});
			}
			return res
				.status(401)
				.json({
					isAuth: false,
					code: 401,
					message: "유효하지 않은 토큰입니다.",
				});
		}
	} else {
		req.decodedToken = null;
	}
	next();
};
