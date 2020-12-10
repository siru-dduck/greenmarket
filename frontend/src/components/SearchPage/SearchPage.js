import React, { useState, useEffect } from 'react'
import axios from 'axios';
import styled from 'styled-components';
import { MainLayout } from '../../util/LayoutStyle';
import ProductList from '../commons/ProductList';
import TopHeader from "../Header/TopHeader";
import qs from "qs";

function SearchPage(props) {
    const [ search, setSearch ] = useState({ isLoading: false }); 
    useEffect(() => {
        const fetchSearchKeyword = async () => {
            const query = qs.parse(props.location.search, {
				ignoreQueryPrefix: true		
            });
            if(!query.keyword){
                setSearch({isLoading: true, keyword: null,productArticles: []});
            } else {
                try{
                    const response = await axios.get(`/api/products?keyword=${query.keyword}`);
                    setSearch({isLoading: true, keyword: query.keyword, productArticles: response.data.productArticles});
                } catch(error) {
                    console.error(error);
                    setSearch({isLoading: true, keyword: query.keyword, productArticles: []});
                }
            } 
        }
        fetchSearchKeyword();
        // eslint-disable-next-line
    }, [props.location.search])
    return (
        <>
            <TopHeader {...props} />
            <MainLayout>
                <SearchSection>
                    <h2 className="title">{search.keyword} 검색결과</h2>
                    {
                        search.isLoading && search.productArticles.length > 0 ? 
                        <ProductList productArticles={search.productArticles}/>:
                        <p className="not-found-message">검색결과를 찾을 수 없습니다.</p>
                    }
                </SearchSection>
            </MainLayout>
        </>
    )
}

const SearchSection = styled.section`
    max-width:1024px;
    margin:0 auto;
    display:flex;
    flex-direction:column;
    .title{
        margin:56px 0;
        font-size:36px;
        font-family:"yg-jalnan";
        color:#3b3b3b;
        text-align:center;
    }
    .not-found-message{
        margin:56px 0;
        font-size:28px;
        font-family:"yg-jalnan";
        color:#5b5b5b;
        text-align:center;
    }
`;

export default SearchPage
