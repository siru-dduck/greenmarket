module.exports = function(sequelize, DataTypes) {
  return sequelize.define('chat_message', {
    id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      primaryKey: true
    },
    room_id: {
      type: DataTypes.INTEGER(11),
      allowNull: false,
      comment: "체팅방아이디"
    },
    sender_type: {
      type: DataTypes.BOOLEAN,
      allowNull: true,
      comment: "0: \"판매자\", 1:\"구매자\""
    },
    message: {
      type: DataTypes.TEXT,
      allowNull: true,
      comment: "채팅메세지"
    },
    create_date: {
      type: DataTypes.DATE,
      allowNull: true,
      defaultValue: sequelize.literal('CURRENT_TIMESTAMP'),
      comment: "채팅을 보낸날짜"
    }
  }, {
    sequelize,
    tableName: 'chat_message'
    });
};
