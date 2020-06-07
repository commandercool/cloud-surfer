package com.github.commandercool.cloudsurfer.db;

import static com.github.commandercool.cloudsurfer.db.tables.Subject.SUBJECT;
import static com.github.commandercool.cloudsurfer.db.tables.Tags.TAGS;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectStorageService {

    @Autowired
    private DSLContext dsl;

    public List<String> fetchSubjectTags(String name, String username) {
        List<String> tags = dsl.select(TAGS.TAG)
                .from(TAGS)
                .join(SUBJECT)
                .on(TAGS.SUBJ_ID.eq(SUBJECT.ID))
                .where(SUBJECT.NAME.eq(name))
                .and(SUBJECT.USERNAME.eq(username))
                .fetch()
                .getValues(TAGS.TAG, String.class);
        return tags;
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
