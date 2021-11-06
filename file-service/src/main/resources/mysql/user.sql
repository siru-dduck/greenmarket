CREATE DATABASE IF NOT EXISTS file_service;

ALTER DATABASE file_service
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON file_service.* TO 'greenmarket'@'%' IDENTIFIED BY 'greenmarket1!';