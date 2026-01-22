CREATE DATABASE /*!32312 IF NOT EXISTS */ `docapproval` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

CREATE USER IF NOT EXISTS 'docapproval'@'localhost' IDENTIFIED BY 'apmsetup';

GRANT SELECT, INSERT, UPDATE, DELETE ON docapproval.* TO 'docapproval'@'localhost';

USE `docapproval`;

--
-- Table structure for table `document`
--
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document`
(
    `id`          int unsigned AUTO_INCREMENT,
    `title`       varchar(255) NOT NULL,
    `category`    varchar(50)  NOT NULL,
    `content`     text         NOT NULL,
    `create_time` datetime     NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `user`
--
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`       int unsigned AUTO_INCREMENT,
    `name`     varchar(100) NOT NULL UNIQUE,
    `password` varchar(255) NOT NULL COMMENT '암호화된 비밀번호',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `approval_request`
--
DROP TABLE IF EXISTS `approval_request`;
CREATE TABLE `approval_request`
(
    `id`          int unsigned AUTO_INCREMENT,
    `status`      tinyint(1)   NOT NULL DEFAULT 0,
    `document_id` int unsigned NOT NULL,
    `user_id`     int unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `approval_request_document_id_fk` (`document_id`),
    KEY `approval_request_user_id_fk` (`user_id`),
    CONSTRAINT `approval_request_document_id_fk` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`),
    CONSTRAINT `approval_request_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `approval_status`
--
DROP TABLE IF EXISTS `approval_status`;
CREATE TABLE `approval_status`
(
    `approval_request_id` int unsigned NOT NULL,
    `user_id`             int unsigned NOT NULL,
    `step`                tinyint(1)   NOT NULL DEFAULT 1,
    `reason`              varchar(255) NULL,
    `status`              tinyint(1)   NULL,
    `update_time`         datetime     NULL,
    UNIQUE KEY `approval_status_unique` (`approval_request_id`, `user_id`),
    CONSTRAINT `approval_status_approval_request_id_fk` FOREIGN KEY (`approval_request_id`) REFERENCES `approval_request` (`id`),
    CONSTRAINT `approval_status_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `api_key`
--
DROP TABLE IF EXISTS `api_key`;
CREATE TABLE `api_key`
(
    `id`         int unsigned AUTO_INCREMENT,
    `name`       varchar(100) NOT NULL COMMENT 'API Key를 사용하는 클라이언트 이름',
    `key`        varchar(255) NOT NULL UNIQUE COMMENT 'API Key 값',
    `active`     boolean      NOT NULL DEFAULT true COMMENT '비활성화 여부',
    `created_at` datetime     NOT NULL DEFAULT current_timestamp(),
    `expires_at` datetime     DEFAULT NULL COMMENT '만료일 (선택사항)',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Test API Key data
--
INSERT INTO `api_key` (`name`, `key`, `active`, `created_at`, `expires_at`)
VALUES ('default-client', 'test-api-key-12345', true, NOW(), NULL);