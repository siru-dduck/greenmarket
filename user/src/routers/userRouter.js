import express from "express";
import { authUser, loginUser, logoutUser } from "../controllers/authController";
import {
	getUsers,
	getUserById,
	isExistUser,
	registerUser,
	updateUser,
} from "../controllers/userController";
import { uploadImage } from "../middlewares/imageUploadMiddleware";
import { decodeToken } from "../middlewares/jwtMiddleware";

const router = express.Router();

router.post("/join", registerUser);
router.post("/login", loginUser);
router.get("/logout", logoutUser);
router.get("/auth", decodeToken, authUser);
router.get("/valid", isExistUser);
router.get("/:id", getUserById);
router.put("/:id", decodeToken, uploadImage, updateUser);
router.get("", getUsers);

export default router;
