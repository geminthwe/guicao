-- 创建数据库
CREATE DATABASE IF NOT EXISTS dormitory_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dormitory_management;

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(50) DEFAULT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `dormitory` varchar(50) DEFAULT NULL COMMENT '宿舍号',
    `college` varchar(100) DEFAULT NULL COMMENT '学院',
    `major` varchar(100) DEFAULT NULL COMMENT '专业',
    `student_id` varchar(20) DEFAULT NULL COMMENT '学号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) NOT NULL COMMENT '手机号',
    `points` int(11) DEFAULT 0 COMMENT '积分',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
    `gender` tinyint(1) DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_student_id` (`student_id`),
    UNIQUE KEY `uk_email` (`email`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 任务表
CREATE TABLE IF NOT EXISTS `task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `description` varchar(255) NOT NULL COMMENT '任务描述',
    `area` varchar(100) NOT NULL COMMENT '清洁区域',
    `duration` int(11) NOT NULL COMMENT '任务时长（分钟）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

CREATE TABLE IF NOT EXISTS `dormitory_task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dormitory` varchar(50) NOT NULL COMMENT '宿舍号',
    `task_id` bigint(20) NOT NULL COMMENT '任务ID',
    `start_time` datetime NOT NULL COMMENT '开始时间',
    `end_time` datetime NOT NULL COMMENT '结束时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dormitory_task` (`dormitory`, `task_id`),
    FOREIGN KEY (`task_id`) REFERENCES `task`(`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍与任务绑定表';

-- 排班表
CREATE TABLE IF NOT EXISTS `schedule` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '排班ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `task_id` bigint(20) NOT NULL COMMENT '任务ID',
    `start_time` datetime NOT NULL COMMENT '开始时间',
    `end_time` datetime NOT NULL COMMENT '结束时间',
    `status` tinyint(1) DEFAULT 0 COMMENT '状态：0-未完成，1-已完成',
    `points` int(11) NOT NULL COMMENT '获得积分',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `date` date NOT NULL COMMENT '日期',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`),
    FOREIGN KEY (`task_id`) REFERENCES `task`(`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排班表';

CREATE TABLE IF NOT EXISTS `user_schedule` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) NOT NULL,
    `title` VARCHAR(100) NOT NULL COMMENT '日程标题',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `date` date NOT NULL COMMENT '日期',
    `type` VARCHAR(50) DEFAULT 'class' COMMENT '类型（class, custom）',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户日程表';

-- 奖励与积分表
CREATE TABLE IF NOT EXISTS `reward_points` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `points` int(11) NOT NULL COMMENT '获得积分',
    `reason` varchar(255) NOT NULL COMMENT '获取原因',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='奖励与积分表';

-- 群聊消息表
CREATE TABLE IF NOT EXISTS `chat_message` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `sender_id` bigint(20) NOT NULL COMMENT '发送者ID',
    `content` text NOT NULL COMMENT '消息内容',
    `dormitory_id` BIGINT(20) NOT NULL COMMENT '所属宿舍ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`sender_id`) REFERENCES `sys_user`(`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群聊消息表';

-- 登录记录表
CREATE TABLE user_sign_in (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    sign_date DATE NOT NULL,
    continuous_days INT DEFAULT 1,
    UNIQUE (user_id, sign_date)
);ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录记录表';