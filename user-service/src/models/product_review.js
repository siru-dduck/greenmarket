/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('product_review', {
    article_id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      primaryKey: true,
      comment: "게시글 아이디",
      references: {
        model: 'product_article',
        key: 'id'
      }
    },
    content: {
      type: DataTypes.TEXT,
      allowNull: false,
      comment: "상품거래후기 내용"
    },
    date: {
      type: DataTypes.DATE,
      allowNull: false,
      defaultValue: sequelize.literal('CURRENT_TIMESTAMP'),
      comment: "삼품거래후기 작성시각"
    },
    user_id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      comment: "상품거래후기 작성자 ID (user  ID)"
    }
  }, {
    sequelize,
    tableName: 'product_review'
    });
};
