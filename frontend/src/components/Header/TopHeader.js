import React, { useContext } from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";
import Logo from "../commons/Logo";
import { BiSearchAlt2 } from "react-icons/bi";
import { FaUserCircle } from "react-icons/fa";
import UserContext from "../../util/User.context";
import axios from "axios";

function TopHeader(props) {
	const {
		state: { user },
		actions: { setUser },
	} = useContext(UserContext);
	const onClickLogoutButton = () => {
		axios
			.get("/api/user/logout")
			.then((response) => {
				if (response.data.logoutSuccess) {
					setUser(null);
					localStorage.removeItem("user");
				}
				props.history.push("/");
			})
			.catch((error) => {
				console.dir(error);
				if (error.response.status === 400) {
					setUser(null);
					localStorage.removeItem("user");
				} else {
					alert("로그아웃에 실패했습니다.");
				}
			});
	};

	const onSubmitSearch = (e) => {
		e.preventDefault();
		props.history.push(`/search?keyword=${e.currentTarget.keyword.value}`);
	};

	return (
		<Header>
			<nav>
				<Logo />
			</nav>
			<SearchForm onSubmit={onSubmitSearch}>
				<input
					name="keyword"
					type="text"
					placeholder="상품을 검색해보세요."
					size="24"
					maxLength="24"
				/>
				<button>
					<BiSearchAlt2 size="26" color="#1dd1a1" />
				</button>
			</SearchForm>
			<Menu>
				<ul>
					{user ? (
						<>
							<li>
								<FaUserCircle size="24" color="#bdc3c7" />
								<MenuText className="margin-left">{user.nickname}님</MenuText>
							</li>
							<li>
								<MenuLink to="/chat">채팅</MenuLink>
							</li>
							<li>
								<MenuLink to="/form">상품등록</MenuLink>
							</li>
							<li>
								<MenuText onClick={onClickLogoutButton}>로그아웃</MenuText>
							</li>
						</>
					) : (
						<>
							<li>
								<MenuLink to="/login">로그인</MenuLink>
							</li>
							<li>
								<MenuLink to="/join">회원가입</MenuLink>
							</li>
						</>
					)}
				</ul>
			</Menu>
		</Header>
	);
}

const Header = styled.header`
	position: fixed;
	top: 0;
	left: 0;
	width: 100vw;
	display: flex;
	justify-content: space-between;
	padding: 12px 60px;
	height: 69px;
	align-items: center;
	border-bottom: 1px solid #dbdbdb;
	background: white;
	z-index: 9999;
	nav {
		display: flex;
		justify-content: center;
		align-items: center;
		flex-grow: 1;
	}
`;

const SearchForm = styled.form`
	display: flex;
	align-items: center;
	border: 1px solid #dbdbdb;
	border-radius: 12px;
	padding: 8px 14px;
	margin: 0 20px;
	justify-content: space-between;
	flex-grow: 0;
	width: 520px;
	min-width: 290px;
	input {
		border: none;
		font-size: 16px;
		width: 400px;
		min-width: 230px;
		outline: none;
	}
	button {
		background: none;
		border: none;
		outline: none;
	}
	svg {
		min-width: 26px;
	}
`;

const Menu = styled.nav`
	color: #757575;
	font-weight: 800;
	flex-grow: 1;
	display: flex;
	justify-content: center;
	ul {
		display: flex;
		li {
			position: relative;
			margin: 0 10px;
			font-weight: 500;
			display: flex;
			align-items: center;
			&:first-child {
				margin-left: 0px;
			}
			&:after {
				content: "";
				position: absolute;
				left: 100%;
				bottom: 0px;
				margin-left: 12px;
				height: 21px;
				border-right: 1px solid #757575;
			}
			&:last-child:after {
				content: none;
			}
		}
	}
`;

const MenuLink = styled(Link)`
	white-space: nowrap;
	text-decoration: none;
	color: #757575;
	position: relative;
	&:hover {
		color: #1dd1a1;
	}
`;

const MenuText = styled.span`
	white-space: nowrap;
	text-decoration: none;
	color: #757575;
	position: relative;
	cursor: pointer;
	&.margin-left {
		margin-left: 6px;
	}
	&:hover {
		color: #1dd1a1;
	}
`;
export default TopHeader;
