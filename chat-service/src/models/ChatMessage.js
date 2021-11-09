import mongoose from "mongoose";

const ChatMessageSchema = new mongoose.Schema({
    userId: {
        type: Number,
        required: "user id is required"
    },
    message: {
        type: String,
        length: 500
    },
    type: {
        type: String,
        required: "type is required"
    },
    createdDate: {
        type: Date,
        default: Date.now,
    },
    roomId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: "ChatRoom",
    },
});

const model = mongoose.model("ChatMessage", ChatMessageSchema);
export default model;