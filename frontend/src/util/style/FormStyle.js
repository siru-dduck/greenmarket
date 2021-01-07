import styled from "styled-components";

export const FormLayout = styled.div`
	width: 100%;
	min-height: calc(100vh - 69px);
	display: flex;
	align-items: flex-start;
	justify-content: center;
`;

export const ProductForm = styled.form`
	margin-top: 16px;
	border: 1px solid #dbdbdb;
	border-radius: 6px;
	padding: 16px 24px;
	display: flex;
	align-items: flex-start;
	flex-direction: column;

	h2 {
		font-size: 28px;
		font-family: "yg-jalnan";
		color: #333;
		margin-bottom: 32px;
		align-self: center;
	}

	.image-upload {
		width: 520px;
		margin-bottom: 20px;
		.image-upload-btn {
			width: 80px;
			height: 80px;
			display: flex;
			justify-content: center;
			align-items: center;
			flex-direction: column;
			border: 1px solid #666;
			background-color: #fcfcfc;
			border-radius: 6px;
			outline: none;
			font-family: inherit;
			.btn-text {
				&:first-of-type {
					margin-top: 5px;
				}
				font-weight: 500;
				color: #444;
				font-size: 13px;
			}
		}

		.image-list {
			margin-top: 16px;
			display: flex;
			width: 100%;
			flex-wrap: wrap;
			li {
				&:nth-child(n + 4) {
					margin-top: 3%;
				}
				width: 156px;
				height: 156px;
				margin-right: 3%;
				overflow: hidden;
				position: relative;
				.image-delete-btn {
					border: none;
					outline: none;
					background: none;
					position: absolute;
					right: 3px;
					top: 3px;
				}
				img {
					width: 100%;
					height: auto;
				}
			}
		}
	}

	.form-paragraph {
		display: flex;
		align-items: center;
		margin-bottom: 16px;
		h3 {
			margin-right: 16px;
			width: 140px;
		}
		select {
			position: relative;
			appearance: none;
			padding: 10px 13px;
			font-size: 16px;
			font-weight: 500;
			font-family: inherit;
			color: #333;
			outline: none;
			border-radius: 6px;
			border: 1px solid #dbdb;
			option {
				color: #333;
			}
		}
		input {
			padding: 8px 13px;
			font-size: 15px;
			font-weight: 500;
			font-family: inherit;
			color: #333;
			outline: none;
			border-radius: 6px;
			border: 1px solid #dbdb;
		}
		textarea {
			padding: 8px 13px;
			resize: none;
			width: 300px;
			height: 120px;
			border-radius: 6px;
			border: 1px solid #dbdbdb;
			font-size: 14px;
			font-weight: 500;
			font-family: inherit;
			color: #333;
			outline: none;
		}
		.region_filed {
			padding: 8px 13px;
			font-size: 15px;
			font-weight: 500;
			font-family: inherit;
			color: #333;
			outline: none;
			border-radius: 6px;
			border: 1px solid #dbdb;
			&:first-of-type {
				color: #333;
				margin-right: 12px;
			}
		}
	}
	button[type="submit"] {
		outline: none;
		padding: 10px 13px;
		border-radius: 6px;
		border: 1px solid #dbdbdb;
		color: #fcfcfc;
		font-weight: 500;
		font-size: 16px;
		background-color: #1dd1a1;
	}
`;
