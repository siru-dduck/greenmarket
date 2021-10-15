import { ChatMessage } from "../models";

const socketController = async (socket) => {
	console.log("✅ An user connected");

	socket.on("sendMessage", async ({ message, userId, roomId }) => {
		try {
			const response = await ChatMessage.create({
				room_id: roomId,
				user_id: userId,
				message,
			});
			socket
				.to(`room_${roomId}`)
				.emit("sendMessage", { ...response.dataValues });
		} catch (error) {
			console.log("❌", error);
		}
	});

	socket.on("disconnect", () => {
		console.log("❕ An user disconnected");
		socket.leaveAll();
	});
};

export default socketController;
