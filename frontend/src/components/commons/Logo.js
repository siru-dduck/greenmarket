import React from "react";
import styled from "styled-components";
import {Link} from "react-router-dom";
import { FaLeaf } from "react-icons/fa";

function Logo({size}){
    return(
        <Link to="/home">
            <LogoBox size={size}>
                <FaLeaf size={parseInt(size) + 4 || 24} color="#1dd1a1"/>
                <h1>초록장터</h1>
            </LogoBox>
        </Link>
    )
}

const LogoBox = styled.div`
    display: flex;
    align-items:center;
    h1 {
        font-family: yg-jalnan;
        margin-left:8px;
        font-size: ${({size}) => size? `${size}px`: "24px"};
        color:#7b7b7b;
        white-space: nowrap;
    }
`;

export default Logo;