import mongoose from "mongoose";

const UserSchema = new mongoose.Schema({
    userId: {
        type: Number,
        required: "user id is required"
    },
    nickName: {
        type: String,
        required: "user nickname is required"
    },
    chatRooms: [
        {
            type: mongoose.Schema.Types.ObjectId,
            ref: "ChatRoom"
        }
    ]
});

const model = mongoose.model("User", UserSchema);
export default model;