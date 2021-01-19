import React, { useReducer } from "react";
import styled from "styled-components";
import { IoIosArrowBack, IoIosArrowForward } from "react-icons/io";

function slider(state, action) {
	switch (action.type) {
		case "next":
			if (state.index >= state.imageNum - 1) {
				return { ...state, index: state.index };
			}
			return { ...state, index: state.index + 1 };
		case "previous":
			if (state.index <= 0) {
				return { ...state, index: state.index };
			}
			return { ...state, index: state.index - 1 };
		case "selectIndex":
			return { ...state, index: action.selectedIndex };
		default:
			throw new Error("Unknown Function Type");
	}
}

function ImageSlider(props) {
	const { images } = props;
	const [state, dispatch] = useReducer(slider, {
		index: 0,
		imageNum: images.length,
	});

	return (
		<SliderSection>
			<Slider>
				<ImageContainer
					style={{ transform: `translate(-${659 * state.index}px,0px)` }}
				>
					{images.map((image, index) => (
						<ImageWrap key={index}>
							<img
								src={image.fileUrl}
								alt="제품 이미지"
								className="product-item__image"
							/>
						</ImageWrap>
					))}
				</ImageContainer>
				<SlickDotList>
					{images.map((image, index) => (
						<SlickDot
							key={index}
							data-index={index}
							onClick={(event) =>
								dispatch({ type: "selectIndex", selectedIndex: index })
							}
						>
							<SlickDotButton active={index === state.index}></SlickDotButton>
						</SlickDot>
					))}
				</SlickDotList>
			</Slider>
			<Button left onClick={() => dispatch({ type: "previous" })}>
				<IoIosArrowBack />
			</Button>
			<Button right onClick={() => dispatch({ type: "next" })}>
				<IoIosArrowForward />
			</Button>
		</SliderSection>
	);
}

const SliderSection = styled.section`
	width: 729px;
	position: relative;
	height: 500px;
`;

const Slider = styled.div`
	position: relative;
	width: 659px;
	height: 100%;
	margin: 0 auto;
	overflow: hidden;
`;

const ImageContainer = styled.div`
	display: inline-flex;
	flex-wrap: nowrap;
	transition: transform 0.25s ease-in;
`;

const ImageWrap = styled.div`
	width: 659px;
	height: 500px;
	overflow: hidden;
	border-radius: 14px;
	img.product-item__image {
		display: block;
		margin: 0 auto;
		width: 100%;
		height: 100%;
	}
`;

const Button = styled.button`
	position: absolute;
	outline: none;
	border: none;
	background: none;
	padding: 0;
	top: calc(50% - 13px);
	color: #6b7076;
	&:hover {
		color: #abb0b6;
	}
	font-size: 32px;
	${(props) => (props.left ? "left:0" : "")}
	${(props) => (props.right ? "right:0" : "")}
`;

const SlickDotList = styled.ul`
	position: absolute;
	padding-bottom: 12px;
	border-radius: 0 0 14px 14px;
	bottom: 0;
	width: 100%;
	display: flex;
	justify-content: center;
	background-image: linear-gradient(
		to top,
		rgba(0, 0, 0, 0.5),
		rgba(0, 0, 0, 0)
	);
`;

const SlickDot = styled.li`
	margin-right: 8px;
`;

const SlickDotButton = styled.button`
	width: 10px;
	height: 10px;
	cursor: pointer;
	background-color: ${(props) => (props.active ? "#dbdbdb" : "#928c88")};
	border-radius: 50%;
	outline: none;
	border: none;
`;

export default ImageSlider;
