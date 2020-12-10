import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { GlobalStyle } from "./util/GlobalStyle";
import Landing from "./components/LandingPage/LandingPage";
import LoginPage from "./components/LoginPage/LoginPage";
import ProductsPage from "./components/ProductPage/ProductsPage";
import ChatPage from "./components/ChatPage/ChatPage";
import JoinPage from "./components/JoinPage/JoinPage";
import Auth from "./hoc/auth";
import { UserProvider } from "./util/User.context";
import FormPage from "./components/FormPage/FormPage";
import NotFoundPage from "./components/commons/NotFoundPage";
import SearchPage from "./components/SearchPage/SearchPage";

function App() {
	return (
		<>
			<GlobalStyle></GlobalStyle>
			<UserProvider>
				<BrowserRouter>
					<Switch>
						<Route path="/" exact component={Auth(Landing,true)} />
						<Route path="/home"  exact component={Auth(Landing,true)} />
						<Route path="/login" exact component={Auth(LoginPage,false)} />
						<Route path="/product/:id" exact component={Auth(ProductsPage,true)} />
						<Route path="/chat" exact component={Auth(ChatPage)} />
						<Route path="/join" exact component={Auth(JoinPage, false)} />
						<Route path="/form" exact component={Auth(FormPage)}/>
						<Route path="/search" exact component={Auth(SearchPage, true)}/>
						<Route component={NotFoundPage}/>
					</Switch>
				</BrowserRouter>
			</UserProvider>
		</>
	);
}

export default App;
