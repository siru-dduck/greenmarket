import React, { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";
import axios from "axios";
import qs from "qs";
import TopHeader from "../Header/TopHeader";
import { MainLayout } from "../../util/style/LayoutStyle";
import UserContext from "../../util/context/User.context";
import { FaUserCircle } from "react-icons/fa";
import io from "socket.io-client";

function ChatPage(props) {
	const {
		state: { user },
	} = useContext(UserContext);
	const [room, setRoom] = useState({ isLoading: false });
	const [chat, setChat] = useState({ currentRoomId: null });
	const [socket, setSocket] = useState(null);

	const fetchChatMessages = async (chatRoom, roomId) => {
		try {
			const response = await axios.get(`/api/chat/room/${roomId}/messages`);
			let currentChatRoom = null;
			if (chatRoom && chatRoom.length > 0) {
				currentChatRoom = chatRoom.find((e) => {
					if (e.id === roomId) {
						return true;
					}
					return false;
				});
			}
			setChat({
				currentRoomId: Number(roomId),
				currentRoomMessages: response.data.messages,
				currentChatRoom,
			});
		} catch (error) {
			alert("채팅을 불러오는데 실패했습니다. 다시 시도해주세요.");
			console.log(error);
		}
	};

	const onClickChatRoomList = async (e) => {
		let { target } = e;
		while (target.tagName !== "LI") {
			target = target.parentElement;
		}
		const { roomId } = target.dataset;
		fetchChatMessages(room.chatRoom, Number(roomId));
	};

	const onSubmitChatMessage = (e) => {
		e.preventDefault();
		console.log(chat, room);
		const message = e.currentTarget["chat-message"];
		if (message.value.trim().length <= 0) {
			return;
		}

		socket.emit("sendMessage", {
			message: message.value,
			userId: user.id,
			roomId: chat.currentRoomId,
		});
		const { currentRoomMessages } = chat;
		currentRoomMessages.push({ message: message.value, user_id: user.id });
		setChat({
			...chat,
			currentRoomMessages,
		});
		message.value = "";
	};

	useEffect(() => {
		const fetchChatRoom = async () => {
			const query = qs.parse(props.location.search, {
				ignoreQueryPrefix: true,
			});
			try {
				const response = await axios.get(`/api/chat/room?user_id=${user.id}`);
				console.log(response);
				if (!query.room_id || Number.isNaN(Number(query.room_id))) {
					setRoom({
						isLoading: true,
						chatRoom: response.data.chatRoom,
					});
				} else {
					setRoom({
						isLoading: true,
						chatRoom: response.data.chatRoom,
					});
					fetchChatMessages(response.data.chatRoom, Number(query.room_id));
				}
			} catch (error) {
				console.error(error);
			}
		};
		fetchChatRoom();
		setSocket(
			io.connect("localhost:4000/", {
				path: "/ws/chat",
				transports: ["websocket"],
			})
		);
		// eslint-disable-next-line
	}, []);

	useEffect(() => {
		return () => {
			if (socket) {
				socket.close();
			}
		};
	}, [socket]);

	useEffect(() => {
		if (socket) {
			socket.on("connect", () => {
				console.log("socket connected");
			});

			socket.on("sendMessage", ({ message, user_id, room_id }) => {
				const { currentRoomMessages } = chat;
				currentRoomMessages.push({ message, user_id, room_id });
				setChat({
					...chat,
					currentRoomMessages,
				});
			});

			socket.on("error", (err) => {
				console.error(err);
				if (err.type === "NotAuthorized") {
					alert("로그인후 이용해 주세요.");
					props.history.push("/login");
				}
			});

			socket.on("disconnect", (data) => {
				console.log(data);
			});

			socket.on("connect_error", () => {
				alert("채팅서버와 연결이 실패하였습니다.");
			});
		}
		return () => {
			if (socket) {
				socket.off("connect");
				socket.off("error");
				socket.off("disconnect");
				socket.off("connect_error");
				socket.off("sendMessage");
			}
		};
		// eslint-disable-next-line
	}, [socket, chat]);

	return (
		<>
			<TopHeader {...props} />
			<MainLayout>
				<ChatLayout>
					<ChatAside>
						<header>
							<h2>채팅</h2>
						</header>
						<ul onClick={onClickChatRoomList}>
							{room.isLoading
								? room.chatRoom.map((e, index) =>
										e.article ? (
											<li key={index} data-room-id={e.id}>
												<div>
													<FaUserCircle size="42" color="#bdc3c7" />
												</div>
												<div className="chat-user-inform">
													<div className="chat-user-name">
														{user.id === e.user_id_seller
															? e.user_buyer.nickname
															: e.user_seller.nickname}
													</div>
													<div className="chat-product-message">
														{e.chat_messages.length > 0
															? e.chat_messages[0].message
															: "새체팅방"}
													</div>
												</div>
												<div className="chat-product-image-box">
													<img
														src={e.article.productImages[0].fileUrl}
														alt="상품 이미지"
													/>
												</div>
											</li>
										) : null
								  )
								: null}
						</ul>
					</ChatAside>
					<ChatSection>
						{chat.currentChatRoom ? (
							<>
								<div className="chat-wrapper">
									<Link to={`/product/${chat.currentChatRoom.article.id}`}>
										<header>
											<div className="chat-image-box">
												<img
													src={
														chat.currentChatRoom.article.productImages[0]
															.fileUrl
													}
													alt="상품이미지"
												/>
											</div>
											<div className="chat-inform">
												<h3>{chat.currentChatRoom.article.title}</h3>
												<div className="user-nickname">
													{chat.currentChatRoom.user_seller.nickname}
												</div>
											</div>
										</header>
									</Link>
									<div className="chat-display">
										<ul className="chatList">
											{chat.currentRoomMessages.map((e, index) =>
												user.id !== e.user_id ? (
													<li key={index} className="seller">
														<FaUserCircle size="34" color="#bdc3c7" />
														<div className="chat-box">{e.message}</div>
													</li>
												) : (
													<li key={index} className="buyer">
														<div className="chat-box">{e.message}</div>
													</li>
												)
											)}
										</ul>
									</div>
								</div>
								<div className="chat-form-wrapper">
									<form onSubmit={onSubmitChatMessage} className="chat-form">
										<div className="chat-form__column-left">
											<textarea
												name="chat-message"
												placeholder="채팅을 입력하세요."
											></textarea>
										</div>
										<div className="chat-form__column-right">
											<button type="submit">전송</button>
										</div>
									</form>
								</div>
							</>
						) : null}
					</ChatSection>
				</ChatLayout>
			</MainLayout>
		</>
	);
}

const ChatLayout = styled.div`
	display: flex;
	height: calc(100vh - 69px);
`;

const ChatAside = styled.aside`
	width: 25vw;
	border-right: 1px solid #ddd;
	background: #fff;
	min-width: 320px;
	header {
		padding: 16px;
		display: flex;
		justify-content: center;
		border-bottom: 1px solid #dbdbdb;
		h2 {
			font-size: 24px;
			font-family: "yg-jalnan";
			color: #333;
		}
	}
	ul {
		display: flex;
		flex-direction: column;
		li {
			border-bottom: 1px solid #dbdbdb;
			display: flex;
			align-items: center;
			padding: 8px 12px;
			.chat-user-inform {
				margin-left: 12px;
				flex-grow: 1;
				overflow: hidden;
				.chat-user-name {
					font-weight: 500;
				}
				.chat-product-message {
					font-size: 13px;
					color: #333;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
				}
			}
			.chat-product-image-box {
				width: 42px;
				min-width: 42px;
				height: 42px;
				img {
					width: 100%;
					height: 100%;
					border-radius: 10px;
				}
			}
		}
	}
`;

const ChatSection = styled.section`
	width: calc(100vw - 25vw);
	height: 100%;
	background: #fcfcfc;
	display: flex;
	flex-direction: column;
	.chat-wrapper {
		height: 80%;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		header {
			padding-top: 12px;
			display: flex;
			align-items: center;
			.chat-image-box {
				margin-right: 8px;
				display: flex;
				align-items: center;
				img {
					width: 42px;
					height: 42px;
					border-radius: 10px;
				}
			}
			.chat-inform {
				display: flex;
				flex-direction: column;
				h3 {
					font-size: 18px;
					font-weight: 500;
					color: #333;
				}
				.user-nickname {
					font-size: 14px;
					color: #333;
				}
			}
		}

		.chat-display {
			padding-top: 24px;
			width: 60%;
			height: 100%;
			overflow: auto;
			ul {
				display: flex;
				flex-direction: column;
				align-items: flex-start;
				li {
					margin-bottom: 10px;
					display: flex;
					display: flex;
					align-items: center;
					.chat-box {
						border-radius: 10px;
						padding: 8px 15px;
					}
					&.buyer {
						align-self: flex-end;
						.chat-box {
							border: 1px solid #bbb;
							color: #333;
							align-self: flex-end;
						}
					}
					&.seller {
						color: #fff;
						.chat-box {
							margin-left: 8px;
							background-color: #1dd1a1;
						}
					}
				}
			}
		}
	}
	.chat-form-wrapper {
		height: 30%;
		display: flex;
		justify-content: center;

		.chat-form {
			height: 100%;
			display: flex;
			justify-content: center;
			width: 60%;
			.chat-form__column-left {
				display: flex;
				margin-right: 16px;
				flex-grow: 1;
				flex-direction: column;
				textarea[name="chat-message"] {
					border-radius: 6px;
					font-size: 15px;
					width: 100%;
					height: 100px;
					padding: 8px 12px;
					margin-right: 18px;
					resize: none;
					border: 1px solid #ddd;
					outline: none;
					font-size: 15px;
					color: #333;
					align-self: stretch;
					font-family: inherit;
				}
			}
			.chat-form__column-right {
				button[type="submit"] {
					height: 100px;
					padding: 0 16px;
					border-radius: 6px;
					outline: none;
					border: none;
					background-color: #1dd1a1;
					color: #fff;
					font-size: 15px;
					font-weight: 500;
					&:hover {
						transform: scale(1.02);
					}
				}
			}
		}
	}
`;

export default ChatPage;
