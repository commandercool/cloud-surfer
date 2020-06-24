package com.github.commandercool.cloudsurfer.db;

import static com.github.commandercool.cloudsurfer.db.tables.Subject.SUBJECT;
import static com.github.commandercool.cloudsurfer.db.tables.Tags.TAGS;
import static com.github.commandercool.cloudsurfer.security.UserHelper.getUserName;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubjectStorageService {

    @Autowired
    private DSLContext dsl;

    public void addSubject(String name, String path) {
        dsl.insertInto(SUBJECT)
                .set(SUBJECT.NAME, name)
                .set(SUBJECT.USERNAME, getUserName())
                .set(SUBJECT.PATH, path)
                .execute();
    }

    public Result<Record> fetchSubjects() {
        return dsl.select()
                .from(SUBJECT)
                .leftJoin(TAGS)
                .on(TAGS.SUBJ_ID.eq(SUBJECT.ID))
                .fetch();
    }

    public List<String> fetchSubjectTags(String name, String username) {
        return dsl.select(TAGS.TAG)
                .from(TAGS)
                .join(SUBJECT)
                .on(TAGS.SUBJ_ID.eq(SUBJECT.ID))
                .where(SUBJECT.NAME.eq(name))
                .and(SUBJECT.USERNAME.eq(username))
                .fetch()
                .getValues(TAGS.TAG, String.class);
    }

    public List<String> fetchSubjectsByTag(String tag) {
        return dsl.select(SUBJECT.NAME)
                .from(SUBJECT)
                .join(TAGS)
                .on(TAGS.SUBJ_ID.eq(SUBJECT.ID))
                .where(TAGS.TAG.eq(tag))
                .fetch()
                .getValues(SUBJECT.NAME, String.class);
    }

}
