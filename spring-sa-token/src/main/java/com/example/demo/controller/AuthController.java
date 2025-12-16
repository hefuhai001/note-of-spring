package com.example.demo.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserMapper userMapper;

    @PostMapping("/login")
    public SaResult login(@RequestParam String username,
                          @RequestParam String password
    ) {
        UserEntity user = userMapper.selectOne(
                new QueryWrapper<UserEntity>().eq("username", username)
        );
        if (user == null || !user.getPassword().equals(password)) {
            return SaResult.error("用户名或密码错误");
        }
        StpUtil.login(user.getId());          // 把主键当 loginId
        return SaResult.data(StpUtil.getTokenValue());
    }

    @PostMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    @GetMapping("/isLogin")
    public SaResult isLogin() {
        return SaResult.data(StpUtil.isLogin());
    }

//    @PostMapping("/doLogin")
//    public SaResult doLogin(String username, String password) {
//        if ("123".equals(username) && "123456".equals(password)) {
//            StpUtil.login(10001);
//            return SaResult.ok("登录成功");
//        }
//        return SaResult.error("登录失败");
//    }
//
//    @GetMapping("/isLogin")
//    public SaResult isLogin() {
//        return SaResult.data(StpUtil.isLogin());
//    }
//
//    @GetMapping("/loginOut")
//    public SaResult loginOut() {
//        StpUtil.logout();
//        return SaResult.data(true);
//    }
//
//
//    @SaCheckLogin                 // ① 仅登录即可
//    @GetMapping("/list")
//    public SaResult list() {
//        return SaResult.data("用户列表");
//    }
//
//    @SaCheckPermission("user:add") // ② 需要权限码
//    @PostMapping("/add")
//    public SaResult add() {
//        return SaResult.ok("添加用户成功");
//    }
//
//    @SaCheckRole("admin")          // ③ 需要角色
//    @DeleteMapping("/delete")
//    public SaResult delete() {
//        return SaResult.ok("删除用户成功");
//    }

}
