import mongoose from "mongoose";

const ProductSchema = new mongoose.Schema({
    productId: {
        type: Number,
        required: "product id is required"
    },
    title: {
        type: String,
        required: "title is required"
    },
    address1: {
        type: String,
        required: "address1 is required"
    },
    address2: {
        type: String,
        required: "address2 is required"
    }
});

const model = mongoose.model("User", UserSchema);
export default model;