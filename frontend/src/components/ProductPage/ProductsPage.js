import React from "react";
import Product from "./Product";
import TopHeader from "../Header/TopHeader";
import { MainLayout } from "../../util/style/LayoutStyle";

function ProductsPage(props) {
	return (
		<>
			<TopHeader {...props} />
			<MainLayout>
				<Product {...props} />
			</MainLayout>
		</>
	);
}

export default ProductsPage;
