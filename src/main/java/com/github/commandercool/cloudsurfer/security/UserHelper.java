package com.github.commandercool.cloudsurfer.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class UserHelper {

    public static String getUserName() {
        return ((Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getClaim("preferred_username");
    }

}
