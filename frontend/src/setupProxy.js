const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
	app.use(
		"/api/user",
		createProxyMiddleware({
			target: "http://localhost:5000",
			changeOrigin: true,
		})
	);
	app.use(
		"/api/products",
		createProxyMiddleware({
			target: "http://localhost:8080",
			changeOrigin: true,
		})
	);
	app.use(
		"/ws/chat",
		createProxyMiddleware({
			target: "ws://localhost:4000",
			changeOrigin: true,
			ws: true,
		})
	);
	app.use(
		"/api/chat",
		createProxyMiddleware({
			target: "http://localhost:4000",
			changeOrigin: true,
		})
	);
	app.use(
		"/resources/images/product",
		createProxyMiddleware({
			target: "http://localhost:8080",
			changeOrigin: true,
		})
	);
	app.use(
		"/resources/images/profile",
		createProxyMiddleware({
			target: "http://localhost:5000",
			changeOrigin: true,
		})
	);
};
