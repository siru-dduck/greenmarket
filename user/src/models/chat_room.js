module.exports = function(sequelize, DataTypes) {
  return sequelize.define('chat_room', {
    id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      primaryKey: true,
      comment: "체팅방아이디"
    },
    article_id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      comment: "게시글 아이디"
    },
    user_id_seller: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      comment: "판매자 사용자 id"
    },
    user_id_buyer: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      comment: "사용자 id"
    }
  }, {
    sequelize,
    tableName: 'chat_room'
    });
};
