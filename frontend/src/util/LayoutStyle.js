import styled from "styled-components";

export const MainLayout = styled.main`
    padding-top:69px;
    background-color:#fcfcfc;
`;

export const SectionLayout = styled.section`
    background: ${ ({background}) => background || '#fcfcfc' };
    min-height:calc(100vh - 69px);
`;