package com.github.commandercool.cloudsurfer.db;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.commandercool.cloudsurfer.controller.user.model.UserInfo;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchUserException;
import com.github.commandercool.cloudsurfer.db.tables.User;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserAdapter {

    private final UserService userService;

    public UserInfo getUserInfo(String login) throws NoSuchUserException {
        Result<Record> user = userService.getUser(login);
        if (user.isEmpty()) {
            throw new NoSuchUserException();
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setLogin(user.get(0).get(User.USER.LOGIN));
        userInfo.setFullName(user.get(0).get(User.USER.NAME));
        userInfo.setLicense(user.get(0).get(User.USER.LICENSE));
        return userInfo;
    }

    public void saveUser(String login, String name) {
        userService.saveUser(login, name);
    }

}
