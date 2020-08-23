-- DROP TABLE IF EXISTS `ecs`;
CREATE TABLE IF NOT EXISTS `ecs`
(
    `id`             int(11)   NOT NULL AUTO_INCREMENT,
    `name`           varchar(255) NOT NULL,
    `description`    varchar(255) DEFAULT NULL,
    `config`         text ,
    `type`           varchar(10)  NOT NULL, -- '类型 FOLDER:文件夹, NODE:叶子节点'
    `parent_id`      bigint(20)   NOT NULL ,--  '父文件夹ID'
    `create_time`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , -- '创建时间'
    `update_time`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP , -- '修改时间'
    PRIMARY KEY (`id`)
);