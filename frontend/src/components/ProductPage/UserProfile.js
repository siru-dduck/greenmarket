import React, { useState, useContext } from "react";
import { Link } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa";
import { AiFillHeart, AiOutlineHeart } from "react-icons/ai";
import styled from "styled-components";
import UserContext from "../../util/context/User.context";
import axios from "axios";

function UserProfile(props) {
	const {
		state: { user },
	} = useContext(UserContext);
	const [interest, setInterest] = useState({
		count: props.interestCount,
		isChecked: props.isCheckedInterest,
	});
	const onClickInterestButton = () => {
		if (!user) {
			alert("로그인후 이용해주세요.");
			props.history.push("/login");
		} else {
			if (interest.isChecked) {
				axios
					.delete(`/api/products/${props.articleId}/interest`)
					.then(() => {
						setInterest({ count: interest.count - 1, isChecked: false });
					})
					.catch((error) => {
						console.log(error);
					});
			} else {
				axios
					.post(`/api/products/${props.articleId}/interest`)
					.then(() => {
						setInterest({ count: interest.count + 1, isChecked: true });
					})
					.catch((error) => {
						console.log(error);
					});
			}
		}
	};
	const onClickChatButton = () => {
		if (!user) {
			alert("로그인후 이용해주세요.");
			props.history.push("/login");
		} else {
			if (props.chatRoomId) {
				props.history.push(`/chat?room_id=${props.chatRoomId}`);
				return;
			}
			const { id } = props.match.params;
			axios
				.post("/api/chat/room", { article_id: id })
				.then((response) => {
					console.log(response);
					if (response.data.isSuccess) {
						props.history.push(`/chat?room_id=${response.data.chatRoomId}`);
					} else {
						alert("채팅방 생성에 실패했습니다. 다시 시도해주세요.");
					}
				})
				.catch((error) => {
					console.log(error);
				});
		}
	};
	const onClickDeleteButton = () => {
		if (!user) {
			alert("로그인후 이용해주세요.");
			props.history.push("/login");
		} else {
			const { id } = props.match.params;
			axios
				.delete(`/api/products/${id}`)
				.then((response) => {
					if (response.status === 204) {
						alert("게시글을 삭제했습니다.");
						props.history.push("/");
					} else {
						alert("게시글 삭제에 실패했습니다.");
					}
				})
				.catch((error) => {
					console.log(error);
					alert("게시글 삭제에 실패했습니다.");
				});
		}
	};

	return (
		<Address>
			<div className="user-profile-address__column">
				<Link to={`/user/${props.user.id}/profile`}>
					<div className="user-profile">
						<div className="user-profile__image">
							{props.user.profile_image_url ? (
								<img
									src={props.user.profile_image_url}
									alt="유저의 프로필 사진"
								/>
							) : (
								<FaUserCircle size="40" color="#bdc3c7" />
							)}
						</div>
						<div className="user-profile__description">
							<div className="user-profile__username">
								{props.user.nickname}
							</div>
							<div className="user-profile__region">
								{props.user.address1} {props.user.address2}
							</div>
						</div>
					</div>
				</Link>
			</div>
			<div className="user-profile-address__column">
				{user && user.id === props.user.id ? (
					<div className="btn-box">
						<Link to={`/products/${props.articleId}/edit`}>
							<Button>수정하기</Button>
						</Link>
						<Button onClick={onClickDeleteButton} backgroundColor="#e74c3c">
							삭제하기
						</Button>
					</div>
				) : (
					<div className="btn-box">
						<Button onClick={onClickInterestButton} backgroundColor="#ccc">
							{interest.isChecked ? (
								<AiFillHeart fontSize="1.25em" />
							) : (
								<AiOutlineHeart fontSize="1.25em" />
							)}

							<span>관심</span>
							<span className="interest-count">{interest.count}</span>
						</Button>
						<Button onClick={onClickChatButton}>채팅하기</Button>
					</div>
				)}
			</div>
		</Address>
	);
}

const Address = styled.address`
	width: 659px;
	margin-top: 32px;
	padding-bottom: 22px;
	border-bottom: 1px solid #dbdbdb;
	display: flex;
	justify-content: space-between;

	.user-profile {
		display: flex;
		.user-profile__image {
			width: 40px;
			height: 40px;
			margin-right: 14px;
			img {
				border-radius: 50%;
				width: 100%;
				height: 100%;
			}
		}
		.user-profile__description {
			display: flex;
			flex-direction: column;
			.user-profile__username {
				font-weight: 500;
			}
			.user-profile__region {
				font-size: 13px;
				color: #757575;
			}
		}
	}

	.btn-box {
		display: flex;
		button {
			margin-right: 12px;
			.interest-count {
				margin-left: 4px;
			}
			svg {
				margin-right: 5px;
			}
		}
		button::last-child {
			margin-right: 0px;
		}
	}
`;

const Button = styled.button`
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 12px 0px;
	color: #fcfcfc;
	font-size: 18px;
	font-weight: 500;
	min-width: 140px;
	background-color: ${(props) => props.backgroundColor || "#1dd1a1"};
	border: none;
	border-radius: 4px;
	outline: none;
	&:active {
		background-image: linear-gradient(
			to top,
			rgba(0, 0, 0, 0.075),
			rgba(0, 0, 0, 0)
		);
	}
`;

export default UserProfile;
