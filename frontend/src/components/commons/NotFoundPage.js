import React, { useEffect } from 'react'
import styled from 'styled-components';
import saveBearImg from "../../assets/images/save_bear.png";
import io from "socket.io-client";

function NotFoundPage({isSetTopMargin}) {
    useEffect(()=>{
        console.log("굿");
        io.connect("localhost:8140/", {
            path: "/ws/chat",
            transports: ["websocket"],
        })
    })
    return (
        <PageContainer isSetTopMargin={isSetTopMargin}>
            <h2>페이지를 찾을 수 없습니다.</h2>
            <img src={saveBearImg} alt="배너 이미지"/>
        </PageContainer>
    )
}

const PageContainer = styled.section`
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #1dd1a1;
    height: ${props => props.isSetTopMargin?"calc(100vh - 69px)":"100vh"};
    h2{
        color: white;
        font-family: 'yg-jalnan';
        font-size: 42px;
    }
`;

export default NotFoundPage
