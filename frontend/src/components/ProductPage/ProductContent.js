import React from "react";
import styled from "styled-components";

function ProductContent({article}){
    return(
        <ProductSection>
            <h2 className="product-content__title">{article.title}</h2>
            <div className="product-content__categories">{article.category.name}</div>
            <div className="product-content__price">{article.price}원</div>
            <p className="product-content__description">
                {article.content}
            </p>
    <div className="product-content__counts">채팅 16 ∙ 관심 {article.interestCount}</div>
        </ProductSection>
    )
}

const ProductSection = styled.section`
    width:659px;
    padding:26px 0;
    border-bottom:1px solid #dbdbdb;
    .product-content__title{
        font-size:22px;
        font-weight:500;
        margin-bottom:4px;
    }

    .product-content__categories{
        margin-bottom:7px;
        font-size:13px;
        color:#868e96;

    }

    .product-content__price{
        margin-bottom:10px;
        font-weight:500;
        font-size:18px;
    }

    .product-content__description{
        margin-bottom:12px;
    }

    .product-content__counts{
        font-size:12px;
        color:#868e96;
    }
`;
export default ProductContent;