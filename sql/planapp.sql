CREATE TABLE `email_config` (
  `email_id` bigint NOT NULL COMMENT '邮箱配置ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属用户',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱登陆密码',
  `encrypt` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码加密方式，1：明文，2：*',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  `updatetime` timestamp NOT NULL COMMENT '更新时间',
  `flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  PRIMARY KEY (`email_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='邮箱配置表';

CREATE TABLE `email_data` (
  `email_data_id` bigint NOT NULL AUTO_INCREMENT COMMENT '邮件ID',
  `email_ref_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮件参考ID',
  `sender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发件人邮箱地址',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件主题',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '邮件内容',
  `receivetime` timestamp NOT NULL COMMENT '收件时间',
  `owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '同步用户',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  `updatetime` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`email_data_id`) USING BTREE,
  UNIQUE KEY `email_ref_id` (`email_ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='同步邮箱数据';


CREATE TABLE `email_filter` (
  `filter_id` bigint NOT NULL COMMENT '过滤条件ID',
  `owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '条件设置者',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主题过滤参数',
  `sender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发件人过滤参数',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '过滤类型 1:黑名单 0：白名单',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  `updatetime` timestamp NOT NULL COMMENT '更新时间',
  `flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 1：生效  0：禁用',
  PRIMARY KEY (`filter_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='同步过滤条件表';

CREATE TABLE `plan_data` (
  `plan_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日程ID',
  `owner` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属用户',
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主题',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '详情',
  `tag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `plantime` timestamp NOT NULL COMMENT '待办时间',
  `seq` int NOT NULL COMMENT '排序',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  `updatetime` timestamp NOT NULL COMMENT '更新时间',
  `flag` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 1-正常，0-禁用 -1,已删除',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据来源 0：手动添加 1:邮件同步',
  PRIMARY KEY (`plan_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日程数据表';


CREATE TABLE `plan_data` (
  `plan_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日程ID',
  `owner` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属用户',
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主题',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '详情',
  `tag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `plantime` timestamp NOT NULL COMMENT '待办时间',
  `seq` int NOT NULL COMMENT '排序',
  `createtime` timestamp NOT NULL COMMENT '创建时间',
  `updatetime` timestamp NOT NULL COMMENT '更新时间',
  `flag` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 1-正常，0-禁用 -1,已删除',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据来源 0：手动添加 1:邮件同步',
  PRIMARY KEY (`plan_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日程数据表';

