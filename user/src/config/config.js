const env = require("dotenv");

env.config();

const sequelizeConfig = {
  development: {
    username: "root",
    password: "k8s123@",
    database: "green_market",
    host: "localhost",
    port: 3306,
    dialect: "mysql"
  },
  production: {
    username: "root",
    password: "k8s123!@#",
    database: "green_market",
    host: process.env.MYSQL_SERVICE_HOST,
    port: process.env.MYSQL_SERVICE_PORT,
    dialect: "mysql"
  },
};

module.exports = sequelizeConfig;
