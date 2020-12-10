import React from "react";
import TopHeader from "../Header/TopHeader";
import Banner from "./Banner";
import ProductSection from "./ProductSection";
import {MainLayout, SectionLayout } from "../../util/LayoutStyle";

function LandingPage(props){
    return (        
        <>
            <TopHeader {...props} />
            <MainLayout>
            <SectionLayout background="#1dd1a1">
                <Banner/>
            </SectionLayout>
            <SectionLayout>
                <ProductSection/>
            </SectionLayout>
        </MainLayout>
        </>
    );
}

export default LandingPage;