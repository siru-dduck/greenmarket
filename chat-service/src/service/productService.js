import "@babel/polyfill";
import axios from "axios";

const SERVICE_PRODUCT_HOST =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_HOST;
const SERVICE_PRODUCT_PORT =
	process.env.GREENMARKET_PRODUCT_APP_SERVICE_PORT;

export const getProductsBy = async (articleIdList) => {
	if (!articleIdList instanceof Array || articleIdList.length <= 0) {
		return [];
	}
	const response = await axios.get(
		`http://${SERVICE_PRODUCT_HOST}:${SERVICE_PRODUCT_PORT}/api/products?articleIds=${articleIdList}`
	);
	return response.data;
};

export const getProductBy = async (productId) => {
	if (!productId) {
		return null;
	}
	const response = await axios.get(
		`http://${SERVICE_PRODUCT_HOST}:${SERVICE_PRODUCT_PORT}/products/${productId}`
	);
	return response.data;
};