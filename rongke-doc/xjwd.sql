

-- ----------------------------
-- Table structure for about_xed
-- ----------------------------
DROP TABLE IF EXISTS `about_xed`;
CREATE TABLE `about_xed`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `about_us` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `serve_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wexin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '关于我们' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for address_log
-- ----------------------------
DROP TABLE IF EXISTS `address_log`;
CREATE TABLE `address_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `user_phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机',
  `province` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `county` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `area_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省代码，若无则用市代码',
  `county_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区代码',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_address_user1_idx`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地址信息' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色外键id',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 1 COMMENT '默认1表示正常，2表示冻结',
  `gmt_datetime` datetime  NULL DEFAULT NULL COMMENT '创建时间',
  `upt_datetime` datetime  NULL DEFAULT NULL COMMENT '修改时间',
  `department_id` bigint(11) NULL DEFAULT NULL COMMENT '部门外键',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 131 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for admin_authority
-- ----------------------------
DROP TABLE IF EXISTS `admin_authority`;
CREATE TABLE `admin_authority`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '权限表id',
  `admin_id` bigint(20) NULL DEFAULT NULL COMMENT '管理员id',
  `catalog_id` bigint(11) NULL DEFAULT NULL COMMENT '目录id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态:默认1正常,2删除',
  `gmt_datetime` date NULL DEFAULT NULL COMMENT '创建时间',
  `upt_datetime` date NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for admin_catalog
-- ----------------------------
DROP TABLE IF EXISTS `admin_catalog`;
CREATE TABLE `admin_catalog`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '目录表id',
  `catalog_name` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目录名字',
  `catalog_url` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目录url',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父目录，没有则为null',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态1代表使用2代表废弃',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for agreement
-- ----------------------------
DROP TABLE IF EXISTS `agreement`;
CREATE TABLE `agreement`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `content` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `type` int(255) NULL DEFAULT NULL COMMENT '类型(1,借款协议 2,借款服务协议,3用户注册协议,4关于我们)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同模板' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for app_feedback
-- ----------------------------
DROP TABLE IF EXISTS `app_feedback`;
CREATE TABLE `app_feedback`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `type` int(33) NULL DEFAULT NULL COMMENT '反馈类型1认证2借款3还款4体验与界面6其他',
  `content` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `img_url` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_app_feedback_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户反馈' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for backup_copy
