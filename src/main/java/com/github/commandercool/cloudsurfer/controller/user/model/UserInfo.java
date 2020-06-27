package com.github.commandercool.cloudsurfer.controller.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {

    @JsonProperty("login")
    private String login;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("license")
    private String license;

}
