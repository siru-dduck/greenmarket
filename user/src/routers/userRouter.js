import express from "express";
import { authUser, getUser, getUserById, isExistUser, joinUser, loginUser, logoutUser } from "../controllers/userController";

const router = express.Router();

router.post("/join", joinUser);
router.post("/login", loginUser);
router.get("/logout", logoutUser);
router.get("/auth", authUser);
router.get("/valid", isExistUser);
router.get("/:id", getUserById);
router.get("", getUser);
export default router;
