CREATE TABLE `email_config` (
  `email_id` bigint NOT NULL AUTO_INCREMENT COMMENT '邮箱配置ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属用户',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱登陆密码',
  `encrypt` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码加密方式，1：明文，2：*',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  `updatetime` timestamp NOT NULL COMMENT '更新时间',
  `flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  PRIMARY KEY (`email_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='邮箱配置表';


CREATE TABLE `email_data` (
  `email_data_id` bigint NOT NULL AUTO_INCREMENT COMMENT '邮件ID',
  `email_ref_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件参考ID',
  `sender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发件人邮箱地址',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件主题',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '邮件内容',
  `receivetime` timestamp NOT NULL COMMENT '收件时间',
  `owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '同步用户',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  `updatetime` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`email_data_id`) USING BTREE,
  UNIQUE KEY `email_ref_id` (`email_ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=317 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='同步邮箱数据';


CREATE TABLE `conference_data` (
  `conference_id` bigint NOT NULL AUTO_INCREMENT COMMENT '会议ID',
  `calendar_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '每个会议的参考id',
  `sender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议发起人',
  `receivetime` timestamp NOT NULL COMMENT '接收时间',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议主题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议内容',
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议地点',
  `starttime` timestamp NOT NULL COMMENT '会议开始时间',
  `endtime` timestamp NOT NULL COMMENT '会议结束时间',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`conference_id`),
  UNIQUE KEY `calendar_id` (`calendar_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会议数据表';



CREATE TABLE `plan_data` (
  `plan_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日程ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属用户',
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详情',
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议位置',
  `starttime` timestamp NOT NULL COMMENT '会议开始时间',
  `endtime` timestamp NOT NULL COMMENT '会议结束时间',
  `plantime` timestamp NOT NULL COMMENT '待办时间',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  `updatetime` timestamp NOT NULL COMMENT '更新时间',
  `flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 1-正常，0-禁用 -1,已删除',
  `source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据来源 0：手动添加 1:邮件同步',
  PRIMARY KEY (`plan_id`) USING BTREE,
  UNIQUE KEY `title` (`title`,`position`,`starttime`,`endtime`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日程数据表';


CREATE TABLE `email_filter` (
  `filter_id` bigint NOT NULL AUTO_INCREMENT COMMENT '过滤条件ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '条件设置者',
  `filter_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sender:发件邮件，title：邮件主题',
  `filter_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '过滤内容',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '过滤类型 1:黑名单 0：白名单',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  `updatetime` timestamp NOT NULL COMMENT '更新时间',
  `flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 1：生效  0：禁用',
  PRIMARY KEY (`filter_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='同步过滤条件表';

CREATE TABLE `title_frequency` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '名词id',
  `words` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主题中分离出的名词',
  `frequency` int NOT NULL COMMENT '出现频数',
  PRIMARY KEY (`id`,`words`) USING BTREE,
  UNIQUE KEY `words` (`words`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日程主题名词词频统计表';
