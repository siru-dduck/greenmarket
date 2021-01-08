import express from "express";
import { authUser, loginUser, logoutUser } from "../controllers/authController";
import {
	getUser,
	getUserById,
	isExistUser,
	registerUser,
	updateUser,
} from "../controllers/userController";
import { uploadImage } from "../middlewares/imageUploadMiddleware";

const router = express.Router();

router.post("/join", registerUser);
router.post("/login", loginUser);
router.get("/logout", logoutUser);
router.get("/auth", authUser);
router.get("/valid", isExistUser);
router.get("/:id", getUserById);
router.put("/:id", uploadImage, updateUser);
router.get("", getUser);
export default router;
