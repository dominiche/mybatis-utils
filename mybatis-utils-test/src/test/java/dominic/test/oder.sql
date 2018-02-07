CREATE TABLE `order` (
  `sub_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(36) NOT NULL COMMENT '订购用户ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '订购用户名称',
  `sub_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '订购时间',
  `sub_number` varchar(50) NOT NULL COMMENT '订购流水号',
  `sub_type` char(1) DEFAULT NULL COMMENT '订单类型',
  `actual_total` decimal(15,2) unsigned DEFAULT NULL COMMENT '实际总值',
  `pay_date` datetime DEFAULT NULL COMMENT '购买时间',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sub_id`),
  UNIQUE KEY `sub_number_unique_ind` (`sub_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单订购表';