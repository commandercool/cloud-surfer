package com.github.commandercool.cloudsurfer.db;

import static com.github.commandercool.cloudsurfer.db.tables.User.USER;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private DSLContext dsl;

    public Result<Record> getUser(String login) {
        return dsl.select()
                .from(USER)
                .where(USER.LOGIN.eq(login))
                .fetch();
    }

    public void saveUser(String login, String name) {
        dsl.insertInto(USER)
                .set(USER.LOGIN, login)
                .set(USER.NAME, name)
                .execute();
    }

}