-- ----------------------------
DROP TABLE IF EXISTS `backup_copy`;
CREATE TABLE `backup_copy`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `login_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道商名称',
  `link_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推广链接',
  `proportion` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '每单分成利润比例',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录token',
  `member_count` int(11) NULL DEFAULT 0 COMMENT '总注册会员数',
  `apply_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '总申请金额',
  `out_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '总放款金额',
  `profit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '总分成利润',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态: 1,正常 2,删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 112 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for collection_record
-- ----------------------------
DROP TABLE IF EXISTS `collection_record`;
CREATE TABLE `collection_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(20) NULL DEFAULT NULL,
  `order_id` bigint(20) NULL DEFAULT NULL,
  `text` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '记录',
  `create_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '催收记录' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `save_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '节约金额，默认0.00',
  `limit_time` date NULL DEFAULT NULL COMMENT '限制使用时间',
  `get_limit` int(11) NULL DEFAULT 0 COMMENT '默认0表示不限制，其他则表示每人领取限制',
  `all_mount` int(11) NULL DEFAULT 0 COMMENT '优惠券总数',
  `use_mount` int(11) NULL DEFAULT 0 COMMENT '已使用的优惠券',
  `get_mount` int(11) NULL DEFAULT NULL COMMENT '已领取未使用的优惠券',
  `coupon_status` int(11) NULL DEFAULT 2 COMMENT '优惠券状态，默认1表示正常，2表示暂停使用,3表示过期优惠券',
  `coupou_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `valid_time` int(11) NULL DEFAULT NULL COMMENT '有效时长(天)',
  `type` int(10) NULL DEFAULT NULL COMMENT '1新用户订单2邀请好友奖励3自由发放',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优惠券,红包表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `department` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '部门名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for each_picture
-- ----------------------------
DROP TABLE IF EXISTS `each_picture`;
CREATE TABLE `each_picture`  (
  `id` bigint(11) NOT NULL,
  `gmt_datetime` datetime  NULL DEFAULT NULL COMMENT '创建时间',
  `upt_datetime` datetime  NULL DEFAULT NULL COMMENT '修改时间',
  `img_url` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `link_url` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for first_catalogue
-- ----------------------------
DROP TABLE IF EXISTS `first_catalogue`;
CREATE TABLE `first_catalogue`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级目录名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '一级目录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for help_center
-- ----------------------------
DROP TABLE IF EXISTS `help_center`;
CREATE TABLE `help_center`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `type` int(33) NULL DEFAULT NULL COMMENT '类型 1帮助中心2资讯中心',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帮助中心' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for loan_order
-- ----------------------------
DROP TABLE IF EXISTS `loan_order`;
CREATE TABLE `loan_order`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `param_setting_id` bigint(33) NOT NULL,
  `order_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号码',
  `lian_pay_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连连支付商户打款订单号',
  `lian_repay_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连连支付用户还款单号',
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_card_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡号',
  `interest_precent` double(255, 0) NULL DEFAULT NULL,
  `limit_days` int(255) NULL DEFAULT NULL COMMENT '借款期限（天）',
  `borrow_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '借款金额',
  `real_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '到账金额',
  `interest_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '利息',
  `place_serve_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '平台服务费',
  `msg_auth_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '信息认证费',
  `risk_serve_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '风控服务费',
  `risk_plan_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '风险准备金',
  `wate_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '综合费用',
  `save_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '优惠卷节省金额',
  `need_pay_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '应还金额',
  `real_pay_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '实还金额',
  `gmt_datetime` datetime  NULL DEFAULT NULL COMMENT '借款时间',
  `upt_dateime` datetime  NULL DEFAULT NULL,
  `pass_time` datetime  NULL DEFAULT NULL COMMENT '放款时间',
  `give_time` datetime  NULL DEFAULT NULL COMMENT '打款时间',
  `limit_pay_time` date NULL DEFAULT NULL COMMENT '应还款时间',
  `overdue_time` date NULL DEFAULT NULL COMMENT '超出容限期时间',
  `real_pay_time` datetime  NULL DEFAULT NULL COMMENT '实际还款时间',
  `user_coupon_id` bigint(33) NULL DEFAULT NULL,
  `overdue_days` int(33) NULL DEFAULT 0 COMMENT '逾期天数',
  `overdue_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '逾期金额',
  `allow_days` decimal(33, 0) NULL DEFAULT 0 COMMENT '容限期',
  `allow_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '容限期费用',
  `auditor_id` bigint(33) NULL DEFAULT NULL COMMENT '审核员id',
  `order_status` int(33) NULL DEFAULT 0 COMMENT '订单状态 默认0未申请    1审核中2待打款3待还款4容限期中5已超出容限期6已还款7审核失败8坏账9打款中10还款中',
  `give_status` int(33) NULL DEFAULT 0 COMMENT '打款状态0未打款1打款还未成功2打款成功3打款失败4退款状态',
  `pay_status` int(255) NULL DEFAULT 0 COMMENT '还款状态 1正常还款2逾期还款',
  `agreement_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借款协议',
  `agreement_two_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '借款服务协议',
  `no_agree` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借款协议号',
  `repayment_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '还款计划编号',
  `consult_repayment_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '协商款金额',
  `press_money_man` bigint(33) NULL DEFAULT NULL COMMENT '催款员',
  `extend_num` int(33) NULL DEFAULT 0,
  `channel_profit` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '利润',
  `risk_items` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `td_score` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同盾分',
  `type` int(10) NULL DEFAULT NULL COMMENT '1,续期2还款',
  `request_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付请求号',
  `press_times` int(10) NULL DEFAULT 0 COMMENT '催收次数',
  `last_press_date` date NULL DEFAULT NULL COMMENT '最近一次催收时间',
  `extend_type` int(10) DEFAULT '0' COMMENT '0:未续期 1:被续期 2:续期' ,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_order_coupon1`(`user_coupon_id`) USING BTREE,
  INDEX `fk_order_admin1`(`user_id`) USING BTREE,
  INDEX `fk_order_param1`(`param_setting_id`) USING BTREE,
  INDEX `fk_order_admin2`(`auditor_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100074 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '借款订单' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for login_record
