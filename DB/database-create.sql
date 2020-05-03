-- 创建不良资产库
CREATE database if NOT EXISTS `distressed_asset` default character set utf8 collate utf8_general_ci;
use `distressed_asset`;

-- 数据库建表前缀遵循以下命名规则：
-- 后台专用表以  admin_开始，如 admin_base_resource等
-- 论坛专用表以  bbs_开始，如 bbs_circle等
-- 支付相关表以  uac_开始 uac_account等
-- 用户相关表以  uc_开始，如 uc_user等
-- 消息相关表以  mc_开始，如 mc_message等
-- 公用相关表以  t_开始，如 t_area等
-- 其它特殊表待定；

-- 创建后台鉴权表
DROP TABLE IF EXISTS `admin_base_resource`;
CREATE TABLE `admin_base_resource` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键标识，自增。',
	`name` varchar(512) NOT NULL COMMENT '权限简短名称。',
	`access_url` varchar(1024) DEFAULT NULL COMMENT '权限对应的访问地址。',
	`parent_id` bigint(20) DEFAULT NULL COMMENT '权限呈树状菜单，因此一个权限可能附属于在父权限之下；根权限为空。',
	`description` varchar(1024) DEFAULT NULL COMMENT '备注描述。',
	`level` int(11) DEFAULT '0' COMMENT '0模块,1菜单,2按钮',
	`show_or` tinyint(1) DEFAULT '0' COMMENT '是否作为菜单显示？0不显示 1显示',
	`sequence` int(8) DEFAULT NULL COMMENT '菜单排序值',
	`main_permissions_or` tinyint(1) DEFAULT NULL COMMENT '是否为主权限，0=否，1=是',
	`status` int(1) DEFAULT '0' COMMENT '是否激活，0-有效，-1-失效',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台权限数据列表';

DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键标识，自增。',
	`name` varchar(512) NOT NULL COMMENT '角色名称。',
	`description` varchar(1024) DEFAULT NULL COMMENT '备注描述。',
	`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`status` tinyint(1) DEFAULT '1' COMMENT '角色的状态: ‘1’有效；‘0’无效',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色数据结构表。';

DROP TABLE IF EXISTS `admin_base_role_resource`;
CREATE TABLE `admin_base_role_resource` (
	`role_id` bigint(20) NOT NULL COMMENT '关联角色表主键(T_ROLE)。',
	`resource_id` bigint(20) NOT NULL COMMENT '关联权限表主键(T_PERMISSION)。',
PRIMARY KEY (`role_id`,`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-权限数据结构关联表。';

DROP TABLE IF EXISTS `admin_admin_user`;
CREATE TABLE `admin_admin_user` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`login_username` varchar(128) DEFAULT NULL COMMENT '登陆名。',
	`nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
	`password` varchar(64) DEFAULT NULL COMMENT '登陆密码',
	`security_password` varchar(64) DEFAULT NULL COMMENT '安全密钥',
	`portrait` varchar(248) DEFAULT NULL COMMENT '头像',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`last_login_time` datetime DEFAULT NULL COMMENT '上次登陆时间',
	`type` int(11) DEFAULT NULL COMMENT '用户类型',
	`status` tinyint(1) DEFAULT '1' COMMENT '状态:1-启用 0-禁用',
	`bound_cellphone` varchar(32) DEFAULT NULL COMMENT '绑定手机',
	`bound_email` varchar(512) DEFAULT NULL COMMENT '绑定邮箱',
	`modify_pwd_or` int(11) DEFAULT NULL COMMENT '是否修改密码',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台用户信息表。';

DROP TABLE IF EXISTS `admin_admin_user_role`;
CREATE TABLE `admin_admin_user_role` (
	`user_id` bigint(20) NOT NULL COMMENT '关联用户表主键(T_USER)。',
	`role_id` bigint(20) NOT NULL COMMENT '关联角色表主键(T_ROLE)。',
PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色数据结构关联表。';


DROP TABLE IF EXISTS `t_area`;
CREATE TABLE `t_area` (
	`code` varchar(20) NOT NULL COMMENT '地区编码',
	`name` varchar(20) NOT NULL COMMENT '名称',
	`parent_code` varchar(20) NOT NULL COMMENT '上级编码',
	`root` tinyint(1) NOT NULL,
	`zone` int(11) NOT NULL,
	`status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
