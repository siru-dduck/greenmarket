import multer from "multer";

const multerImage = multer({ dest: "resources/images/profile" });

export const uploadImage = multerImage.single("profileImage");
