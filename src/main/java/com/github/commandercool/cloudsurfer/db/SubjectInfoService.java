package com.github.commandercool.cloudsurfer.db;

import static com.github.commandercool.cloudsurfer.db.tables.Subject.SUBJECT;
import static com.github.commandercool.cloudsurfer.db.tables.Tags.TAGS;
import static com.github.commandercool.cloudsurfer.security.UserHelper.getUserName;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectInfoService {

    private final DSLContext dsl;

    public Result<Record> fetchSubjectInfo(String name) {
        return dsl.select()
                .from(SUBJECT)
                .leftJoin(TAGS)
                .on(TAGS.SUBJ_ID.eq(SUBJECT.ID))
                .where(SUBJECT.NAME.eq(name))
                .and(SUBJECT.USERNAME.eq(getUserName()))
                .fetch();
    }

    public Result<Record> fetchRunningSubjectInfo() {
        return dsl.select()
                .from(SUBJECT)
                .where(SUBJECT.STATUS.eq(1))
                .fetch();
    }

    public void updateRunning(Integer id, Integer status, Integer progress) {
        dsl.update(SUBJECT)
                .set(SUBJECT.STATUS, status)
                .set(SUBJECT.PROGRESS, progress)
                .where(SUBJECT.ID.eq(id))
                .execute();
    }

    public void setRunning(String name, String container) {
        dsl.update(SUBJECT)
                .set(SUBJECT.STATUS, 1)
                .set(SUBJECT.PROGRESS, 1)
                .set(SUBJECT.CONTAINER, container)
                .where(SUBJECT.NAME.eq(name))
                .execute();
    }

}
