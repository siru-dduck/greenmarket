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
		"/api/chat",
		createProxyMiddleware({
			target: "http://localhost:4000",
			changeOrigin: true,
		})
	);
	app.use(
		"/resources/images",
		createProxyMiddleware({
			target: "http://localhost:8080",
			changeOrigin: true,
		})
	);
};
