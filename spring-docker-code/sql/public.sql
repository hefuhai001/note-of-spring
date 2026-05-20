-- 代码执行任务表
CREATE TABLE IF NOT EXISTS "public"."code_task"
(
    "id"         BIGSERIAL                                   NOT NULL PRIMARY KEY,
    "exe_name"   varchar(100) COLLATE "pg_catalog"."default" NOT NULL,  -- 可执行文件名（UUID，不含扩展名）
    "language"   varchar(32)  COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'c',  -- 编程语言标识：c/cpp/java/python/javascript/rust/dotnet/go
    "code"       text COLLATE "pg_catalog"."default"         NOT NULL,  -- 源代码内容
    "args"       text[] COLLATE "pg_catalog"."default",                 -- 命令行参数列表
    "status"     varchar(20) COLLATE "pg_catalog"."default"  NOT NULL DEFAULT 'CREATED',  -- 任务状态：CREATED/RUNNING/COMPLETED/ERROR
    "result"     text COLLATE "pg_catalog"."default",                   -- 执行输出结果
    "created_at" timestamptz(6) DEFAULT now(),                          -- 创建时间
    "updated_at" timestamptz(6) DEFAULT now()                           -- 更新时间
)
;

COMMENT ON TABLE  "public"."code_task"              IS '代码执行任务表';
COMMENT ON COLUMN "public"."code_task"."id"         IS '主键ID';
COMMENT ON COLUMN "public"."code_task"."exe_name"   IS '可执行文件名（UUID，不含扩展名）';
COMMENT ON COLUMN "public"."code_task"."language"   IS '编程语言标识：c/cpp/java/python/javascript/rust/dotnet/go';
COMMENT ON COLUMN "public"."code_task"."code"       IS '源代码内容';
COMMENT ON COLUMN "public"."code_task"."args"       IS '命令行参数列表';
COMMENT ON COLUMN "public"."code_task"."status"     IS '任务状态：CREATED/RUNNING/COMPLETED/ERROR';
COMMENT ON COLUMN "public"."code_task"."result"     IS '执行输出结果';
COMMENT ON COLUMN "public"."code_task"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."code_task"."updated_at" IS '更新时间';
