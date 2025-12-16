package com.example.demo.mapper;

import com.example.demo.entity.UserRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xiaoHe
 * @since 2025-09-30
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {

    @Select("SELECT r.role_name FROM t_role r " +
            "JOIN t_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> listRoleByUserId(@Param("userId") Long userId);

}
