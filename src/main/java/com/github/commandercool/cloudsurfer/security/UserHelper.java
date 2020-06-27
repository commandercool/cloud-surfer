package com.github.commandercool.cloudsurfer.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.github.commandercool.cloudsurfer.controller.user.model.UserInfo;

public class UserHelper {

    public static String getUserName() {
        return ((Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getClaim("preferred_username");
    }

    public static UserInfo getUserInfo() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        UserInfo userInfo = new UserInfo();
        userInfo.setFullName(principal.getClaim("name"));
        userInfo.setLogin(principal.getClaim("preferred_username"));
        return userInfo;
    }

}
