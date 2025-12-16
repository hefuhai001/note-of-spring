package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("t_role_permission")
@Schema(name = "RolePermissionEntity", description = "")
public class RolePermissionEntity extends Model<RolePermissionEntity> {

    //    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    //    @TableId(value = "permission_id", type = IdType.AUTO)
    private Long permissionId;

    public static final String ROLE_ID = "role_id";

    public static final String PERMISSION_ID = "permission_id";

    @Override
    public Serializable pkVal() {
        return this.permissionId;
    }
}
