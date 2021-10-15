const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
	app.use(
		"/api/user",
		createProxyMiddleware({
			target: "http://localhost:8130",
			changeOrigin: true,
		})
	);
	app.use(
		"/api/products",
		createProxyMiddleware({
			target: "http://localhost:8110",
			changeOrigin: true,
		})
	);
	app.use(
		"/ws/chat",
		createProxyMiddleware({
			target: "ws://localhost:8140",
			changeOrigin: true,
			ws: true,
		})
	);
	app.use(
		"/api/chat",
		createProxyMiddleware({
			target: "http://localhost:8140",
			changeOrigin: true,
		})
	);
	app.use(
		"/resources/images/product",
		createProxyMiddleware({
			target: "http://localhost:8110",
			changeOrigin: true,
		})
	);
	app.use(
		"/resources/images/profile",
		createProxyMiddleware({
			target: "http://localhost:8130",
			changeOrigin: true,
		})
	);
};
