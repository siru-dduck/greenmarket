import mongoose from "mongoose";

const UserSchema = new mongoose.Schema({
    userId: {
        type: Number,
        required: "user id is required",
        unique: true
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