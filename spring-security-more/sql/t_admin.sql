/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 170005 (170005)
 Source Host           : localhost:5432
 Source Catalog        : postgres
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170005 (170005)
 File Encoding         : 65001

 Date: 12/08/2025 14:30:24
*/


-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_admin";
CREATE TABLE "public"."t_admin" (
  "id" int8 NOT NULL DEFAULT nextval('t_admin_id_seq'::regclass),
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "role" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'ROLE_ADMIN'::character varying
)
;

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO "public"."t_admin" VALUES (1, 'admin', '$2a$10$Txpm.olqkF0sJA9/YV5qhumrwtt66pNgqqISLiNPJ9yqc64I0PhJG', 'ROLE_ADMIN');

-- ----------------------------
-- Uniques structure for table t_admin
-- ----------------------------
ALTER TABLE "public"."t_admin" ADD CONSTRAINT "t_admin_username_key" UNIQUE ("username");

-- ----------------------------
-- Primary Key structure for table t_admin
-- ----------------------------
ALTER TABLE "public"."t_admin" ADD CONSTRAINT "t_admin_pkey" PRIMARY KEY ("id");
