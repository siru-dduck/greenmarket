import React, { useContext } from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";
import Logo from "../commons/Logo";
import KakaoIconSvg from "../../assets/images/kakao_icon.svg";
import NaverIconSvg from "../../assets/images/naver_icon.svg";
import axios from "axios";
import UserContext from "../../util/User.context";

function LoginPage(props) {
	const {actions:{setUser}} =useContext(UserContext);

	const onSubmit = (e) => {
		e.preventDefault();
		const { email, password } = e.currentTarget;
		if (email.value.trim().length <= 0 || password.value.trim().length <= 0) {
			return alert("이메일과 비밀번호를 확인해주세요.");
		}
		axios
			.post("/api/user/login", { email: email.value, password: password.value })
			.then((response) => {
				if (response.data.loginSuccess) {
					localStorage.setItem("user", JSON.stringify(response.data.user));
					setUser(response.data.user);
					props.history.push("/");
				} else {
					alert(response.data.message);
				}
			})
			.catch((e) => {
				console.log(e);
				alert("로그인에 실패하였습니다. 다시 시도해주세요.");
			});
	};
	return (
		<MainLayout>
			<div className="sign-in-form">
				<header className="sign-in-form__header">
					<Logo size="42" />
				</header>
				<form onSubmit={onSubmit} name="signIn">
					<input
						className="sign-in-form__input sign-in-form__input--email"
						type="text"
						name="email"
						placeholder="이메일"
					/>
					<input
						className="sign-in-form__input sign-in-form__input--password"
						type="password"
						name="password"
						placeholder="비밀번호"
					/>
					<input
						className="sign-in-form__input sign-in-form__input--submit"
						type="submit"
						value="로그인"
					/>
				</form>
				<div className="sign-in-form__action">
					<a className="sign-in-form__action__link" href="/user/password">
						비밀번호 찾기
					</a>
					<a className="sign-in-form__action__link" href="/join">
						회원가입
					</a>
				</div>
				<div className="sign-in-form__sns">
					<h2 className="sign-in-form__sns__title">
						SNS계정으로 간편 로그인/회원가입
					</h2>
					<div className="sign-in-form__sns__list">
						<Link to="/user/login/kakao">
							<img
								className="sign-in-form__sns__list__icon"
								src={KakaoIconSvg}
								alt="카카오톡 아이콘"
							/>
						</Link>
						<Link to="/user/login/naver">
							<img
								className="sign-in-form__sns__list__icon"
								src={NaverIconSvg}
								alt="네이버 아이콘"
							/>
						</Link>
					</div>
				</div>
			</div>
		</MainLayout>
	);
}

const MainLayout = styled.main`
	display: flex;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: 100vh;

	.sign-in-form {
		width: 100%;
		max-width: 300px;
		border-bottom: 1px solid #dbdbdb;
	}

	.sign-in-form__header {
		display: flex;
		justify-content: center;
		margin-bottom: 30px;
	}

	.sign-in-form__input {
		display: block;
		border: 1px solid #dbdbdb;
		outline: none;
		width: 100%;
		height: 50px;
		line-height: 50px;
		padding: 0px 16px;
		font-size: 15px;
		color: #424242;
	}

	.sign-in-form__input--email {
		border-radius: 4px 4px 0 0;
	}

	.sign-in-form__input--password {
		border-radius: 0 0 4px 4px;
	}

	.sign-in-form__input--submit {
		background: #1dd1a1;
		color: #fcfcfc;
		margin: 32px 0;
		font-size: 18px;
		font-weight: 600;
		border-radius: 4px;
	}

	.sign-in-form__action {
		display: flex;
		color: #424242;
		justify-content: center;
		.sign-in-form__action__link {
			margin: 0 8px;
		}

		.sign-in-form__action__link:hover {
			text-decoration: underline;
		}
	}

	.sign-in-form__sns {
		margin: 30px 0 20px;
		.sign-in-form__sns__title {
			text-align: center;
			font-size: 12px;
			color: #757575;
			margin-bottom: 10px;
		}
		.sign-in-form__sns__list {
			display: flex;
			justify-content: center;
			.sign-in-form__sns__list__icon {
				width: 48px;
				margin: 0 12px;
			}
		}
	}
`;

export default LoginPage;
