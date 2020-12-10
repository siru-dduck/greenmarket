/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('product_image', {
    list_num: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      primaryKey: true,
      comment: "게시글 이미지 리스트 번호"
    },
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
    file_url: {
      type: DataTypes.STRING(255),
      allowNull: false,
      comment: "이미지 파일 url"
    }
  }, {
    sequelize,
    tableName: 'product_image'
    });
};