-- ----------------------------
DROP TABLE IF EXISTS `login_record`;
CREATE TABLE `login_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_time` datetime  NULL DEFAULT NULL,
  `phone` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `app_version` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT NULL COMMENT '类型：1(后台登录) 2(app登录)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5423 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for msg_model
-- ----------------------------
DROP TABLE IF EXISTS `msg_model`;
CREATE TABLE `msg_model`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `type` int(33) NULL DEFAULT NULL COMMENT '类型 1借款成功 2还款前一天 3还款日 4还款成功 5逾期',
  `content` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for order_extend
-- ----------------------------
DROP TABLE IF EXISTS `order_extend`;
CREATE TABLE `order_extend`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(33) NULL DEFAULT NULL,
  `extend_days` int(33) NULL DEFAULT NULL COMMENT '续期天数',
  `extend_money` decimal(33, 2) NULL DEFAULT NULL,
  `extend_lianlian_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '续期连连支付单号',
  `repayment_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连连还款计划单号',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `status` int(33) NULL DEFAULT 0 COMMENT '0续期未成功 1续期成功',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_extend_order1`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4293 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for param_setting
-- ----------------------------
DROP TABLE IF EXISTS `param_setting`;
CREATE TABLE `param_setting`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `limit_days` int(33) NULL DEFAULT NULL COMMENT '贷款期限',
  `min_borrow_money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '贷款金额',
  `max_borrow_money` decimal(33, 2) NULL DEFAULT 0.00,
  `interest_percent` double(10, 2) NULL DEFAULT NULL COMMENT '利息 %',
  `place_serve_percent` double(10, 2) NULL DEFAULT NULL COMMENT '平台服务费',
  `msg_auth_percent` double(10, 2) NULL DEFAULT NULL COMMENT '信息认证费',
  `risk_serve_percent` double(10, 2) NULL DEFAULT NULL COMMENT '风控服务费',
  `risk_plan_percent` double(10, 2) NULL DEFAULT NULL COMMENT '风险准备金',
  `allow_days` int(33) NULL DEFAULT NULL COMMENT '容限期',
  `allow_percent` double(10, 2) NULL DEFAULT NULL COMMENT '容限期日利率',
  `overdue_percent` double(10, 2) NULL DEFAULT NULL COMMENT '逾期日利率',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 1 COMMENT '数据状态，1表示可用，2表示禁用，3表示删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pay_feedback
-- ----------------------------
DROP TABLE IF EXISTS `pay_feedback`;
CREATE TABLE `pay_feedback`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `reason` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '还款反馈原因',
  `img_url` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pay_feedbak_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 145 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pay_order_confirm
-- ----------------------------
DROP TABLE IF EXISTS `pay_order_confirm`;
CREATE TABLE `pay_order_confirm`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `loan_order_id` bigint(11) NULL DEFAULT NULL,
  `no_order` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '疑似订单号',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `recive_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款人',
  `recive_bank_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款账户',
  `money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打款金额',
  `status` int(255) NULL DEFAULT 0 COMMENT '状态0未确认1确认再次打款2不再打款',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for person_record
-- ----------------------------
DROP TABLE IF EXISTS `person_record`;
CREATE TABLE `person_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `all_count` int(11) NULL DEFAULT NULL COMMENT '用户总数',
  `out_order_count` int(11) NULL DEFAULT NULL COMMENT '放款订单总数',
  `bad_order_count` int(11) NULL DEFAULT NULL COMMENT '坏账数',
  `black_count` int(11) NULL DEFAULT NULL COMMENT '黑名单数',
  `out_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '总放款金额',
  `member_count` int(11) NULL DEFAULT NULL COMMENT '会员总数',
  `over_order_count` int(11) NULL DEFAULT NULL COMMENT '总结清订单数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人员统计' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for press_records
