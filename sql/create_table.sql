# 数据库初始化


-- 创建库
create database if not exists spa_db;

-- 切换库
use spa_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment 'Account',
    userPassword varchar(512)                           not null comment 'Password',
    userName     varchar(256)                           null comment 'UserName',
    userAvatar   varchar(1024)                          null comment 'UserAvatar',
    userProfile  varchar(512)                           null comment 'UserProfile',
    userRole     varchar(256) default 'user'            not null comment 'UserRole：user/admin/healthcare/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment 'CreateTime',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'UpdateTime',
    isDelete     tinyint      default 0                 not null comment 'IsDelete',
    index idx_unionId (id)
) comment 'user' collate = utf8mb4_unicode_ci;

-- 报告表
create table if not exists report
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment 'title',
    content    text                               null comment 'content',
    userId     bigint                             not null comment 'userId',
    createTime datetime default CURRENT_TIMESTAMP not null comment 'createTime',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updateTime',
    isDelete   tinyint  default 0                 not null comment 'isDelete',
    index idx_userId (userId)
) comment 'report' collate = utf8mb4_unicode_ci;

-- Schedules表
create table if not exists schedules
(
    id         bigint auto_increment comment 'id' primary key,
    content    text                               null comment 'content',
    userId     bigint                             not null comment 'userId',
    createTime datetime default CURRENT_TIMESTAMP not null comment 'createTime',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updateTime',
    isDelete   tinyint  default 0                 not null comment 'isDelete',
    index idx_userId (userId)
) comment 'report' collate = utf8mb4_unicode_ci;


