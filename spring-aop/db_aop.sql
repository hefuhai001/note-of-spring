/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : db_aop

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 06/08/2025 22:38:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `operation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作内容',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求IP',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1, '查询用户信息', 'list', NULL, '0:0:0:0:0:0:0:1', '2025-04-04 22:50:22');
INSERT INTO `sys_oper_log` VALUES (2, '查询用户信息', 'list', '[]', '0:0:0:0:0:0:0:1', '2025-04-04 22:53:05');
INSERT INTO `sys_oper_log` VALUES (3, '查询用户信息', 'list', '[]', '0:0:0:0:0:0:0:1', '2025-04-04 23:18:41');
INSERT INTO `sys_oper_log` VALUES (4, '查询用户信息', 'list', '[]', '0:0:0:0:0:0:0:1', '2025-04-04 23:18:55');

SET FOREIGN_KEY_CHECKS = 1;
