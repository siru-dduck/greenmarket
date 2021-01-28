import "@babel/polyfill";
import axios from "axios";

const SERVICE_PRODUCT_HOST =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_HOST || "localhost";
const SERVICE_PRODUCT_PORT =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_PORT || "8080";

export const getProductsBy = async (articleIdList) => {
	if (!articleIdList instanceof Array || articleIdList.length <= 0) {
		return [];
	}
	const response = await axios.get(
		`http://${SERVICE_PRODUCT_HOST}:${SERVICE_PRODUCT_PORT}/api/products?articleIds=${articleIdList}`
	);
	return response.data.productArticles;
};

export const getProductBy = async (articleId) => {
	if (!articleId) {
		return null;
	}
	const response = await axios.get(
		`http://${SERVICE_PRODUCT_HOST}:${SERVICE_PRODUCT_PORT}/api/products/${articleId}`
	);
	return response.data.productArticle;
};
