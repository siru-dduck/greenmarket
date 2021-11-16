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
    }
});

const ChatRoomSchema = new mongoose.Schema({
    productId: {
        type: Number,
        required: "product id is required"
    },
    sellerUserId: {
        type: Number,
        required: "seller user id is required"
    },
    buyerUserId: {
        type: Number,
        required: "buyer user id is required"
    },
    messages: [ChatMessageSchema],
    createdDate: {
        type: Date,
        default: Date.now,
    }
});

ChatRoomSchema.index({ productId: 1, buyerUserId: 1 }, { unique: true });

const model = mongoose.model("ChatRoom", ChatRoomSchema);

export default model;