-- ----------------------------
DROP TABLE IF EXISTS `press_records`;
CREATE TABLE `press_records`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `press_money_man` bigint(10) NULL DEFAULT NULL COMMENT '催款人id',
  `is_success` int(10) NULL DEFAULT NULL COMMENT '催收是否成功 1.成功 2失败',
  `content` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '催收备注',
  `order_id` bigint(10) NULL DEFAULT NULL COMMENT '订单id',
  `user_id` bigint(10) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for push_msg
-- ----------------------------
DROP TABLE IF EXISTS `push_msg`;
CREATE TABLE `push_msg`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `link_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `status` int(33) NULL DEFAULT 0 COMMENT '0未推送1已推送2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for push_msg_record
-- ----------------------------
DROP TABLE IF EXISTS `push_msg_record`;
CREATE TABLE `push_msg_record`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `push_msg_id` bigint(33) NULL DEFAULT NULL COMMENT '推送消息id',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for refuse_area
-- ----------------------------
DROP TABLE IF EXISTS `refuse_area`;
CREATE TABLE `refuse_area`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `area_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11188 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for refuse_area_copy
-- ----------------------------
DROP TABLE IF EXISTS `refuse_area_copy`;
CREATE TABLE `refuse_area_copy`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `area_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for role_third_catalogue
-- ----------------------------
DROP TABLE IF EXISTS `role_third_catalogue`;
CREATE TABLE `role_third_catalogue`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `third_catalogue_id` bigint(20) NULL DEFAULT NULL COMMENT '三级目录id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `third_catalogue_id`(`third_catalogue_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8740 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色-功能关联表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for rolling_news
-- ----------------------------
DROP TABLE IF EXISTS `rolling_news`;
CREATE TABLE `rolling_news`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `content` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for second_catalogue
-- ----------------------------
DROP TABLE IF EXISTS `second_catalogue`;
CREATE TABLE `second_catalogue`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级目录名称',
  `first_id` bigint(20) NULL DEFAULT NULL COMMENT '一级外键id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `first_id`(`first_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '二级目录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区code',
  `area_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地区数据字典' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `config_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置名',
  `config_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置值',
  `create_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `revise_time` timestamp  NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tabao_log
-- ----------------------------
DROP TABLE IF EXISTS `tabao_log`;
CREATE TABLE `tabao_log`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `task_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1717 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tb_base_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_base_info`;
CREATE TABLE `tb_base_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `user_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户等级',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `gender` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定手机号',
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实名认证姓名',
  `identity_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实名认证身份证',
  `vip_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员成长值',
  `gmt_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '个人信息' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tb_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_log`;
CREATE TABLE `tb_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `task_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tb_receiver
-- ----------------------------
DROP TABLE IF EXISTS `tb_receiver`;
CREATE TABLE `tb_receiver`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定电话',
  `default_area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '默认地址 1默认 0非默认',
  `zip_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编',
  `gmt_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '淘宝收货地址' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for text_template
-- ----------------------------
DROP TABLE IF EXISTS `text_template`;
CREATE TABLE `text_template`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `text` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文本内容',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 ：1,帮助中心 2,协议 3,短信 4,消息推送模板 5,关于我们',
  `title` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态：1,正常 2,删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文本模板' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for third_catalogue
-- ----------------------------
DROP TABLE IF EXISTS `third_catalogue`;
CREATE TABLE `third_catalogue`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '三级目录名称',
  `second_id` bigint(20) NULL DEFAULT NULL COMMENT '二级外键id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `second_id`(`second_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 130 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '三级目录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tongdun_audit
-- ----------------------------
DROP TABLE IF EXISTS `tongdun_audit`;
CREATE TABLE `tongdun_audit`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `report_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `success` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reason_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reason_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `final_score` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风险分数',
  `final_decision` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风险结果',
  `device_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `apply_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扫描时间',
  `report_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报告时间',
  `risk_items` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '扫描出来的风险项',
  `address_detect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属地解析',
  `application_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请编号',
  `credit_score` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '信用分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '同盾贷前审核结果' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tongdun_log
