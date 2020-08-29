package com.github.commandercool.cloudsurfer.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.github.commandercool.cloudsurfer.controller.user.model.UserInfo;

public class UserHelper {

    public static String getUserName() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt) {
            return ((Jwt) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal()).getClaim("preferred_username");
        } else {
            return (String) principal;
        }
    }

    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt) {
            Jwt jwtPrincipal = (Jwt) principal;
            userInfo.setFullName(jwtPrincipal.getClaim("name"));
            userInfo.setLogin(jwtPrincipal.getClaim("preferred_username"));
        } else {
            String stringPrincipal = (String) principal;
            userInfo.setFullName(stringPrincipal);
            userInfo.setLogin(stringPrincipal);
        }
        return userInfo;
    }

}
