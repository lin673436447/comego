package com.comego.record.vo;

import java.io.Serializable;

public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;

    private UserVO user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
}