-- ----------------------------
DROP TABLE IF EXISTS `tongdun_log`;
CREATE TABLE `tongdun_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `success` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否调用成功 1成功 2失败',
  `reason_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '失败code',
  `reason_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误详情描述',
  `report_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '贷前申请风险报告编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `submit_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '同盾接口提交日志表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `uuid` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `head_img` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `money` decimal(33, 2) NULL DEFAULT 0.00 COMMENT '额度',
  `user_type` int(11) NULL DEFAULT 1 COMMENT '用户类型1普通会员',
  `auth_status` int(11) NULL DEFAULT 0 COMMENT '认证状态，默认0表示未认证，1表示已认证可借款,2暂时拒绝  3永久拒绝',
  `token` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 1 COMMENT '默认1表示正常，2表示黑名单，3表示禁用，4被拒绝(拒绝后，一月之后可借款)',
  `pay_pwd` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付密码',
  `auth_score` int(33) NULL DEFAULT 0 COMMENT '认证分数',
  `phone_sign` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备标识',
  `is_pay` int(33) NULL DEFAULT 0 COMMENT '还款状态 0已还款1还款中或者申请中',
  `coupon_all_count` int(11) NULL DEFAULT 0 COMMENT '优惠券总数',
  `coupon_use_count` int(11) NULL DEFAULT 0 COMMENT '使用总数',
  `coupon_past_count` int(11) NULL DEFAULT 0 COMMENT '过期数',
  `refuse_remove_time` date NULL DEFAULT NULL COMMENT '拒绝状态释放时间',
  `is_old` int(11) NULL DEFAULT 0 COMMENT '是否为老客户',
  `bankauth_num` int(11) NULL DEFAULT 0 COMMENT '银行卡认证次数',
  `channel_id` bigint(33) NULL DEFAULT NULL COMMENT '渠道商id',
  `channel_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道商姓名',
  `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信id',
  `is_xuqi` int(11) NULL DEFAULT NULL COMMENT '0已支付未回调，1回调成功',
  `overdue_times` int(11) NULL DEFAULT NULL COMMENT '逾期次数',
  `loan_times` int(11) NULL DEFAULT NULL COMMENT '贷款次数',
  `press_times` int(11) NULL DEFAULT NULL COMMENT '催收次数',
  `admin_audit_id` bigint(10) NULL DEFAULT NULL COMMENT '信审员id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23572 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户基本信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_auth
-- ----------------------------
DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `baisc_auth` int(10) NULL DEFAULT 0 COMMENT '基本信息是否已验证 0否1是2审核中',
  `bank_auth` int(10) NULL DEFAULT 0 COMMENT '银行是否验证0否1是',
  `phone_auth` int(10) NULL DEFAULT 0 COMMENT '手机是否认证 0否1是',
  `identity_auth` int(10) NULL DEFAULT 0 COMMENT '身份是否验证',
  `zhima_auth` int(10) NULL DEFAULT 0 COMMENT '芝麻是否验证',
  `shebao_auth` int(10) NULL DEFAULT 0 COMMENT '社保是否验证',
  `gongjijin_auth` int(10) NULL DEFAULT 0 COMMENT '公积金是否验证',
  `zhifubao_auth` int(10) NULL DEFAULT 0 COMMENT '支付宝是否验证',
  `jindong_auth` int(10) NULL DEFAULT 0 COMMENT '京东是否验证',
  `taobao_auth` int(10) NULL DEFAULT 0 COMMENT '淘宝是否认证',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `auth_fee` int(11) NULL DEFAULT 0 COMMENT '认证费用是否已缴(0未缴，1已缴）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_auth_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22528 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户认证表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_bank
