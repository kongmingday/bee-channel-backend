/*
 Navicat Premium Data Transfer

 Source Server         : docker mysql 5.7
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.25.128:3306
 Source Schema         : bee_channel_media

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 08/06/2024 10:37:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类表主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `create_time` datetime NOT NULL COMMENT '分类创建时间',
  `create_user` bigint NOT NULL COMMENT '分类创建人员Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论表Id',
  `derive_id` bigint NOT NULL COMMENT '评论媒体来源Id  0-系统消息',
  `derive_type` int NOT NULL COMMENT '评论媒体来源类型 0-视频, 1-动态',
  `user_from_id` bigint NOT NULL COMMENT '评论用户Id',
  `user_to_id` bigint NOT NULL DEFAULT 0 COMMENT '被评论用户Id',
  `create_time` datetime NOT NULL COMMENT '评论时间',
  `has_read` int NOT NULL COMMENT '是否已读 0-未读 1-已读',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父级评论Id 0-无父级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1733844723015200794 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for community
-- ----------------------------
DROP TABLE IF EXISTS `community`;
CREATE TABLE `community`  (
  `id` bigint NOT NULL COMMENT '动态表主键',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '动态内容',
  `user_id` bigint NOT NULL COMMENT '动态发送用户',
  `with_image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '动态图片 (ps: [] or [文件编号Id])',
  `liked` int NULL DEFAULT NULL COMMENT '动态被赞数',
  `create_time` datetime NOT NULL COMMENT '动态发送时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for history
-- ----------------------------
DROP TABLE IF EXISTS `history`;
CREATE TABLE `history`  (
  `id` bigint NOT NULL COMMENT '历史记录表Id',
  `video_id` bigint NOT NULL COMMENT '浏览视频Id',
  `user_id` bigint NOT NULL COMMENT '浏览用户Id',
  `duration` bigint NOT NULL COMMENT '浏览时长毫秒值',
  `create_time` datetime NOT NULL COMMENT '初次浏览时间',
  `update_time` datetime NOT NULL COMMENT '最后浏览时间',
  `pause_point` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '暂停时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for like_list
-- ----------------------------
DROP TABLE IF EXISTS `like_list`;
CREATE TABLE `like_list`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '点赞表主键',
  `derive_type` int NOT NULL COMMENT '点赞来源类型 0-视频 2-评论',
  `derive_id` bigint NOT NULL COMMENT '点赞来源Id',
  `user_from_id` bigint NOT NULL COMMENT '点赞用户',
  `favorite_type` int NOT NULL COMMENT '0-点踩 1-点赞  ',
  `create_time` datetime NOT NULL COMMENT '点赞时间',
  `user_to_id` bigint NOT NULL DEFAULT 0 COMMENT '被点赞用户 评论必填',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 651 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for media_file
-- ----------------------------
DROP TABLE IF EXISTS `media_file`;
CREATE TABLE `media_file`  (
  `id` bigint NOT NULL COMMENT '文件表主键',
  `upload_user` bigint NOT NULL COMMENT '上传用户Id',
  `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件桶目录',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件存储路径',
  `upload_time` datetime NOT NULL COMMENT '文件上传日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for play_list
-- ----------------------------
DROP TABLE IF EXISTS `play_list`;
CREATE TABLE `play_list`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '播放列表主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '播放列表名称',
  `create_time` datetime NOT NULL COMMENT '列表创建时间',
  `create_user` bigint NOT NULL COMMENT '创建用户Id',
  `status` int NOT NULL COMMENT '列表状态 0-隐藏 1-公开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for play_video_list
-- ----------------------------
DROP TABLE IF EXISTS `play_video_list`;
CREATE TABLE `play_video_list`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '播放列表中间表主键',
  `play_list_id` bigint NOT NULL COMMENT '播放列表Id',
  `video_id` bigint NOT NULL COMMENT '视频Id',
  `create_time` datetime NOT NULL COMMENT '视频加入时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for supervise
-- ----------------------------
DROP TABLE IF EXISTS `supervise`;
CREATE TABLE `supervise`  (
  `id` bigint NOT NULL COMMENT '审核表主键',
  `video_id` bigint NOT NULL COMMENT '审核视频Id',
  `supervisor_id` bigint NULL DEFAULT NULL COMMENT '审核员Id',
  `status` int NOT NULL COMMENT '审核状态 0-尚未审核 1-审核不通过 2-审核通过',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `supervise_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `id` bigint NOT NULL COMMENT '视频表主键',
  `author_id` bigint NOT NULL COMMENT '视频作者Id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频标题',
  `introduction` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频介绍',
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频标签 ([] - 无, [\"nice job\"])',
  `status` int NOT NULL COMMENT '审核状态',
  `category_id` int NOT NULL COMMENT '视频分类Id',
  `cover_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面保存路径',
  `save_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频保存路径',
  `up_time` datetime NOT NULL COMMENT '视频上传时间',
  `public_time` datetime NOT NULL COMMENT '视频公布时间',
  `saw_time` bigint NOT NULL DEFAULT 0 COMMENT '被浏览时间',
  `clicked_count` int NOT NULL DEFAULT 0 COMMENT '被浏览数',
  `save_id` bigint NULL DEFAULT NULL COMMENT '视频关联文件Id',
  `cover_id` bigint NULL DEFAULT NULL COMMENT '封面关联文件Id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`, `author_id`, `title`, `introduction`, `tag`, `status`, `public_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
