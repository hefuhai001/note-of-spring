package com.example.demo.config;

import cn.dev33.satoken.stp.StpInterface;
import com.example.demo.mapper.RolePermissionMapper;
import com.example.demo.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    /* 获取权限码列表 */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.valueOf(loginId.toString());
        return rolePermissionMapper.listPermissionByUserId(userId);
    }

    /* 获取角色列表 */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.valueOf(loginId.toString());
        return userRoleMapper.listRoleByUserId(userId);
    }

    //    @Override
    //    public List<String> getPermissionList(Object loginId, String loginType) {
    //        ArrayList<String> list = new ArrayList<>();
    //        list.add("user.add");
    //        list.add("user.delete");
    //        list.add("user.update");
    //        list.add("user.get");
    //        return list;
    //    }
    //
    //    @Override
    //    public List<String> getRoleList(Object loginId, String loginType) {
    //        ArrayList<String> list = new ArrayList<>();
    //        list.add("admin");
    //        list.add("user");
    //        return list;
    //    }


}