import React, { useState, useContext, useEffect } from "react";
import ProductList from "../commons/ProductList";
import styled from "styled-components";
import axios from "axios";
import UserContext from "../../util/context/User.context";
import { Button } from "../../util/style/CommonStyle";

function ProductSection() {
	const {
		state: { user },
	} = useContext(UserContext);
	const [product, setProduct] = useState({ isLoading: false });
	const REQUEST_PRODUCT_URL = user
		? `/api/products?address1=${user.address1}&address2=${user.address2}&limit=20`
		: `/api/products?order=-interest_count&limit=20`;

	const onClickMoreButton = async () => {
		try {
			const response = await axios.get(
				REQUEST_PRODUCT_URL + `&offset=${product.lastArticleId}`
			);
			if (response.data.length > 0) {
				setProduct({
					isLoading: true,
					lastArticleId: response.data.lastArticleId,
					productArticles: [
						...product.productArticles,
						...response.data.productArticles,
					],
				});
			}
		} catch (error) {
			console.error(error);
		}
	};

	useEffect(() => {
		const fetchProductArticle = async () => {
			try {
				const response = await axios.get(REQUEST_PRODUCT_URL);
				setProduct({ isLoading: true, ...response.data });
			} catch (error) {
				console.error(error);
			}
		};
		fetchProductArticle();
		// eslint-disable-next-line
	}, []);
	if (!product.isLoading) {
		return <></>;
	}
	return (
		<ProductWrap>
			<h1 className="propduct-wrap__title">{user ? "우리동네" : "인기"}매물</h1>
			<ProductList productArticles={product.productArticles} />
			{user ? <Button onClick={onClickMoreButton}>더보기</Button> : null}
		</ProductWrap>
	);
}

const ProductWrap = styled.section`
	max-width: 1024px;
	margin: 0 auto;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding-bottom: 52px;
	.propduct-wrap__title {
		margin: 56px 0;
		font-size: 36px;
		font-family: "yg-jalnan";
		color: #3b3b3b;
		text-align: center;
	}
`;

export default ProductSection;
