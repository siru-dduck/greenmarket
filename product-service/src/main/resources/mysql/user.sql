CREATE DATABASE IF NOT EXISTS product_service;

ALTER DATABASE product_service
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON product_service.* TO 'greenmarket'@'%' IDENTIFIED BY 'greenmarket1!';