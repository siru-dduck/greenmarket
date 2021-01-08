import "@babel/polyfill";
import { User as UserModel } from "../models";
import jwt from "jsonwebtoken";

export const loginUser = async (req, res) => {
	const { email, password } = req.body;
	try {
		const user = await UserModel.findOne({ where: { email } });
		if (!user) {
			return res.json({
				loginSuccess: false,
				message: "이메일에 해당하는 유저정보가 없습니다.",
			});
		}
		if (user.dataValues.password === password) {
			const {
				id,
				email,
				address1,
				address2,
				nickname,
				profile_image_url,
			} = user.dataValues;
			const expireTime = Math.floor(Date.now() / 1000) + 60 * 60;
			const token = jwt.sign(
				{ id, email, exp: expireTime },
				process.env.SECRET_TOKEN
			);
			res
				.cookie("x_auth", token, {
					expires: new Date(Date.now() + 1000 * 60 * 60),
					httpOnly: true,
				})
				.json({
					loginSuccess: true,
					user: { id, email, address1, address2, nickname, profile_image_url },
				});
		} else {
			res.json({
				loginSuccess: false,
				message: "비밀번호가 일치하지 않습니다.",
			});
		}
	} catch (error) {
		console.log(error);
		res.json({ loginSuccess: false });
	}
};

export const logoutUser = (req, res) => {
	const token = req.cookies.x_auth;
	if (!token) {
		return res.status(400).json({
			logoutSuccess: false,
			code: 400,
			message: "전송된 토큰이 없습니다.",
		});
	}
	return res
		.cookie("x_auth", "", { expires: new Date(Date.now()), httpOnly: true })
		.json({ logoutSuccess: true });
};

export const authUser = (req, res) => {
	const token = req.cookies.x_auth;
	if (!token) {
		return res
			.status(200)
			.json({ isAuth: false, code: 200, message: "전송된 토큰이 없습니다." });
	}
	try {
		const decoded = jwt.verify(token, process.env.SECRET_TOKEN);
		const { id, email } = decoded;
		return res.json({ isAuth: true, code: 200, user: { id, email } });
	} catch (error) {
		if (error.name === "TokenExpiredError") {
			return json
				.status(419)
				.json({ isAuth: false, code: 419, message: "토큰이 만료되었습니다." });
		}
		return res
			.status(401)
			.json({ isAuth: false, code: 401, message: "유효하지 않은 토큰입니다." });
	}
};
