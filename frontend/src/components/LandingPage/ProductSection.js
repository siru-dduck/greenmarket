import React, { useState, useContext, useEffect } from "react";
import ProductList from "../commons/ProductList";
import styled from "styled-components";
import axios from "axios";
import UserContext from "../../util/User.context";

function ProductSection(props){
    const { state: { user } } = useContext(UserContext);
    const [ productAticles, setProductArticles ] = useState([]);
    useEffect(()=>{
        const fetchProductArticle = async () => {
            const REQUEST_PRODUCT_URL = user? 
                `/api/products?address1=${user.address1}`:
                `/api/products?order=interest_count`;
            try{
                const response = await axios.get(REQUEST_PRODUCT_URL);
                setProductArticles(...productAticles, response.data.productArticles);
            } catch(error) {
                console.error(error);
            }
        }
        fetchProductArticle();
        // eslint-disable-next-line
    }, []);
    return(
        <ProductWrap>
            <h1 className="propduct-wrap__title">{user?"우리동네":"인기"}매물</h1>
            <ProductList productArticles={productAticles} />
        </ProductWrap>
    );
}

const ProductWrap = styled.section`
    max-width:1024px;
    margin:0 auto;
    display:flex;
    flex-direction:column;
    .propduct-wrap__title{
        margin:56px 0;
        font-size:36px;
        font-family:"yg-jalnan";
        color:#3b3b3b;
        text-align:center;
    }

`;



export default ProductSection;