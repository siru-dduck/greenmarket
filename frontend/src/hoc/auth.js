import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import UserContext from "../util/context/User.context";

export default function (SpecificComponent, option = null) {
	function AuthenticationCheck(props) {
		const {
			actions: { setUser },
		} = useContext(UserContext);
		const [isLoading, setIsLoading] = useState(false);

		useEffect(() => {
			axios
				.get("/api/user/auth")
				.then((response) => {
					if (!response.data.isAuth) {
						localStorage.removeItem("user");
						setUser(null);
						if (option === null) {
							if (props.location.pathname !== "/login") {
								alert("로그인 후 이용하시길 바랍니다.");
								props.history.push("/login");
							}
						}
						setIsLoading(true);
					} else {
						if (option === false) {
							props.history.push("/");
						} else {
							setIsLoading(true);
						}
					}
				})
				.catch(() => {
					console.log("인증에러");
					localStorage.removeItem("user");
					setUser(null);
					if (option === null) {
						if (props.location.pathname !== "/login") {
							alert("로그인 후 이용하시길 바랍니다.");
							props.history.push("/login");
						}
					}
					setIsLoading(true);
				});
			// eslint-disable-next-line
		}, []);
		return isLoading ? <SpecificComponent {...props} /> : <></>;
	}

	return AuthenticationCheck;
}
