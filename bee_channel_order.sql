/*
 Navicat Premium Data Transfer

 Source Server         : docker mysql 5.7
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.25.128:3306
 Source Schema         : bee_channel_order

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 08/06/2024 10:37:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` bigint NOT NULL COMMENT '支付记录表主键',
  `order_id` bigint NULL DEFAULT NULL COMMENT '对应订单Id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录名称',
  `create_time` datetime NOT NULL COMMENT '支付记录生成实践',
  `other_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '外部支付用户名称',
  `total_price` decimal(10, 2) NOT NULL COMMENT '支付价格',
  `status` int NOT NULL COMMENT '支付状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade
-- ----------------------------
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade`  (
  `id` bigint NOT NULL COMMENT '订单表主键',
  `total_price` decimal(10, 2) NOT NULL COMMENT '订单价格',
  `create_time` datetime NOT NULL COMMENT '订单生成日期',
  `user_id` bigint NOT NULL COMMENT '订单用户Id',
  `details` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单详情Json',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单描述',
  `status` int NOT NULL COMMENT '订单状态',
  `derive_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单来源Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
