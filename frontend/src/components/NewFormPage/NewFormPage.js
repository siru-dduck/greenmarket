import React, { useContext, useState } from "react";
import { MainLayout } from "../../util/style/LayoutStyle";
import { FormLayout, ProductForm } from "../../util/style/FormStyle";
import { Button } from "../../util/style/CommonStyle";
import TopHeader from "../Header/TopHeader";
import { FaCamera } from "react-icons/fa";
import { IoIosCloseCircle } from "react-icons/io";
import axios from "axios";
import UserContext from "../../util/context/User.context";

function NewFormPage(props) {
	const {
		state: { user },
	} = useContext(UserContext);
	const [images, setImages] = useState([]);
	const [imageFiles, setImageFiles] = useState([]);
	const onSubmit = (e) => {
		e.preventDefault();
		const {
			title,
			content,
			price,
			category,
			address1,
			address2,
		} = e.currentTarget;

		if (
			title.value.trim().length <= 0 ||
			content.value.trim().length <= 0 ||
			Number(category.value) === NaN ||
			price.value.trim().length <= 0 ||
			Number(price.value) === NaN ||
			Number(price.value) <= 0 ||
			Number(price.value) >= 100000000 ||
			address1.value.trim().length <= 0 ||
			address2.value.trim().length <= 0 ||
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
			formData.append("files", file);
		});
		formData.append("title", title.value);
		formData.append("price", price.value);
		formData.append("content", content.value);
		formData.append("categoryId", category.value);
		formData.append("address1", address1.value);
		formData.append("address2", address2.value);
		axios
			.post("/api/products", formData, config)
			.then((response) => {
				props.history.push(`/products/${response.data.articleId}`);
			})
			.catch(() => {
				// TODO jwt 유효기간 만료 및 쿠키 만료등으로 유저인증이 안될경우 현재 작성한 페이지를 저장하고 로그인페이지로 이동
				alert("상품등록에 실패하였습니다.");
			});
	};

	const onClickImageUplaodButton = (e) => {
		e.preventDefault();
		if (images.length >= 12) {
			alert("사진은 최대 12개까지만 등록할 수 있습니다.");
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

	const onClickImageDeleteBtn = (e) => {
		e.preventDefault();
		let target = e.target;
		while (target.tagName !== "BUTTON" && target !== e.currentTarget) {
			target = target.parentElement;
		}
		if (target.tagName === "BUTTON") {
			const delteIndex = Array.from(e.currentTarget.children).indexOf(
				target.parentElement
			);
			const tempImageFiles = new Array(...imageFiles);
			const tempImages = new Array(...images);
			tempImageFiles.splice(delteIndex, 1);
			tempImages.splice(delteIndex, 1);
			setImageFiles(tempImageFiles);
			setImages(tempImages);
		}
	};
	return (
		<>
			<TopHeader {...props} />
			<MainLayout>
				<FormLayout>
					<ProductForm onSubmit={onSubmit}>
						<h2>상품등록</h2>
						<div className="image-upload">
							<button
								className="image-upload-btn"
								onClick={onClickImageUplaodButton}
							>
								<FaCamera size="24" color="#999" />
								<span className="btn-text">업로드</span>
								<span className="btn-text">{images.length}/12</span>
							</button>
							<ul className="image-list" onClick={onClickImageDeleteBtn}>
								{images.map((v, i) => (
									<li key={i}>
										<button className="image-delete-btn">
											<IoIosCloseCircle size="22" color="rgba(33,33,33,0.6)" />
										</button>
										<img src={v} alt="업로드 이미지" />
									</li>
								))}
							</ul>
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
							<input type="number" name="price" placeholder="가격(원)" />
						</div>
						<div className="form-paragraph content">
							<h3>게시글 내용</h3>
							<textarea name="content"></textarea>
						</div>
						<div className="form-paragraph region">
							<h3>지역</h3>
							<input
								type="text"
								className="region_filed"
								name="address1"
								defaultValue={user.address1}
							/>
							<input
								type="text"
								className="region_filed"
								name="address2"
								defaultValue={user.address2}
							/>
						</div>
						<Button type="submit">상품등록</Button>
					</ProductForm>
				</FormLayout>
			</MainLayout>
		</>
	);
}

export default NewFormPage;
