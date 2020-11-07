/*创建数据库javacode2018*/
DROP DATABASE IF EXISTS `javacode2018`;
CREATE DATABASE `javacode2018`;
USE `javacode2018`;

/*创建表结构*/
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE t_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键，用户id，自动增长',
  `name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '姓名',
  `age` SMALLINT NOT NULL DEFAULT 1 COMMENT '年龄',
  `salary` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '薪水',
  `sex` TINYINT NOT NULL DEFAULT 0 COMMENT '性别,0:未知,1:男,2:女'
) COMMENT '用户表';

SELECT * FROM t_user;