/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 170007 (170007)
 Source Host           : localhost:5432
 Source Catalog        : db_code_run
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170007 (170007)
 File Encoding         : 65001

 Date: 20/05/2026 00:33:02
*/


-- ----------------------------
-- Type structure for difficulty_enum
-- ----------------------------
DROP TYPE IF EXISTS "public"."difficulty_enum";
CREATE TYPE "public"."difficulty_enum" AS ENUM (
  'easy',
  'medium',
  'hard'
);
ALTER TYPE "public"."difficulty_enum" OWNER TO "root";

-- ----------------------------
-- Sequence structure for gcc_task_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gcc_task_id_seq";
CREATE SEQUENCE "public"."gcc_task_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for gcc_task
-- ----------------------------
DROP TABLE IF EXISTS "public"."gcc_task";
CREATE TABLE "public"."gcc_task" (
  "id" int8 NOT NULL DEFAULT nextval('gcc_task_id_seq'::regclass),
  "exe_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "code" text COLLATE "pg_catalog"."default" NOT NULL,
  "args" json,
  "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'CREATED'::character varying,
  "result" text COLLATE "pg_catalog"."default",
  "created_at" timestamptz(6) DEFAULT now(),
  "updated_at" timestamptz(6) DEFAULT now()
)
;

-- ----------------------------
-- Function structure for trg_set_updated_at
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."trg_set_updated_at"();
CREATE OR REPLACE FUNCTION "public"."trg_set_updated_at"()
  RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."gcc_task_id_seq"
OWNED BY "public"."gcc_task"."id";
SELECT setval('"public"."gcc_task_id_seq"', 2, true);

-- ----------------------------
-- Uniques structure for table gcc_task
-- ----------------------------
ALTER TABLE "public"."gcc_task" ADD CONSTRAINT "gcc_task_exe_name_key" UNIQUE ("exe_name");

-- ----------------------------
-- Primary Key structure for table gcc_task
-- ----------------------------
ALTER TABLE "public"."gcc_task" ADD CONSTRAINT "gcc_task_pkey" PRIMARY KEY ("id");
