package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
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
 * @since 2025-09-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_permission")
@Schema(name = "PermissionEntity", description = "")
public class PermissionEntity extends Model<PermissionEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "例如 user:add")
    @TableField("permission_code")
    private String permissionCode;

    public static final String ID = "id";

    public static final String PERMISSION_CODE = "permission_code";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
