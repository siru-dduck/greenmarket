CREATE DATABASE IF NOT EXISTS user_service;

ALTER DATABASE user_service
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON user_service.* TO 'greenmarket'@'%' IDENTIFIED BY 'greenmarket1!';