PRIMARY KEY (`code`),
UNIQUE KEY `unique_area_code` (`code`),
KEY `index_area_root` (`root`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='省市区表';

DROP TABLE IF EXISTS `t_attachment`;
CREATE TABLE `t_attachment` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '主键标识，自增。',
	`user_id` bigint(20) DEFAULT NULL,
	`uuid` varchar(32) NOT NULL COMMENT '附件UUID，用于下载暴露字段',
	`model` varchar(32) NOT NULL DEFAULT 'OTHER' COMMENT '附件所属模块（用于区分文件目录，参见枚举：UploadModelEnum）',
	`upload_time` datetime NOT NULL COMMENT '上传时间，格式：yyyy-MM-dd hh:mm:ss。',
	`original_filename` varchar(512) DEFAULT NULL COMMENT '附件的原始文件名称。',
	`file_size` int(11) NOT NULL DEFAULT '0' COMMENT '文件大小，以K为单位。',
	`access_path` varchar(512) NOT NULL COMMENT '访问路径的后缀(可能是服务器存储路径的一部分后缀)，不包含WEB访问的根上下文。举例：/upload/create.jpg。',
	`actual_path` varchar(512) DEFAULT NULL COMMENT '附件在服务器保存的绝对路径。',
	`description` varchar(1024) DEFAULT NULL COMMENT '备注描述。',
	`type` varchar(20) NOT NULL DEFAULT '6' COMMENT '附件类型。''IMG''：图片；''PDF''：PDF；''DOC''：DOC/DOCX；''EXL''：EXCEL；''TXT''：文本文件；''OTE''：其他。',
	`status` int(11) DEFAULT NULL COMMENT '状态：0正常，1删除',
	`update_time` datetime DEFAULT NULL COMMENT '修改时间',
PRIMARY KEY (`id`),
KEY `auto_shard_key_user_id` (`user_id`),
UNIQUE KEY `idx_attachment_uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件是系统中公共的数据结构，可以和其他需要附件的数据结构进行泛化关联(any)。';


DROP TABLE IF EXISTS `uc_user`;
CREATE TABLE `uc_user` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
	`login_username` varchar(64) NOT NULL COMMENT '用户名',
	`nickname` varchar(64) NOT NULL DEFAULT '' COMMENT '昵称',
	`password` varchar(64) DEFAULT NULL COMMENT '登陆密码',
	`portrait` varchar(248) DEFAULT NULL COMMENT '用户头像',
	`bound_cellphone` varchar(128) NOT NULL DEFAULT '' COMMENT '绑定手机号码',
	`bound_email` varchar(128) NOT NULL DEFAULT '' COMMENT '绑定邮箱',
	`wechat_key` varchar(128) NOT NULL DEFAULT '' COMMENT '绑定微信唯一openId',
	`share_code` varchar(64) NOT NULL DEFAULT '' COMMENT '个人邀请码',
	`registered_time` datetime DEFAULT NULL COMMENT '注册时间',
	`last_login_time` datetime DEFAULT NULL COMMENT '最后登陆时间',
	`type` int(11) DEFAULT NULL COMMENT '用户类型【0：普通会员，1：VIP会员】',
	`vip_start_date` date DEFAULT NULL COMMENT 'VIP开始时间',
	`vip_end_date` date DEFAULT NULL COMMENT 'VIP结束时间',
	`vip_status` int(11) DEFAULT NULL COMMENT 'VIP状态【1：有效，0：失效】',
	`signature` varchar(256) DEFAULT NULL COMMENT '个性签名',
	`user_type` int(11) DEFAULT '0' COMMENT '0个人用户,1企业用户',
	`recommend_code` varchar(64) DEFAULT NULL COMMENT '推荐人邀请码',
	`status` int(11) NOT NULL COMMENT '状态',
	PRIMARY KEY (`id`),
	UNIQUE KEY `UQ_user_name_phone_email_wechat` (`login_username`,`nickname`,`bound_cellphone`,
	    `bound_email`,`wechat_key`,`share_code`,`status`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';



























