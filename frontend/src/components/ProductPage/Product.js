import React, { useEffect, useState } from "react";
import styled from "styled-components";
import ImageSlider from "./ImageSlider";
import { SectionLayout } from "../../util/style/LayoutStyle";
import ProductDescription from "./ProductContent";
import UserProfile from "./UserProfile";
import axios from "axios";
import NotFoundPage from "../commons/NotFoundPage";

function Product(props) {
	const [product, setProduct] = useState({ isLoading: false });

	useEffect(() => {
		const { id } = props.match.params;
		axios
			.get(`/api/products/${id}`)
			.then((response) => {
				setProduct({ ...response.data, isLoading: true });
			})
			.catch((error) => {
				console.log(error);
			});
	}, [props.match.params]);

	if (!product.isLoading) {
		return <p>로딩중 ...</p>;
	}

	return (
		<SectionLayout>
			{!product.productArticle ? (
				<NotFoundPage isSetTopMargin={true} />
			) : (
				<ProductArticle>
					<ImageSlider images={product.productImages} />
					<UserProfile
						{...props}
						articleId={product.productArticle.id}
						isCheckedInterest={product.isCheckedInterest}
						interestCount={product.productArticle.interestCount}
						chatRoomId={product.chatRoomId}
						user={product.productArticle.user}
					/>
					<ProductDescription article={product.productArticle} />
				</ProductArticle>
			)}
		</SectionLayout>
	);
}

const ProductArticle = styled.article`
	padding-top: 32px;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: flex-start;
`;

export default Product;
