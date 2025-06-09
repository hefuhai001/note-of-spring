package com.example.deerweb.mapper;

import com.example.deerweb.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author bestwishes0203
 * @since 2024-03-23
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
