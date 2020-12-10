const path = require("path");
const Sequelize = require("sequelize");

const env = process.env.NODE_ENV || "development";
const config = require(path.join(__dirname, "..", "config","config.js"))[
	env
];

const db = {};

const sequelize = new Sequelize(
	config.database,
	config.username,
	config.password,
	config
);

db.sequelize = sequelize;
db.Sequelize = Sequelize;

db.ChatRoom = require("./chat_room")(sequelize, Sequelize);
db.ChatMessage = require("./chat_message")(sequelize, Sequelize);

db.ChatRoom.hasMany(db.ChatMessage, { foreignKey : 'room_id', sourceKey:"id"});
db.ChatMessage.belongsTo(db.ChatRoom, { foreignKey : 'room_id', targetKey:"id"});

module.exports = db;
