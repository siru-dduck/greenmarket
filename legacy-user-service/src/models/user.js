module.exports = function (sequelize, DataTypes) {
	return sequelize.define(
		"user",
		{
			id: {
				type: DataTypes.INTEGER(11),
				autoIncrement: true,
				allowNull: false,
				primaryKey: true,
				comment: "사용자 id",
			},
			email: {
				type: DataTypes.STRING(50),
				allowNull: false,
				unique: true,
				comment: "OAuth 용 이메일 (카카오,구글,네이버)",
			},
			password: {
				type: DataTypes.STRING(100),
				allowNull: false,
				comment: "비밀번호",
			},
			address1: {
				type: DataTypes.STRING(30),
				allowNull: false,
				comment: "주소1",
			},
			address2: {
				type: DataTypes.STRING(30),
				allowNull: false,
				comment: "주소2",
			},
			nickname: {
				type: DataTypes.STRING(30),
				allowNull: false,
				comment: "사용자 별명",
			},
			profile_image_url: {
				type: DataTypes.STRING(255),
				allowNull: true,
				comment: "프로필 이미지 파일 url",
			},
			kakao_id: {
				type: DataTypes.INTEGER(11),
				allowNull: true,
			},
			naver_id: {
				type: DataTypes.INTEGER(11),
				allowNull: true,
			},
		},
		{
			timestamps: false,
			sequelize,
			tableName: "user",
		}
	);
};
