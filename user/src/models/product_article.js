/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('product_article', {
    id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      primaryKey: true,
      comment: "게시글 아이디"
    },
    title: {
      type: DataTypes.STRING(100),
      allowNull: false,
      comment: "게시글 제목"
    },
    content: {
      type: DataTypes.TEXT,
      allowNull: true,
      comment: "게시글 내용"
    },
    write_date: {
      type: DataTypes.DATE,
      allowNull: false,
      defaultValue: sequelize.literal('CURRENT_TIMESTAMP'),
      comment: "게시글 작성시각"
    },
    update_date: {
      type: DataTypes.DATE,
      allowNull: true,
      comment: "게시글 수정시각"
    },
    price: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      comment: "상품가격"
    },
    interest_count: {
      type: DataTypes.INTEGER(11),
      allowNull: true,
      defaultValue: 0,
      comment: "상품 관심수 (cf 좋아요 갯수와 비슷한 개념)"
    },
    user_id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      comment: "게시글작성자 ID (user ID)"
    },
    category_id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      comment: "카테고리 아이디"
    },
    status: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      comment: "상품거래 상태(거래진행중:0, :거래완료:1)"
    }
  }, {
    sequelize,
    tableName: 'product_article'
    });
};
