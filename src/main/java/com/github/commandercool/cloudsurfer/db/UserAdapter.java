package com.github.commandercool.cloudsurfer.db;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.commandercool.cloudsurfer.controller.user.model.UserInfo;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchUserException;
import com.github.commandercool.cloudsurfer.db.exceptions.UserIsNotLicensedException;
import com.github.commandercool.cloudsurfer.db.tables.User;
import com.github.commandercool.cloudsurfer.security.UserHelper;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserAdapter {

    private final DbUserService dbUserService;

    public void checkLicense(String login) throws NoSuchUserException {
        final UserInfo userInfo = getUserInfo(UserHelper.getUserName());
        if (StringUtils.isEmpty(userInfo.getLicense())) {
            throw new UserIsNotLicensedException("No license is specified for user");
        }
    }

    public UserInfo getUserInfo(String login) throws NoSuchUserException {
        Result<Record> user = dbUserService.getUser(login);
        if (user.isEmpty()) {
            throw new NoSuchUserException("No user with name " + login);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setLogin(user.get(0).get(User.USER.LOGIN));
        userInfo.setFullName(user.get(0).get(User.USER.NAME));
        userInfo.setLicense(user.get(0).get(User.USER.LICENSE));
        return userInfo;
    }

    public void saveUser(String login, String name) {
        dbUserService.saveUser(login, name);
    }

    public void saveLicense(String login, String license) {
        dbUserService.saveLicense(login, license);
    }

}
