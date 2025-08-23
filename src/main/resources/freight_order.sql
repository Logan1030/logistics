-- 运费订单表
CREATE TABLE `freight_orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单编号',
  `sender_city` varchar(50) NOT NULL COMMENT '发货城市',
  `receiver_city` varchar(50) NOT NULL COMMENT '收货城市',
  `receiver_name` varchar(100) NOT NULL COMMENT '收货人姓名',
  `cargo_code` varchar(100) DEFAULT NULL COMMENT '货物代码',
  `cargo_type` varchar(100) DEFAULT NULL COMMENT '货物类型',
  `vehicle_type` varchar(100) DEFAULT NULL COMMENT '车型信息',
  `freight_result` varchar(100) DEFAULT NULL COMMENT '运费结果',
  `billing_weight` varchar(100) DEFAULT NULL COMMENT '计费重量',
  `cost_statistics` text DEFAULT NULL COMMENT '费用统计详情(JSON格式)',
  `total_amount` decimal(10,2) DEFAULT 0.00 COMMENT '总费用',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '订单状态：1-待确认 2-已确认 3-运输中 4-已完成 5-已取消',
  `remark` text DEFAULT NULL COMMENT '备注信息',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_sender_city` (`sender_city`),
  KEY `idx_receiver_city` (`receiver_city`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运费订单表';

-- 运费订单详情表（扩展信息）
CREATE TABLE `freight_order_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '详情ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `detail_type` varchar(50) NOT NULL COMMENT '详情类型：weight-重量 type-类型 vehicle-车型 freight-运费',
  `detail_key` varchar(100) NOT NULL COMMENT '详情键',
  `detail_value` varchar(255) NOT NULL COMMENT '详情值',
  `detail_unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_detail_type` (`detail_type`),
  CONSTRAINT `fk_order_details` FOREIGN KEY (`order_id`) REFERENCES `freight_orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运费订单详情表';