-- ----------------------------
DROP TABLE IF EXISTS `user_bank`;
CREATE TABLE `user_bank`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `cardname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡名称',
  `cardtype` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡类型',
  `bankcardno` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡号码',
  `idcardno` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `birthday` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户生日',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证地址',
  `bank_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留手机号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `mobile_city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '号码归属城市',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_bank_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12323 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户绑定银行卡信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_basic_msg
-- ----------------------------
DROP TABLE IF EXISTS `user_basic_msg`;
CREATE TABLE `user_basic_msg`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `marry` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '婚姻状况',
  `study` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学历',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `county` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `area_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域代码',
  `address_details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `work_company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作单位',
  `work_place` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作地点',
  `work_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工资',
  `work_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作电话',
  `link_person_name_one` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人一姓名',
  `link_person_phone_one` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人一电话',
  `link_person_relation_one` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人一关系',
  `link_person_name_two` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `link_person_phone_two` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `link_person_relation_two` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_basic_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13742 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户基本信息认证表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `past_datetime` date NULL DEFAULT NULL COMMENT '过期时间',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `coupon_id` bigint(20) NOT NULL COMMENT '优惠券id',
  `save_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '节约金额',
  `invitee_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被邀请人手机号',
  `status` int(11) NULL DEFAULT 1 COMMENT '默认1表示正常，2表示已使用，3表示过期，4表示不能使用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_coupon_coupon1_idx`(`coupon_id`) USING BTREE,
  INDEX `fk_user_coupon_user1_idx`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户优惠券列表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_identity
-- ----------------------------
DROP TABLE IF EXISTS `user_identity`;
CREATE TABLE `user_identity`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `identity_front` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证正面',
  `identity_back` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '反面',
  `face_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人脸照片',
  `identity_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `qq_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'qq',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省市区',
  `address_details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `status` int(255) NULL DEFAULT 1 COMMENT '0未认证1已认证',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_identity_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12581 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_jindong_address
-- ----------------------------
DROP TABLE IF EXISTS `user_jindong_address`;
CREATE TABLE `user_jindong_address`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `order_address` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_jindong_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `login_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录时间',
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经度',
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `address_details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `app_version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_login_log_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6235 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_message
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `title` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `text` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `gmt_datetime` datetime  NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态：1,正常 2,删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_Reference_9`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1062 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户推送消息' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_phone
-- ----------------------------
DROP TABLE IF EXISTS `user_phone`;
CREATE TABLE `user_phone`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机注册姓名',
  `identity_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `account_balance` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户余额',
  `net_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入网时间',
  `net_ageo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网龄',
  `mobile_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户状态',
  `credit_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '等级',
  `status` int(255) NULL DEFAULT 0 COMMENT '状态0未通过验证1通过',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `task_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_phone_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12276 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_phone_list
-- ----------------------------
DROP TABLE IF EXISTS `user_phone_list`;
CREATE TABLE `user_phone_list`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关系',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `belong_area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '号码归属地',
  `call_times` int(11) NULL DEFAULT NULL COMMENT '通话次数',
  `call_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通话时长',
  `call_count_active` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '近6月主叫通话次数',
  `call_count_passive` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '近6月被叫通话次数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_phone_list_user1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4748529 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户通讯录' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_phone_record
-- ----------------------------
DROP TABLE IF EXISTS `user_phone_record`;
CREATE TABLE `user_phone_record`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `belong_area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '号码归属地',
  `called_times` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `conn_times` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `call_times` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `identify_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `another_nm` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联通号码',
  `comm_fee` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用',
  `comm_mode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主被叫',
  `comm_plac` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '号码归属地',
  `comm_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通话时间',
  `start_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `comm_phone_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电信电话号码',
  `comm_total_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通话时间',
  `comm_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通话时期',
  `comm_area_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属地',
  `call_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主被叫',
  `type` int(255) NULL DEFAULT NULL COMMENT '1移动2联通3电信',
  `user_id` bigint(33) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_phone_record1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36709899 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户通话记录' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_phone_record_copy1
-- ----------------------------
DROP TABLE IF EXISTS `user_phone_record_copy1`;
CREATE TABLE `user_phone_record_copy1`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `belong_area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '号码归属地',
  `called_times` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `conn_times` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `call_times` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `identify_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `another_nm` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联通号码',
  `comm_fee` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用',
  `comm_mode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主被叫',
  `comm_plac` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '号码归属地',
  `comm_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通话时间',
  `start_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `comm_phone_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电信电话号码',
  `comm_total_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通话时间',
  `comm_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通话时期',
  `comm_area_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属地',
  `call_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主被叫',
  `type` int(255) NULL DEFAULT NULL COMMENT '1移动2联通3电信',
  `user_id` bigint(33) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_phone_record1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户通话记录' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_tabao_goods
-- ----------------------------
DROP TABLE IF EXISTS `user_tabao_goods`;
CREATE TABLE `user_tabao_goods`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `order_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下单时间',
  `receiver_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `receiver_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `order_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单金额',
  `order_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `product_name` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `order_shop` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商铺',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_tabao_goods_1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 204346 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_taobao
