/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : admin

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 05/09/2021 21:21:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sequence_table
-- ----------------------------
DROP TABLE IF EXISTS `sequence_table`;
CREATE TABLE `sequence_table`  (
  `sequence_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sequence_count` bigint(0) NOT NULL,
  PRIMARY KEY (`sequence_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sequence_table
-- ----------------------------
INSERT INTO `sequence_table` VALUES ('sys_menu', 15003);
INSERT INTO `sequence_table` VALUES ('sys_role_menu', 44);
INSERT INTO `sequence_table` VALUES ('sys_user', 9);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `PKID` bigint(0) NOT NULL COMMENT '主键',
  `TypeCode` int(0) NOT NULL COMMENT '参数类型编码',
  `ConfigName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数名称',
  `ConfigKey` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数键名',
  `ConfigValue` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '参数键值',
  `Status` tinyint(0) NULL DEFAULT NULL COMMENT '状态（0停用 1启用）',
  `CreateBy` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateBy` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `UpdateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `Remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `OrderNum` int(0) NULL DEFAULT NULL COMMENT '显示顺序',
  PRIMARY KEY (`PKID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_config_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_config_type`;
CREATE TABLE `sys_config_type`  (
  `PKID` bigint(0) NOT NULL COMMENT '主键',
  `TypeCode` int(0) NOT NULL COMMENT '类型编码',
  `Remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `CreateBy` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateBy` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `UpdateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `TypeName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型名称',
  `OrderNum` int(0) NULL DEFAULT NULL COMMENT '排序顺序',
  PRIMARY KEY (`PKID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `PKID` bigint(0) NOT NULL COMMENT '表主键',
  `MenuName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `ParentId` bigint(0) NULL DEFAULT NULL COMMENT '父菜单id',
  `OrderNum` int(0) NULL DEFAULT NULL COMMENT '显示顺序',
  `Path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由地址',
  `Component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `MenuType` tinyint(0) NULL DEFAULT NULL COMMENT '菜单类型（1目录 2菜单 3按钮）',
  `Visible` tinyint(0) NULL DEFAULT NULL COMMENT '菜单状态（0隐藏 1显示）',
  `Status` tinyint(0) NULL DEFAULT NULL COMMENT '菜单状态（0停用 1正常）',
  `Perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `Icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `CreateBy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateBy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `UpdateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`PKID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 10, 'system', NULL, 1, 1, 1, NULL, 'folder-o', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'User', 'system/user/index', 2, 1, 1, 'system:user:list', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (103, '菜单管理', 1, 4, 'Menu', 'system/menu/index', 2, 1, 1, 'system:menu:list', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (104, '角色管理', 1, 5, 'Role', 'System/Role', 2, 1, 1, 'system:role:list', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (105, '参数管理', 0, 6, 'Config', '', 1, 1, 1, '', 'folder-o', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1004, '添加用户', 100, 1, NULL, NULL, 3, 1, 1, 'system:user:add', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1005, '修改用户', 100, 2, NULL, NULL, 3, 1, 1, 'system:user:edit', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1006, '删除用户', 100, 3, NULL, NULL, 3, 1, 1, 'system:user:delete', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1007, '添加角色', 104, 1, NULL, NULL, 3, 1, 1, 'system:role:add', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1008, '编辑角色', 104, 2, NULL, NULL, 3, 1, 1, 'system:role:edit', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1009, '删除角色', 104, 3, NULL, NULL, 3, 1, 1, 'system:role:delete', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1010, '角色查询', 104, 4, NULL, NULL, 3, 1, 1, 'system:role:query', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1011, '新增权限', 103, 1, NULL, NULL, 3, 1, 1, 'system:menu:add', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1012, '编辑权限', 103, 2, NULL, NULL, 3, 1, 1, 'system:menu:edit', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1013, '删除权限', 103, 3, NULL, NULL, 3, 1, 1, 'system:menu:delete', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1019, '重置密码\r\n', 100, 4, NULL, NULL, 3, 1, 1, 'system:user:resetPwd', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1020, '用户详情', 100, 5, NULL, NULL, 3, 1, 1, 'system:user:query', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1021, '参数类型', 105, 1, 'ConfigType', 'System/Config/ConfigType', 2, 1, 1, 'config:type:list', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1022, '参数详情', 105, 2, 'ConfigDetail', 'System/ConfigDetail/ConfigDetail', 2, 1, 1, 'config:detail:list', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (10801, '菜单详情', 103, 4, NULL, NULL, 3, 1, 1, 'system:menu:query', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13401, '更新状态', 1022, 0, '', '', 3, 1, 1, 'config:detail:editStatus', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13501, '编辑角色参数权限', 104, 4, '', '', 3, 1, 1, 'system:roleConfig:edit', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13601, '查看角色参数权限', 104, 1, '', '', 3, 1, 1, 'system:roleConfig:query', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13701, '查询参数类型', 105, 0, '', '', 3, 1, 1, 'config:type:list', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13702, '添加参数类型', 1021, 1, '', '', 3, 1, 1, 'config:type:add', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13703, '删除参数类型', 1021, 2, '', '', 3, 1, 1, 'config:type:delete', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13704, '查询参数类型详情', 1021, 3, '', '', 3, 1, 1, 'config:type:query', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13705, '编辑参数类型', 1021, 4, '', '', 3, 1, 1, 'config:type:edit', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13707, '添加参数', 1022, 1, '', '', 3, 1, 1, 'config:detail:add', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13708, '查询参数详情', 1022, 2, '', '', 3, 1, 1, 'config:detail:query', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13709, '删除参数', 1022, 3, '', '', 3, 1, 1, 'config:detail:delete', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13710, '编辑参数', 1022, 4, '', '', 3, 1, 1, 'config:detail:edit', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (15002, '在线用户', 1, 0, 'OnLineUser', 'System/OnLineUser', 2, 1, 1, 'system:login:list', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (15003, '下线指定用户', 15002, 0, '', '', 3, 1, 1, 'system:login:logout', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `PKID` bigint(0) NOT NULL COMMENT '角色id',
  `RoleName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `RoleKey` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `Status` tinyint(0) NULL DEFAULT NULL COMMENT '角色状态（0停用 1正常）',
  `OrderNum` int(0) NULL DEFAULT NULL COMMENT '显示顺序',
  `CreateBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateBy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `UpdateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `Remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`PKID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级用户', 'admin', 1, 1, NULL, '2021-09-05 15:04:43', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_config_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_config_type`;
CREATE TABLE `sys_role_config_type`  (
  `PKID` bigint(0) NOT NULL,
  `RoleId` bigint(0) NULL DEFAULT NULL COMMENT '角色表主键',
  `ConfigTypeId` bigint(0) NULL DEFAULT NULL COMMENT '参数类型表主键',
  PRIMARY KEY (`PKID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `PKID` bigint(0) NOT NULL COMMENT '主键',
  `RoleId` bigint(0) NOT NULL COMMENT '角色Id',
  `MenuId` bigint(0) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`PKID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (11, 1, 1);
INSERT INTO `sys_role_menu` VALUES (12, 1, 100);
INSERT INTO `sys_role_menu` VALUES (13, 1, 103);
INSERT INTO `sys_role_menu` VALUES (14, 1, 104);
INSERT INTO `sys_role_menu` VALUES (15, 1, 105);
INSERT INTO `sys_role_menu` VALUES (16, 1, 13701);
INSERT INTO `sys_role_menu` VALUES (17, 1, 13702);
INSERT INTO `sys_role_menu` VALUES (18, 1, 13703);
INSERT INTO `sys_role_menu` VALUES (19, 1, 13704);
INSERT INTO `sys_role_menu` VALUES (20, 1, 13705);
INSERT INTO `sys_role_menu` VALUES (21, 1, 13707);
INSERT INTO `sys_role_menu` VALUES (22, 1, 13708);
INSERT INTO `sys_role_menu` VALUES (23, 1, 13709);
INSERT INTO `sys_role_menu` VALUES (24, 1, 13710);
INSERT INTO `sys_role_menu` VALUES (25, 1, 13401);
INSERT INTO `sys_role_menu` VALUES (26, 1, 13601);
INSERT INTO `sys_role_menu` VALUES (27, 1, 1004);
INSERT INTO `sys_role_menu` VALUES (28, 1, 1005);
INSERT INTO `sys_role_menu` VALUES (29, 1, 1006);
INSERT INTO `sys_role_menu` VALUES (30, 1, 1007);
INSERT INTO `sys_role_menu` VALUES (31, 1, 1008);
INSERT INTO `sys_role_menu` VALUES (32, 1, 1009);
INSERT INTO `sys_role_menu` VALUES (33, 1, 10801);
INSERT INTO `sys_role_menu` VALUES (34, 1, 1010);
INSERT INTO `sys_role_menu` VALUES (35, 1, 1011);
INSERT INTO `sys_role_menu` VALUES (36, 1, 1012);
INSERT INTO `sys_role_menu` VALUES (37, 1, 1013);
INSERT INTO `sys_role_menu` VALUES (38, 1, 1019);
INSERT INTO `sys_role_menu` VALUES (39, 1, 1020);
INSERT INTO `sys_role_menu` VALUES (40, 1, 1021);
INSERT INTO `sys_role_menu` VALUES (41, 1, 13501);
INSERT INTO `sys_role_menu` VALUES (42, 1, 1022);
INSERT INTO `sys_role_menu` VALUES (43, 1, 15002);
INSERT INTO `sys_role_menu` VALUES (44, 1, 15003);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `PKID` bigint(0) NOT NULL COMMENT '用户主键',
  `LoginName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录名',
  `UserName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `Password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `Status` tinyint(0) NOT NULL COMMENT '帐号状态（0停用 1正常）',
  `LoginIp` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近登录IP',
  `LoginTime` datetime(0) NULL DEFAULT NULL COMMENT '最近登录时间',
  `CreateBy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateBy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `UpdateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `Remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`PKID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '超级用户', '7af31d6bf7da0e8719e73e9442c839cd', 1, '127.0.0.1', '2021-09-05 21:10:41', NULL, '2021-09-05 16:33:49', NULL, NULL, '初始用户');
INSERT INTO `sys_user` VALUES (9, 'lm', '李梅', '330c368f4f7298851ca0c5497d7102fe', 1, '127.0.0.1', '2021-09-05 17:28:08', NULL, '2021-09-05 16:40:31', NULL, NULL, '普通用户');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `PKID` bigint(0) NOT NULL COMMENT '表主键',
  `UserId` bigint(0) NOT NULL COMMENT '用户主键',
  `RoleId` bigint(0) NOT NULL COMMENT '角色主键',
  PRIMARY KEY (`PKID`, `UserId`, `RoleId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
