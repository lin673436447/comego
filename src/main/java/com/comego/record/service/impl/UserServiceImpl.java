package com.comego.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.comego.record.common.exception.BusinessException;
import com.comego.record.entity.UserInfo;
import com.comego.record.mapper.UserInfoMapper;
import com.comego.record.service.UserService;
import com.comego.record.vo.LoginVO;
import com.comego.record.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public LoginVO login(String phone, String password) {
        if (phone == null || password == null) {
            throw new BusinessException("手机号和密码不能为空");
        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.eq("is_deleted", 0);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }

        if (!userInfo.getPassword().equals(encryptPassword(password))) {
            throw new BusinessException("密码错误");
        }

        LoginVO loginVO = new LoginVO();
        loginVO.setToken("token_" + userInfo.getId() + "_" + System.currentTimeMillis());

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userInfo, userVO);
        loginVO.setUser(userVO);

        return loginVO;
    }

    @Override
    public void register(String username, String phone, String password) {
        if (username == null || phone == null || password == null) {
            throw new BusinessException("用户名、手机号和密码不能为空");
        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.eq("is_deleted", 0);
        if (userInfoMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("手机号已被注册");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPhone(phone);
        userInfo.setPassword(encryptPassword(password));
        userInfo.setMemberLevel(1);
        userInfo.setIsActive(1);
        userInfo.setCreatedAt(LocalDateTime.now());
        userInfo.setUpdatedAt(LocalDateTime.now());
        userInfo.setIsDeleted(0);

        userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfo getUserInfo(Long userId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq("id", userId);
        }
        queryWrapper.eq("is_deleted", 0);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        userInfo.setUpdatedAt(LocalDateTime.now());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        if (oldPassword == null || newPassword == null) {
            throw new BusinessException("密码不能为空");
        }

        UserInfo userInfo = getUserInfo(userId);
        if (!userInfo.getPassword().equals(encryptPassword(oldPassword))) {
            throw new BusinessException("原密码错误");
        }

        userInfo.setPassword(encryptPassword(newPassword));
        userInfo.setUpdatedAt(LocalDateTime.now());
        userInfoMapper.updateById(userInfo);
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("密码加密失败");
        }
    }
}