package com.github.commandercool.cloudsurfer.db;

import static com.github.commandercool.cloudsurfer.db.tables.Subject.SUBJECT;
import static com.github.commandercool.cloudsurfer.db.tables.Tags.TAGS;
import static com.github.commandercool.cloudsurfer.security.UserHelper.getUserName;

import java.util.List;

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

    public void deleteTags(List<String> tags, String subjectName) {
        Integer subjId = fetchSubjectInfo(subjectName).get(0)
                .get(SUBJECT.ID, Integer.class);
        dsl.deleteFrom(TAGS)
                .where(TAGS.SUBJ_ID.eq(subjId))
                .and(TAGS.TAG.in(tags))
                .execute();
    }

    public void addTags(List<String> tags, String subjectName) {
        Integer subjId = fetchSubjectInfo(subjectName).get(0)
                .get(SUBJECT.ID, Integer.class);
        tags.forEach(tag -> {
            dsl.insertInto(TAGS)
                    .set(TAGS.TAG, tag)
                    .set(TAGS.SUBJ_ID, subjId)
                    .execute();
        });
    }

    public List<String> fetchTags(String subjectName) {
        return dsl.select(TAGS.TAG)
                .from(TAGS)
                .join(SUBJECT)
                .on(TAGS.SUBJ_ID.eq(SUBJECT.ID))
                .where(SUBJECT.NAME.eq(subjectName))
                .fetch(TAGS.TAG);
    }

    public List<String> fetchTags() {
        return dsl.selectDistinct(TAGS.TAG)
                .from(TAGS)
                .join(SUBJECT)
                .on(TAGS.SUBJ_ID.eq(SUBJECT.ID))
                .where(SUBJECT.USERNAME.eq(getUserName()))
                .fetch(TAGS.TAG);
    }

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
