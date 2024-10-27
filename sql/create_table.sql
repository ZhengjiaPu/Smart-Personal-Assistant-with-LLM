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
    index idx_Id (id)
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
CREATE TABLE IF NOT EXISTS schedules (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
                                         content TEXT COMMENT 'Schedule content',
                                         title VARCHAR(255) COMMENT 'Title of the schedule',
                                         userId BIGINT NOT NULL COMMENT 'User ID',
                                         createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
                                         updateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
                                         startTime VARCHAR(255) COMMENT 'Start time of the schedule',
                                         endTime VARCHAR(255) COMMENT 'End time of the schedule',
                                         isDelete TINYINT DEFAULT 0 COMMENT 'Soft delete flag (0 = active, 1 = deleted)',
                                         INDEX idx_userId (userId)
) COMMENT='Schedules table' COLLATE = utf8mb4_unicode_ci;

-- Body Data表
create table if not exists body_data
(
    id         bigint auto_increment comment 'id' primary key,
    userId     bigint                             not null comment 'userId',
    height_cm    INT                      NULL COMMENT 'User height in cm',  -- User height in cm
    weight_kg    INT                     NULL COMMENT 'User weight in kg',  -- User weight in kg
    bmi          DOUBLE                     GENERATED ALWAYS AS (weight_kg / (height_cm / 100 * height_cm / 100)) STORED COMMENT 'Calculated BMI',  -- Calculated BMI
    createTime datetime default CURRENT_TIMESTAMP not null comment 'createTime',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updateTime',
    isDelete   tinyint  default 0                 not null comment 'isDelete',
    CONSTRAINT fk_user FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,  -- Foreign key constraint with cascade delete
    index idx_userId (userId)
) comment 'body_data' collate = utf8mb4_unicode_ci;

-- Health Monitoring Data
create table if not exists health_data
(
    id              bigint auto_increment comment 'id' primary key,
    userId          bigint                             not null comment 'userId',
    heartRate       varchar(256)                       not null comment '心率数据(BPM)',  -- 存储心率数据，逗号分隔
    averageHeartRate DECIMAL(5, 2)                     null comment '心率平均值(BPM)',  -- 心率平均值
    stepsPerMinute  int                                null comment '步频',
    sleepTime       DECIMAL(4, 2)                      null comment '平均睡眠时间（小时/天）',
    deepSleep       DECIMAL(5, 2)                      null comment '深度睡眠占比',
    lightSleep      DECIMAL(5, 2)                      null comment '浅睡眠占比',
    remSleep        DECIMAL(5, 2)                      null comment '快速眼动睡眠占比',
    caloriesBurned  int                                null comment '燃烧的卡路里',
    doctorName      varchar(256)                       null comment '医生姓名',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment 'isDelete',
    CONSTRAINT fk_user_health_data FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,
    index idx_userId (userId)
) comment 'health_data' collate = utf8mb4_unicode_ci;

-- Medicine Plan
create table if not exists medicine_plan
(
    id            bigint auto_increment comment 'id' primary key,
    userId        bigint                             not null comment 'userId',
    medicineName  varchar(256)                       not null comment '药物名称',
    dosage        varchar(256)                       not null comment '剂量/次数',
    alarmTime     time                               not null comment '闹钟时间',
    isDelete      tinyint  default 0                 not null comment 'isDelete',
    CONSTRAINT fk_user_medicine_plan FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,
    index idx_userId (userId)
) comment 'medicine_plan' collate = utf8mb4_unicode_ci;