import mongoose from "mongoose";

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
    createdDate: {
        type: Date,
        default: Date.now,
    }
});

const model = mongoose.model("ChatRoom", ChatRoomSchema);
export default model;