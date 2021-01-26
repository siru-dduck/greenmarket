import React, { useEffect, useState } from "react";
import { MainLayout } from "../../util/style/LayoutStyle";
import { FormLayout, ProductForm } from "../../util/style/FormStyle";
import { Button } from "../../util/style/CommonStyle";
import TopHeader from "../Header/TopHeader";
import { FaCamera } from "react-icons/fa";
import { IoIosCloseCircle } from "react-icons/io";
import axios from "axios";

function EditFormPage(props) {
	const [productArticle, setProductArticle] = useState({ isLoading: false });
	const [images, setImages] = useState([]);
	const [imageFiles, setImageFiles] = useState([]);
	const onSubmit = (e) => {
		e.preventDefault();
		const {
			title,
			content,
			price,
			category,
			status,
			address1,
			address2,
		} = e.currentTarget;

		if (
			title.value.trim().length <= 0 ||
			content.value.trim().length <= 0 ||
			Number.isNaN(Number(category.value)) ||
			price.value.trim().length <= 0 ||
			Number.isNaN(Number(price.value)) ||
			Number(price.value) < 0 ||
			Number(price.value) >= 100000000 ||
			(Number(status.value) !== 0 && Number(status.value) !== 1) ||
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
		formData.append("status", status.value);
		formData.append("categoryId", category.value);
		formData.append("address1", address1.value);
		formData.append("address2", address2.value);
		axios
			.put(`/api/products/${props.match.params.id}`, formData, config)
			.then(() => {
				props.history.push(`/products/${props.match.params.id}`);
			})
			.catch(() => {
				// TODO jwt 유효기간 만료 및 쿠키 만료등으로 유저인증이 안될경우 현재 작성한 페이지를 저장하고 로그인페이지로 이동
				alert("상품수정에 실패하였습니다.");
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

	useEffect(() => {
		axios
			.get(`/api/products/${props.match.params.id}`)
			.then((response) => {
				setProductArticle({ ...response.data.productArticle, isLoading: true });
				Promise.all(
					response.data.productArticle.productImages.map((e) => {
						return new Promise((resolve, reject) => {
							axios
								.get(e.fileUrl, {
									responseType: "blob",
								})
								.then((response) => {
									resolve(
										new File([response.data], "", { type: response.data.type })
									);
								});
						});
					})
				)
					.then((fileList) => {
						const imageUrlList = fileList.map((e) => {
							return URL.createObjectURL(e);
						});
						setImages(imageUrlList);
						setImageFiles(fileList);
					})
					.catch(() => {
						alert("상품정보를 불러오는데 실패했습니다.");
						props.history.push("/");
					});
			})
			.catch(() => {
				alert("상품정보를 불러오는데 실패했습니다.");
				props.history.push("/");
			});
		// eslint-disable-next-line
	}, []);

	return !productArticle.isLoading ? null : (
		<>
			<TopHeader {...props} />
			<MainLayout>
				<FormLayout>
					<ProductForm onSubmit={onSubmit}>
						<h2>상품수정</h2>
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
						<div className="form-paragraph status">
							<h3>거래상태</h3>
							<select defaultValue={productArticle.status} name="status">
								<option hidden value="-1">
									거래상태
								</option>
								<option value="0">거래중</option>
								<option value="1">거래완료</option>
							</select>
						</div>
						<div className="form-paragraph category">
							<h3>카테고리</h3>
							<select defaultValue={productArticle.category.id} name="category">
								<option hidden>카테고리</option>
								<option value="1">디지털/가전</option>
								<option value="2">가구/인테리어</option>
								<option value="3">유아동/유아도서</option>
								<option value="4">생활/가공식품</option>
								<option value="5">스포츠/레저</option>
								<option value="6">여성잡화</option>
								<option value="7">여성의류</option>
								<option value="8">남성패션/잡화</option>
								<option value="9">게임/취미</option>
								<option value="10">뷰티/미용</option>
								<option value="11">반려동물용품</option>
								<option value="12">도서티켓/음반</option>
								<option value="13">식물</option>
								<option value="14">기타 중고물품</option>
							</select>
						</div>
						<div className="form-paragraph title">
							<h3>글제목</h3>
							<input
								type="text"
								name="title"
								defaultValue={productArticle.title}
								placeholder="글제목"
							/>
						</div>
						<div className="form-paragraph price">
							<h3>가격</h3>
							<input
								type="number"
								name="price"
								defaultValue={productArticle.price}
								placeholder="가격(원)"
							/>
						</div>
						<div className="form-paragraph content">
							<h3>게시글 내용</h3>
							<textarea
								name="content"
								defaultValue={productArticle.content}
							></textarea>
						</div>
						<div className="form-paragraph region">
							<h3>지역</h3>
							<input
								type="text"
								className="region_filed"
								name="address1"
								defaultValue={productArticle.address1}
							/>
							<input
								type="text"
								className="region_filed"
								name="address2"
								defaultValue={productArticle.address2}
							/>
						</div>
						<Button type="submit">상품수정</Button>
					</ProductForm>
				</FormLayout>
			</MainLayout>
		</>
	);
}

export default EditFormPage;
