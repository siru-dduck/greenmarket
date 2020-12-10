module.exports = function(sequelize, DataTypes) {
    return sequelize.define('chat_room', {
      id: {
        type: DataTypes.INTEGER(11),
        autoIncrement: true,
        allowNull: false,
        primaryKey: true,
        comment: "체팅방 아이디"
      },
      article_id: {
        type: DataTypes.INTEGER(11),
        allowNull: false,
        comment: "게시글 아이디"
      },
      user_id_buyer: {
        type: DataTypes.INTEGER(11),
        allowNull: false,
        comment: "구매자 사용자 id"
      }
    }, 
    {
      timestamps: false,
      sequelize,
      tableName: 'chat_room'
    });
  };
  