-- ----------------------------
DROP TABLE IF EXISTS `user_taobao`;
CREATE TABLE `user_taobao`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `gender` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定手机号',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '淘宝昵称',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_taobao_1`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3226 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_taobao_address
-- ----------------------------
DROP TABLE IF EXISTS `user_taobao_address`;
CREATE TABLE `user_taobao_address`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `address` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '淘宝地址',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4325 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_taobao_zhifubao
-- ----------------------------
DROP TABLE IF EXISTS `user_taobao_zhifubao`;
CREATE TABLE `user_taobao_zhifubao`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `huabei_can_use_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '花呗余额',
  `huabei_total_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '花呗额度',
  `alipay_remaining_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝余额',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_tongdunfen
-- ----------------------------
DROP TABLE IF EXISTS `user_tongdunfen`;
CREATE TABLE `user_tongdunfen`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NOT NULL,
  `risk_name` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风险名称',
  `rule_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `score` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风险分',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `order_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_zhifubao
-- ----------------------------
DROP TABLE IF EXISTS `user_zhifubao`;
CREATE TABLE `user_zhifubao`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `user_mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `identity_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `verified` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否实名认证0未1已',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `assets_yu_e` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额',
  `assets_yu_ebao` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额宝',
  `huabei_quota` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '花呗额度',
  `huabei_balance` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '花呗余额',
  `huabei_next_repayment_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '花呗下一期还款金额',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `ali_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `ali_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝密码',
  `jiebei_quota` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借呗额度',
  `zhima_point` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '芝麻分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10256 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_zhifubao1
-- ----------------------------
DROP TABLE IF EXISTS `user_zhifubao1`;
CREATE TABLE `user_zhifubao1`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `user_mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `identity_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `verified` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否实名认证0未1已',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `assets_yu_e` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额',
  `assets_yu_ebao` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额宝',
  `huabei_quota` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '花呗额度',
  `huabei_balance` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '花呗余额',
  `huabei_next_repayment_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '花呗下一期还款金额',
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  `ali_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `ali_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝密码',
  `jiebei_quota` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借呗额度',
  `zhima_point` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '芝麻分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7557 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_zhima
-- ----------------------------
DROP TABLE IF EXISTS `user_zhima`;
CREATE TABLE `user_zhima`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `open_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '芝麻用户openid',
  `score` int(20) NOT NULL COMMENT '芝麻分',
  `transaction_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流水凭证',
  `addtime` datetime  NULL DEFAULT NULL,
  `updtime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '芝麻信用' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for you_dun_log
-- ----------------------------
DROP TABLE IF EXISTS `you_dun_log`;
CREATE TABLE `you_dun_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `identity_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '有盾实名认证商户唯一订单号',
  `created_time` datetime  NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12735 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yys_log
-- ----------------------------
DROP TABLE IF EXISTS `yys_log`;
CREATE TABLE `yys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `task_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务id',
  `created_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建 结果code',
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建结果 ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18428 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '同盾日志表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for zhifubao_log
-- ----------------------------
DROP TABLE IF EXISTS `zhifubao_log`;
CREATE TABLE `zhifubao_log`  (
  `id` bigint(33) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(33) NULL DEFAULT NULL,
  `task_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_datetime` datetime  NULL DEFAULT NULL,
  `upt_datetime` datetime  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
