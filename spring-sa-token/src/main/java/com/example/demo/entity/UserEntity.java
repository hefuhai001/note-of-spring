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
@TableName("t_user")
@Schema(name = "UserEntity", description = "")
public class UserEntity extends Model<UserEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    public static final String ID = "id";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
