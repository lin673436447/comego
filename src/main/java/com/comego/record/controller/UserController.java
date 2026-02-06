package com.comego.record.controller;

import com.comego.record.common.result.Result;
import com.comego.record.common.util.TokenUtil;
import com.comego.record.entity.UserInfo;
import com.comego.record.service.UserService;
import com.comego.record.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestParam String phone, @RequestParam String password) {
        LoginVO loginVO = userService.login(phone, password);
        return Result.success(loginVO);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestParam String username, @RequestParam String phone, @RequestParam String password) {
        userService.register(username, phone, password);
        return Result.success();
    }

    @PostMapping("/info")
    public Result<UserInfo> getUserInfo(@RequestBody Map<String, Object> params, @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            userId = TokenUtil.getUserIdFromToken(token);
        }
        UserInfo userInfo = userService.getUserInfo(userId);
        return Result.success(userInfo);
    }

    @PostMapping("/update")
    public Result<Void> updateUserInfo(@RequestBody UserInfo userInfo, @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            userId = TokenUtil.getUserIdFromToken(token);
        }
        userInfo.setId(userId);
        userService.updateUserInfo(userInfo);
        return Result.success();
    }

    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            userId = TokenUtil.getUserIdFromToken(token);
        }
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success();
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        return Result.success();
    }
}