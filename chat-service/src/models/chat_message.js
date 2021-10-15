module.exports = function(sequelize, DataTypes) {
    return sequelize.define('chat_message', {
      id: {
        type: DataTypes.INTEGER(11),
        autoIncrement: true,
        allowNull: false,
        primaryKey: true,
        comment: "채팅 메세지 아이디",
      },
      room_id: {
        type: DataTypes.INTEGER(11),
        allowNull: false,
        comment: "체팅방아이디"
      },
      user_id: {
        type: DataTypes.INTEGER(11),
        allowNull: false,
        comment: "메세지를 보낸 사용자 아이디"
      },
      message: {
        type: DataTypes.TEXT,
        allowNull: false,
        comment: "채팅메세지"
      },
      create_date: {
        type: DataTypes.DATE,
        allowNull: false,
        defaultValue: sequelize.literal('CURRENT_TIMESTAMP'),
        comment: "채팅을 보낸날짜"
      }
    }, 
    {
        timestamps: false,
        sequelize,
        tableName: 'chat_message'
    });
  };
  