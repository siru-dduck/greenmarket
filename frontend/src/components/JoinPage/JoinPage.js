import React from "react";
import styled from "styled-components";
import Logo from "../commons/Logo";
import axios from "axios";

function JoinPage(props) {
	const onSubmit = (e) => {
		e.preventDefault();
		console.dir(e.currentTarget);
		const {
			email,
			password,
			checkPassword,
			address1,
			address2,
			nickname,
		} = e.currentTarget;
		if (password.value !== checkPassword.value) {
			alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		} else if (
			email.value.trim().length <= 0 ||
			password.value.trim().length <= 0 ||
			address1.value.trim().length <= 0 ||
			address2.value.trim().length <= 0 ||
			nickname.value.trim().length <= 0
		) {
			alert("입력되지않은 값이 있습니다. 양식을 다시 확인해주세요.");
		} else {
			axios
				.post("/api/user/join", {
					email: email.value,
					password: password.value,
					address1: address1.value,
					address2: address2.value,
					nickname: nickname.value,
				})
				.then((response) => {
					if (response.data.joinSuccess) {
						alert("회원가입을 축하드립니다.");
						props.history.push("/login");
					} else {
						alert(response.data.message);
					}
				})
				.catch((error) => {
					console.log(error);
					alert("회원가입에 실패하였습니다.");
				});
		}
	};

	const checkEmail = (email) => {
		// eslint-disable-next-line
		const reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
		if(reg_email.test(email)){
			return true;
		} else {
			return false;
		}
	}
	
	const onChangeEmail = (e) => { 
		const emailInput = e.currentTarget;
		
		if(emailInput.value.length <=0){
			emailInput.parentElement.classList.remove("sign-up-form__paragraph--valid");
			emailInput.parentElement.classList.remove("sign-up-form__paragraph--invalid");
		}
		else if(checkEmail(emailInput.value)){
			axios.get("/api/user/valid",{params:{email:emailInput.value}})
			.then((response)=>{
				if(response.data.isExistUser){
					emailInput.parentElement.classList.add("sign-up-form__paragraph--invalid");
					emailInput.parentElement.classList.remove("sign-up-form__paragraph--valid");
					emailInput.parentElement.children[1].innerText="사용할 수 없는 이메일입니다.";
				}else{
					emailInput.parentElement.classList.add("sign-up-form__paragraph--valid");
					emailInput.parentElement.classList.remove("sign-up-form__paragraph--invalid");
					emailInput.parentElement.children[1].innerText="사용할 수 있는 이메일입니다.";
				}
			})
		} else {
			emailInput.parentElement.classList.remove("sign-up-form__paragraph--valid");
			emailInput.parentElement.classList.add("sign-up-form__paragraph--invalid");
			emailInput.parentElement.children[1].innerText="사용할 수 없는 이메일입니다.";
		}
	}

	const onChangePassword = (e) =>{
		const { password:passwordInput, checkPassword:passwordCheckInput} = e.currentTarget.form;
		
		if(passwordInput.value.length <= 0 || passwordCheckInput.value.length <=0){
			passwordCheckInput.parentElement.classList.remove("sign-up-form__paragraph--invalid");
			passwordCheckInput.parentElement.classList.remove("sign-up-form__paragraph--valid");
			passwordCheckInput.parentElement.children[1].innerText = "패스워드가 일치하지 않습니다."
		} else if(passwordInput.value === passwordCheckInput.value){
			passwordCheckInput.parentElement.classList.add("sign-up-form__paragraph--valid");
			passwordCheckInput.parentElement.classList.remove("sign-up-form__paragraph--invalid");
			passwordCheckInput.parentElement.children[1].innerText = "패스워드가 일치합니다."
		} else{
			passwordCheckInput.parentElement.classList.add("sign-up-form__paragraph--invalid");
			passwordCheckInput.parentElement.classList.remove("sign-up-form__paragraph--valid");
			passwordCheckInput.parentElement.children[1].innerText = "패스워드가 일치하지 않습니다."
		}
	}

	return (
		<MainLayout>
			<div className="sign-up-form">
				<header className="sign-up-form__header">
					<Logo size="42" />
				</header>
				<form name="signUp" onSubmit={onSubmit}>
					<div className="sign-up-form__paragraph">
						<input
							className="sign-up-form__input"
							name="email"
							type="text"
							placeholder="이메일"
							onChange={onChangeEmail}
						/>
						<div className="sign-up-form__check-message">
							
						</div>
					</div>
					<div className="sign-up-form__paragraph">
						<input
							className="sign-up-form__input"
							name="password"
							type="password"
							placeholder="비밀번호"
							onChange={onChangePassword}
						/>
					</div>
					<div className="sign-up-form__paragraph">
						<input
							className="sign-up-form__input"
							name="checkPassword"
							type="password"
							placeholder="비밀번호 확인"
							onChange={onChangePassword}
						/>
						<div className="sign-up-form__check-message">
							
						</div>
					</div>
					<div className="sign-up-form__paragraph">
						<input
							className="sign-up-form__input"
							name="address1"
							type="text"
							placeholder="주소(시/도/광역시/특별시)"
						/>
					</div>
					<div className="sign-up-form__paragraph">
						<input
							className="sign-up-form__input"
							name="address2"
							type="text"
							placeholder="주소(구/군)"
						/>
					</div>
					<div className="sign-up-form__paragraph">
						<input
							className="sign-up-form__input"
							name="nickname"
							type="text"
							placeholder="닉네임"
						/>
					</div>
					<input
						className="sign-up-form__input sign-up-form__input--submit"
						type="submit"
						value="회원가입"
					/>
				</form>
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

	.sign-up-form {
		width: 100%;
		max-width: 300px;
		border-bottom: 1px solid #dbdbdb;
	}

	.sign-up-form__header {
		display: flex;
		justify-content: center;
		margin-bottom: 30px;
	}
	.sign-up-form__paragraph {
		&:not(:last-child) {
			margin-bottom: 12px;
		}
		.sign-up-form__check-message {
			display:none;
		}
	}

	.sign-up-form__paragraph--valid {
		input {
			box-shadow: 0px 0px 1px 1.5px rgba(46, 187, 75, 0.5);
			border: 1px solid #2ebb4b;
		}
		.sign-up-form__check-message {
			font-size: 12px;
			margin-top: 4px;
			color: #2ebb4b;
			display:block;
		}
	}

	.sign-up-form__paragraph--invalid {
		input {
			box-shadow: 0px 0px 1px 1.5px rgba(216, 83, 79, 0.5);
			border: 1px solid #d8534f;
		}
		.sign-up-form__check-message {
			display:block;
			font-size: 12px;
			margin-top: 4px;
			color: #d8534f;
		}
	}

	.sign-up-form__input {
		display: block;
		border: 1px solid #dbdbdb;
		outline: none;
		width: 100%;
		height: 50px;
		line-height: 50px;
		padding: 0px 16px;
		font-size: 15px;
		border-radius: 5px;
		color: #424242;
	}

	.sign-up-form__input--submit {
		background: #1dd1a1;
		color: #fcfcfc;
		margin: 32px 0;
		font-size: 18px;
		font-weight: 600;
		border-radius: 4px;
	}
`;

export default JoinPage;
