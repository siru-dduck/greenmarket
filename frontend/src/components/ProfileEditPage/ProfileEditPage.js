import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { FormLayout } from "../../util/style/FormStyle";
import { MainLayout } from "../../util/style/LayoutStyle";
import TopHeader from "../Header/TopHeader";
import { GoPencil } from "react-icons/go";
import axios from "axios";

function ProfileEditPage(props) {
	const [userProfile, setUserProfile] = useState({ isLoading: false });
	const [profileImageFile, setProfileImageFile] = useState(null);
	const onClickImageEditButton = (e) => {
		e.preventDefault();
		const input = document.createElement("input");
		input.setAttribute("type", "file");
		input.click();
		input.onchange = (e) => {
			setProfileImageFile(e.target.files[0]);
			setUserProfile({
				...userProfile,
				profile: {
					...userProfile.profile,
					profile_image_url: URL.createObjectURL(e.target.files[0]),
				},
			});
		};
	};

	const onSubmit = (e) => {
		e.preventDefault();
		const { nickname, address1, address2 } = e.currentTarget;

		if (
			nickname.value.trim().length <= 0 ||
			address1.value.trim().length <= 0 ||
			address2.value.trim().length <= 0 ||
			!profileImageFile
		) {
			alert("양식을 올바르게 입력해주세요.");
			return;
		}

		const formData = new FormData();
		const config = {
			headers: {
				"content-type": "multipart/form-data",
			},
		};

		formData.append("profileImage", profileImageFile);
		formData.append("nickname", nickname.value);
		formData.append("address1", address1.value);
		formData.append("address2", address2.value);

		axios
			.put(`/api/user/${props.match.params.id}`, formData, config)
			.then((response) => {
				console.log(response);
				if (response.data.isSuccess) {
					props.history.push(`/user/${props.match.params.id}/profile`);
				} else {
					alert("프로필수정에 실패하였습니다.");
				}
			})
			.catch(() => {
				alert("프로필수정에 실패하였습니다.");
			});
	};

	useEffect(() => {
		axios
			.get(`/api/user/${props.match.params.id}`)
			.then((response) => {
				const { user } = response.data;
				setUserProfile({ isLoading: true, profile: user });
				if (user.profile_image_url) {
					axios
						.get(user.profile_image_url, {
							responseType: "blob",
						})
						.then((response) => {
							setProfileImageFile(
								new File([response.data], "", { type: response.data.type })
							);
						});
				}
			})
			.catch(() => {
				alert("유저정보를 찾을 수 없습니다.");
				props.history.push("/");
			});
	}, []);

	return (
		<>
			<TopHeader />
			<MainLayout>
				{!userProfile.isLoading ? (
					"로딩중"
				) : (
					<FormLayout>
						<EditForm onSubmit={onSubmit}>
							<h2>프로필 수정</h2>
							<div className="profile-image">
								<img src={userProfile.profile.profile_image_url} />
								<button
									onClick={onClickImageEditButton}
									className="profile-image__edit-btn"
								>
									<GoPencil /> 수정
								</button>
								<span>프로필 이미지</span>
							</div>
							<div className="user-nickname">
								<label>닉네임</label>
								<input
									type="text"
									name="nickname"
									defaultValue={userProfile.profile.nickname}
								/>
							</div>
							<div className="adress">
								<label>주소</label>
								<input
									type="text"
									name="address1"
									defaultValue={userProfile.profile.address1}
								/>
								<input
									type="text"
									name="address2"
									defaultValue={userProfile.profile.address2}
								/>
							</div>
							<button type="submit">수정</button>
						</EditForm>
					</FormLayout>
				)}
			</MainLayout>
		</>
	);
}

const EditForm = styled.form`
	margin-top: 16px;
	border: 1px solid #dbdbdb;
	border-radius: 6px;
	padding: 16px 24px;
	display: flex;
	align-items: flex-start;
	flex-direction: column;

	h2 {
		font-size: 28px;
		font-family: "yg-jalnan";
		color: #333;
		margin-bottom: 32px;
		align-self: center;
	}

	input {
		margin: 0;
		padding: 8px 13px;
		font-size: 15px;
		font-weight: 500;
		font-family: inherit;
		color: #333;
		outline: none;
		border-radius: 6px;
		border: 1px solid #dbdb;
	}

	label {
		margin-right: 16px;
		width: 120px;
	}

	.profile-image {
		position: relative;
		display: flex;
		flex-direction: column;
		align-items: center;
		img {
			border-radius: 50%;
			width: 128px;
			height: 128px;
		}

		span {
			font-weight: 500;
		}

		.profile-image__edit-btn {
			position: absolute;
			top: 0;
			right: -10px;
			padding: 2px 4px;
			border-radius: 3px;
			background: rgba(0, 0, 0, 0.85);
			color: white;
			font-size: 10px;
		}
	}

	.user-nickname {
		margin-top: 32px;
		display: flex;
		align-items: center;
	}

	.adress {
		margin-top: 20px;
		margin-bottom: 24px;
		display: flex;
		align-items: center;
		input:first-of-type {
			margin-right: 12px;
		}
	}

	button[type="submit"] {
		outline: none;
		padding: 10px 13px;
		border-radius: 6px;
		border: 1px solid #dbdbdb;
		color: #fcfcfc;
		font-weight: 500;
		font-size: 16px;
		background-color: #1dd1a1;
	}
`;

export default ProfileEditPage;
