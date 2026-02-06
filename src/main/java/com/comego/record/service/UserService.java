package com.comego.record.service;

import com.comego.record.entity.UserInfo;
import com.comego.record.vo.LoginVO;

public interface UserService {

    LoginVO login(String phone, String password);

    void register(String username, String phone, String password);

    UserInfo getUserInfo(Long userId);

    void updateUserInfo(UserInfo userInfo);

    void changePassword(Long userId, String oldPassword, String newPassword);
}