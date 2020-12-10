import React from "react";
import styled from "styled-components";
import saveBearImg from "../../assets/images/save_bear.png";
import {FaRecycle, FaTree} from "react-icons/fa" 
import {ImEarth} from "react-icons/im";

function Banner(){
    return(
        <BannerArticle>
            <div>
                <h2>초록장터는 환경을<br/> 생각합니다.</h2>
                <p>환경을 생각하는 초록장터에서 모두함께 환경을 지켜보아요.</p>
                <BadgeBox>
                    <section>  
                        <div>
                            <FaRecycle size="32"/>
                        </div>
                        <h3>Recycle</h3>
                    </section>
                    <section>  
                        <div>
                            <ImEarth size="32"/>
                        </div>
                        <h3>Earth</h3>
                    </section>
                    <section>  
                        <div>
                            <FaTree size="32"/>
                        </div>
                        <h3>Enviroment</h3>
                    </section>
                </BadgeBox>
            </div>
            <div>
                <img src={saveBearImg} alt="top section 배너 메인이미지"/>  
            </div>
        </BannerArticle>
    );
}

const BannerArticle = styled.article`
    margin:0 auto;
    max-width:768px;
    height:100%;
    display:flex;
    & > div{
        color:#fcfcfc;
        display:flex;
        flex-direction:column;
        justify-content:center;
        align-items:center;
        h2{
            font-size:42px;
            font-weight:800;
            margin-bottom:32px;
        }
        p{
            font-size:20px;
            margin-bottom:52px;
        }
    }
`;

const BadgeBox = styled.div`
    display: flex;
    justify-content:space-between;
    width:100%;
    section{
        display:flex;
        flex-direction:column;
        align-items:center;
        div{
            margin-bottom:10px;
            width:86px;
            height:86px;
            display:flex;
            justify-content:center;
            align-items:center;
            border-radius:50%;
            border:1px solid #fcfcfc;
        }
        div:hover{
            background:#fcfcfc;
            svg{
                color:#1dd1a1;
            }
        }
        & > h3{
            font-size:14px;
        }
    }
`;

export default Banner;