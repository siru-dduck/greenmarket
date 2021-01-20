import styled from "styled-components";

export const Button = styled.button`
	outline: none;
	padding: 10px 13px;
	border-radius: 6px;
	border: ${(props) =>
		props.borderColor ? `1px solid ${props.borderColor}` : "none"};
	color: ${(props) => (props.color ? props.color : "#fcfcfc")};
	font-weight: 500;
	font-size: 16px;
	background-color: ${(props) =>
		props.background ? props.background : "#1dd1a1"};
`;
