import "@babel/polyfill";
import axios from "axios";

const SERVICE_USER_HOST =
	process.env.GREENMARKET_USER_APP_SERVICE_HOST || "localhost";
const SERVICE_USER_PORT =
	process.env.GREENMARKET_USER_APP_SERVICE_PORT || "5000";

export const getUsersBy = async (userIdList) => {
	if (!userIdList instanceof Array || userIdList.length <= 0) {
		return [];
	}
	const response = await axios.get(
		`http://${SERVICE_USER_HOST}:${SERVICE_USER_PORT}/api/user?user_ids=${userIdList}`
	);
	return response.data.users;
};
