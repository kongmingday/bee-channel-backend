/*
 Navicat Premium Data Transfer

 Source Server         : docker mysql 5.7
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.25.128:3306
 Source Schema         : bee_channel_live

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 08/06/2024 10:37:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for live
-- ----------------------------
DROP TABLE IF EXISTS `live`;
CREATE TABLE `live`  (
  `id` bigint NOT NULL COMMENT '推流表主键',
  `user_id` bigint NOT NULL COMMENT '推流用户Id',
  `live_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '推流键名',
  `live_secret` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '推流密钥',
  `status` int NOT NULL COMMENT '直播间状态 0-封禁 1-启用',
  `cureent_status` int NOT NULL COMMENT '推流状态 0-未推流 1-推流中',
  `credit_score` int NOT NULL DEFAULT 10 COMMENT '信用分值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for live_info
-- ----------------------------
DROP TABLE IF EXISTS `live_info`;
CREATE TABLE `live_info`  (
  `id` bigint NOT NULL,
  `live_id` bigint NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cover_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_chat
-- ----------------------------
DROP TABLE IF EXISTS `super_chat`;
CREATE TABLE `super_chat`  (
  `id` int NOT NULL COMMENT 'sc表主键',
  `live_id` bigint NOT NULL COMMENT '推流房间Id',
  `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sc内容',
  `creat_time` datetime NOT NULL COMMENT '建立时间',
  `user_id` bigint NOT NULL COMMENT 'sc发起用户Id',
  `total_price` decimal(10, 2) NOT NULL COMMENT 'sc总金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for supervise_license
-- ----------------------------
DROP TABLE IF EXISTS `supervise_license`;
CREATE TABLE `supervise_license`  (
  `id` bigint NOT NULL COMMENT '直播审核主键',
  `user_id` bigint NOT NULL COMMENT '被审核用户Id',
  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `supervisor_id` bigint NULL DEFAULT NULL COMMENT '审核用户Id',
  `create_time` datetime NOT NULL COMMENT '建立时间',
  `supervise_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `status` int NOT NULL COMMENT '审核状态 0 - 未审核 1- 审核未通过 2 - 审核通过',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
