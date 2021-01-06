import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { GlobalStyle } from "./util/style/GlobalStyle";
import Landing from "./components/LandingPage/LandingPage";
import LoginPage from "./components/LoginPage/LoginPage";
import ProductsPage from "./components/ProductPage/ProductsPage";
import ChatPage from "./components/ChatPage/ChatPage";
import NewFormPage from "./components/NewFormPage/NewFormPage";
import NotFoundPage from "./components/commons/NotFoundPage";
import SearchPage from "./components/SearchPage/SearchPage";
import JoinPage from "./components/JoinPage/JoinPage";
import Auth from "./hoc/auth";
import { UserProvider } from "./util/context/User.context";
import EditFormPage from "./components/EditFormPage/EditFormPage";

function App() {
	return (
		<>
			<GlobalStyle></GlobalStyle>
			<UserProvider>
				<BrowserRouter>
					<Switch>
						<Route path="/" exact component={Auth(Landing, true)} />
						<Route path="/home" exact component={Auth(Landing, true)} />
						<Route path="/login" exact component={Auth(LoginPage, false)} />
						<Route path="/products/new" exact component={Auth(NewFormPage)} />
						<Route
							path="/products/:id"
							exact
							component={Auth(ProductsPage, true)}
						/>
						<Route
							path="/products/:id/edit"
							exact
							component={Auth(EditFormPage)}
						/>
						<Route path="/chat" exact component={Auth(ChatPage)} />
						<Route path="/join" exact component={Auth(JoinPage, false)} />
						<Route path="/search" exact component={Auth(SearchPage, true)} />
						<Route component={NotFoundPage} />
					</Switch>
				</BrowserRouter>
			</UserProvider>
		</>
	);
}

export default App;
