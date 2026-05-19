package asia.hfh.code.entity;

import asia.hfh.code.base.BaseEntity;
import asia.hfh.code.handler.JsonNodeTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaoHe
 * @since 2025-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "gcc_task")
@Schema(name = "TaskEntity", description = "")
public class TaskEntity extends BaseEntity<TaskEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "exe_name")
    private String exeName;

    @TableField("code")
    private String code;

    @TableField(value = "args", typeHandler = JsonNodeTypeHandler.class)
    private JsonNode args;

    @TableField("status")
    private String status;

    @TableField("result")
    private String result;

    public static final String ID = "id";

    public static final String EXE_NAME = "exe_name";

    public static final String CODE = "code";

    public static final String ARGS = "args";

    public static final String STATUS = "status";

    public static final String RESULT = "result";

    @Override
    public Serializable pkVal() {
        return this.exeName;
    }
}
