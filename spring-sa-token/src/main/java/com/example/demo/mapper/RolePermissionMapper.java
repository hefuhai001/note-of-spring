package com.example.demo.mapper;

import com.example.demo.entity.RolePermissionEntity;
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
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {

    @Select("SELECT DISTINCT p.permission_code FROM t_permission p " +
            "JOIN t_role_permission rp ON p.id = rp.permission_id " +
            "JOIN t_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> listPermissionByUserId(@Param("userId") Long userId);

}
