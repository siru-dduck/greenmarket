/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('category', {
    id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      primaryKey: true,
      comment: "카테고리 아이디"
    },
    name: {
      type: DataTypes.STRING(30),
      allowNull: false,
      comment: "카테고리  이름"
    }
  }, {
    sequelize,
    tableName: 'category'
    });
};
