import React from "react";
import styled from "styled-components";

function ProductDescription({ article }) {
	return (
		<ProductSection>
			<h2 className="product-content__title">
				{article.status === 1 ? (
					<span className="product-content__tag">거래완료</span>
				) : null}
				{article.title}
			</h2>
			<div className="product-content__categories">{article.category.name}</div>
			<div className="product-content__price">
				<span className="product-content__price-value">{article.price}원</span>
				{article.price === 0 ? (
					<span className="product-content__tag">무료나눔</span>
				) : null}
			</div>
			<p className="product-content__description">{article.content}</p>
			<div className="product-content__counts">
				관심 {article.interestCount}
			</div>
		</ProductSection>
	);
}

const ProductSection = styled.section`
	width: 659px;
	padding: 26px 0;
	border-bottom: 1px solid #dbdbdb;
	.product-content__title {
		font-size: 22px;
		font-weight: 500;
		margin-bottom: 4px;
	}

	.product-content__tag {
		color: #fff;
		background-color: #1dd1a1;
		border-radius: 8px;
		padding: 2px 8px;
		margin-right: 10px;
	}

	.product-content__categories {
		margin-bottom: 7px;
		font-size: 13px;
		color: #868e96;
	}

	.product-content__price {
		margin-bottom: 10px;
		font-weight: 500;
		font-size: 18px;
		.product-content__price-value {
			margin-right: 8px;
		}
	}

	.product-content__description {
		margin-bottom: 12px;
	}

	.product-content__counts {
		font-size: 12px;
		color: #868e96;
	}
`;
export default ProductDescription;
