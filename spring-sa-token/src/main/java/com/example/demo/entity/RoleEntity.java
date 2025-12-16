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
@TableName("t_role")
@Schema(name = "RoleEntity", description = "")
public class RoleEntity extends Model<RoleEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("role_name")
    private String roleName;

    public static final String ID = "id";

    public static final String ROLE_NAME = "role_name";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
