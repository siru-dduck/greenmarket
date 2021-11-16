import { createChatMessage } from "../service/chatService";
import { emit } from "../service/socketService";

const socketController = async (socket) => {
	console.log("✅ An user connected");

	socket.on("sendMessage", async ({ roomId, message }) => {
		const { authUser } = socket; 
		try {
			// 채팅메세지 생성
			const messageId = await createChatMessage(roomId, authUser.userId, message);
			
			// 채팅방 사용자에게 이벤트 전송 
			emit(`room_${roomId}`,"sendMessage", { messageId, roomId } );
		} catch (error) {
			console.log("❌", error);
			socket.emit("error", { from: "sendMessage", error: error });
		}
	});

	socket.on("disconnect", () => {
		console.log("❕ An user disconnected");
		socket.leaveAll();
	});
};

export default socketController;
