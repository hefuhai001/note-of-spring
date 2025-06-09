package com.example.deerweb.entity;

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
 * 用户表
 * </p>
 *
 * @author bestwishes0203
 * @since 2024-03-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
@Schema(name = "UserEntity", description = "$!{table.comment}")
public class UserEntity extends Model<UserEntity> {

    @TableId(value = "u_id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "姓名")
    @TableField("u_name")
    private String name;

    @Schema(description = "性别")
    @TableField("u_sex")
    private String sex;

    @Schema(description = "年龄")
    @TableField("u_age")
    private Integer age;

    @Schema(description = "身份证号")
    @TableField("u_idcard")
    private String idcard;

    @Schema(description = "头像")
    @TableField("u_pic")
    private String pic;

    @Schema(description = "账号")
    @TableField("u_acc")
    private String acc;

    @Schema(description = "密码")
    @TableField("u_pwd")
    private String pwd;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private String createTime;

    @Schema(description = "创建人")
    @TableField("create_by")
    private String createBy;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private String updateTime;

    @Schema(description = "更新人")
    @TableField("update_by")
    private String updateBy;

    @Schema(description = "状态")
    @TableField("state")
    private Integer state;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
