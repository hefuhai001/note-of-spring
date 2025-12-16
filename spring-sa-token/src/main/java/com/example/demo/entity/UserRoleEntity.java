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
@TableName("t_user_role")
@Schema(name = "UserRoleEntity", description = "")
public class UserRoleEntity extends Model<UserRoleEntity> {

    //    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    //    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    public static final String USER_ID = "user_id";

    public static final String ROLE_ID = "role_id";

    @Override
    public Serializable pkVal() {
        return this.roleId;
    }
}
