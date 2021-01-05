import React, { useContext, useState } from "react";
import styled from "styled-components";
import { MainLayout } from "../../util/LayoutStyle";
import TopHeader from "../Header/TopHeader";
import { FaCamera } from "react-icons/fa";
import axios from "axios";
import UserContext from "../../util/User.context";

function FormPage(props) {
	const {
		state: { user },
	} = useContext(UserContext);
	const [images, setImages] = useState([]);
	const [imageFiles, setImageFiles] = useState([]);

	const onSubmit = (e) => {
		e.preventDefault();
		const { title, content, price, category } = e.currentTarget;

		if (
			title.value.trim().length <= 0 ||
			content.value.trim().length <= 0 ||
			!Number(price.value) ||
			!Number(category.value) ||
			Number(price.value) <= 0 ||
			Number(price.value) >= 100000000 ||
			imageFiles.length <= 0
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
		imageFiles.forEach((file) => {
			formData.append("file", file);
		});
		formData.append("title", title.value);
		formData.append("price", price.value);
		formData.append("content", content.value);
		formData.append("categoryId", category.value);
		axios
			.post("/api/products", formData, config)
			.then((response) => {
				console.log(response);
				if (response.data.isSuccess) {
					props.history.push(`/product/${response.data.articleId}`);
				} else {
					alert("상품등록에 실패하였습니다.");
				}
			})
			.catch(() => {
				// TODO jwt 유효기간 만료 및 쿠키 만료등으로 유저인증이 안될경우 현재 작성한 페이지를 저장하고 로그인페이지로 이동
				alert("상품등록에 실패하였습니다.");
			});
	};

	const onClickImageUplaodButton = (e) => {
		e.preventDefault();
		if (images.length > 10) {
			alert("사진은 최대 10개까지만 등록할 수 있습니다.");
			return;
		}
		const input = document.createElement("input");
		input.setAttribute("type", "file");
		input.click();
		input.onchange = (e) => {
			const reader = new FileReader();
			reader.onload = (e) => {
				setImages([...images, e.target.result]);
			};
			setImageFiles([...imageFiles, e.target.files[0]]);
			reader.readAsDataURL(e.target.files[0]);
		};
	};
	return (
		<>
			<TopHeader {...props} />
			<MainLayout>
				<FormLayout>
					<ProductForm onSubmit={onSubmit}>
						<h2>상품등록</h2>
						<div className="image-upload">
							<button onClick={onClickImageUplaodButton}>
								<FaCamera size="24" color="#999" />
								<span className="btn-text">업로드</span>
								<span className="btn-text">{images.length}/10</span>
							</button>
							<div className="image-container">
								{images.map((v, i) => (
									<img key={i} src={v} alt="업로드 이미지" />
								))}
							</div>
						</div>
						<div className="form-paragraph category">
							<h3>카테고리</h3>
							<select name="category">
								<option hidden>카테고리</option>
								<option value="1">디지털/가전</option>
								<option value="2">유아동/유아도서</option>
								<option value="3">생활/가공식품</option>
								<option value="4">스포츠/레저</option>
								<option value="5">여성잡화</option>
								<option value="6">여성의류</option>
								<option value="7">남성패션/잡화</option>
								<option value="8">게임/취미</option>
								<option value="9">뷰티/내용</option>
								<option value="10">반려동물용품</option>
								<option value="11">도서티켓/음반</option>
								<option value="12">기타중고용품</option>
							</select>
						</div>
						<div className="form-paragraph title">
							<h3>글제목</h3>
							<input type="text" name="title" placeholder="글제목" />
						</div>
						<div className="form-paragraph price">
							<h3>가격</h3>
							<input type="text" name="price" placeholder="가격(원)" />
						</div>
						<div className="form-paragraph content">
							<h3>게시글 내용</h3>
							<textarea name="content"></textarea>
						</div>
						<div className="form-paragraph region">
							<h3>지역</h3>
							<div className="region_filed">{user.address1}</div>
							<div className="region_filed">{user.address2}</div>
						</div>
						<button type="submit">상품등록</button>
					</ProductForm>
				</FormLayout>
			</MainLayout>
		</>
	);
}

const FormLayout = styled.div`
	width: 100%;
	min-height: calc(100vh - 69px);
	display: flex;
	align-items: flex-start;
	justify-content: center;
`;

const ProductForm = styled.form`
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

	.image-upload {
		width: 520px;
		margin-bottom: 20px;
		button {
			width: 80px;
			height: 80px;
			display: flex;
			justify-content: center;
			align-items: center;
			flex-direction: column;
			border: 1px solid #666;
			background-color: #fcfcfc;
			border-radius: 6px;
			outline: none;
			font-family: inherit;
			.btn-text {
				&:first-of-type {
					margin-top: 5px;
				}
				font-weight: 500;
				color: #444;
				font-size: 13px;
			}
		}

		.image-container {
			margin-top: 16px;
			display: flex;
			img {
				max-width: 30%;
				margin-right: 3%;
				height: auto;
			}
		}
	}

	.form-paragraph {
		display: flex;
		align-items: center;
		margin-bottom: 16px;
		h3 {
			margin-right: 16px;
			width: 140px;
		}
		select {
			position: relative;
			appearance: none;
			padding: 10px 13px;
			font-size: 16px;
			font-weight: 500;
			font-family: inherit;
			color: #333;
			outline: none;
			border-radius: 6px;
			border: 1px solid #dbdb;
			option {
				color: #333;
			}
		}
		input {
			padding: 8px 13px;
			font-size: 15px;
			font-weight: 500;
			font-family: inherit;
			color: #333;
			outline: none;
			border-radius: 6px;
			border: 1px solid #dbdb;
		}
		textarea {
			padding: 8px 13px;
			resize: none;
			width: 300px;
			height: 120px;
			border-radius: 6px;
			border: 1px solid #dbdbdb;
			font-size: 14px;
			font-weight: 500;
			font-family: inherit;
			color: #333;
			outline: none;
		}
		.region_filed {
			padding: 8px 13px;
			font-size: 15px;
			font-weight: 500;
			font-family: inherit;
			color: #333;
			outline: none;
			border-radius: 6px;
			border: 1px solid #dbdb;
			&:first-of-type {
				color: #333;
				margin-right: 12px;
			}
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

export default FormPage;
