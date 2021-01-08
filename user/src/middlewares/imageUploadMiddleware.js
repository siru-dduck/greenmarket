import multer from "multer";

const multerImage = multer({ dest: "resource/images/profile" });

export const uploadImage = multerImage.single("profileImage");
