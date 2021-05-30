import React, { useContext, useEffect, useState } from "react";
import styled from "styled-components";
import TopHeader from "../Header/TopHeader";
import { MainLayout } from "../../util/style/LayoutStyle";
import { FaUserCircle } from "react-icons/fa";
import axios from "axios";
import { Link } from "react-router-dom";
import UserContext from "../../util/context/User.context";

function ProfilePage(props) {
	const {
		state: { user },
	} = useContext(UserContext);
	const [userProfile, setUserProfile] = useState({ isLoading: false });
	useEffect(() => {
		axios
			.get(`/api/user/${props.match.params.id}`)
			.then((response) => {
				setUserProfile({ isLoading: true, profile: response.data.user });
			})
			.catch(() => {
				alert("유저정보를 찾을 수 없습니다.");
				props.history.push("/");
			});
		// eslint-disable-next-line
	}, []);

	return (
		<>
			<TopHeader />
			<MainLayout>
				{!userProfile.isLoading ? (
					"로딩중 ..."
				) : (
					<UserProfile>
						<h1 className="user-profile__title">프로필</h1>
						<div className="user-profile">
							<div className="user-profile__image">
								{userProfile.profile.profile_image_url ? (
									<img
										src={userProfile.profile.profile_image_url}
										alt="유저 프로필 이미지"
									/>
								): (
									<FaUserCircle size="24" color="#bdc3c7" />
								)}
							</div>
							<div className="user-profile__description">
								<span className="user-profile__nickname">
									{userProfile.profile.nickname}
								</span>
								<span className="user-profile__address">
									{userProfile.profile.address1}
								</span>
							</div>
							{user && user.id === userProfile.profile.id ? (
								<div className="user-profile__edit-btn">
									<Button>
										<Link to={`/user/${props.match.params.id}/profile/edit`}>
											프로필 수정
										</Link>
									</Button>
								</div>
							) : null}
						</div>
					</UserProfile>
				)}
			</MainLayout>
		</>
	);
}

const UserProfile = styled.div`
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	.user-profile__title {
		margin-top: 42px;
		font-size: 48px;
		color: #333;
		font-family: "yg-jalnan";
	}
	.user-profile {
		margin-top: 32px;
		display: flex;
		justify-content: center;
		align-items: center;
		.user-profile__image {
			margin-right: 16px;
			img {
				border-radius: 50%;
				width: 72px;
				height: 72px;
			}
		}
		.user-profile__description {
			display: flex;
			margin-right: 42px;
			flex-direction: column;
			justify-content: center;
			align-items: flex-start;
			.user-profile__nickname {
				font-weight: 500;
				font-size: 20px;
			}
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

export default ProfilePage;
