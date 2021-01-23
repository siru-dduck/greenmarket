import React, { useState, useEffect } from "react";
import axios from "axios";
import styled from "styled-components";
import { MainLayout } from "../../util/style/LayoutStyle";
import ProductList from "../commons/ProductList";
import TopHeader from "../Header/TopHeader";
import qs from "qs";
import { Button } from "../../util/style/CommonStyle";

function SearchPage(props) {
	const [search, setSearch] = useState({ isLoading: false });
	const onClickMoreButton = async () => {
		try {
			const response = await axios.get(
				`/api/products?keyword=${search.keyword}&limit=20&offset=${search.lastArticleId}`
			);
			if (response.data.length > 0) {
				setSearch({
					isLoading: true,
					keyword: search.keyword,
					lastArticleId: response.data.lastArticleId,
					productArticles: [
						...search.productArticles,
						...response.data.productArticles,
					],
				});
			}
		} catch (error) {
			console.error(error);
		}
	};

	const extractQuery = () => {
		return qs.parse(props.location.search, {
			ignoreQueryPrefix: true,
		});
	};

	useEffect(() => {
		const fetchSearchKeyword = async () => {
			const query = extractQuery();
			if (!query.keyword) {
				setSearch({ isLoading: true, keyword: null, productArticles: [] });
			} else {
				try {
					const response = await axios.get(
						`/api/products?keyword=${query.keyword}&limit=20`
					);
					setSearch({
						isLoading: true,
						keyword: query.keyword,
						lastArticleId: response.data.lastArticleId,
						productArticles: response.data.productArticles,
					});
				} catch (error) {
					console.error(error);
					setSearch({
						isLoading: true,
						keyword: query.keyword,
						lastArticleId: null,
						productArticles: [],
					});
				}
			}
		};
		fetchSearchKeyword();
		// eslint-disable-next-line
	}, [props.location.search]);
	if (!search.isLoading) {
		return <></>;
	}
	return (
		<>
			<TopHeader {...props} />
			<MainLayout>
				<SearchSection>
					<h2 className="title">{search.keyword} 검색결과</h2>
					{search.productArticles.length > 0 ? (
						<ProductList productArticles={search.productArticles} />
					) : (
						<p className="not-found-message">검색결과를 찾을 수 없습니다.</p>
					)}
					<Button onClick={onClickMoreButton}>더보기</Button>
				</SearchSection>
			</MainLayout>
		</>
	);
}

const SearchSection = styled.section`
	max-width: 1024px;
	padding-bottom: 52px;
	margin: 0 auto;
	display: flex;
	flex-direction: column;
	align-items: center;
	.title {
		margin: 56px 0;
		font-size: 36px;
		font-family: "yg-jalnan";
		color: #3b3b3b;
		text-align: center;
	}
	.not-found-message {
		margin: 56px 0;
		font-size: 28px;
		font-family: "yg-jalnan";
		color: #5b5b5b;
		text-align: center;
	}
`;

export default SearchPage;
