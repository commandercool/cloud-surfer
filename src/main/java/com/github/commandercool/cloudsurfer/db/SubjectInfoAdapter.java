package com.github.commandercool.cloudsurfer.db;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import com.github.commandercool.cloudsurfer.controller.subject.model.SubjectInfo;
import com.github.commandercool.cloudsurfer.db.tables.Subject;
import com.github.commandercool.cloudsurfer.db.tables.Tags;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubjectInfoAdapter {

    private final SubjectInfoService subjectInfoService;

    public SubjectInfo fetchInfo(String name) {
        Result<Record> records = subjectInfoService.fetchSubjectInfo(name);
        SubjectInfo info = new SubjectInfo(name);
        info.setStatus(records.get(0)
                .get(Subject.SUBJECT.STATUS, Integer.class));
        info.setProgress(records.get(0)
                .get(Subject.SUBJECT.PROGRESS, Integer.class));
        records.forEach(r -> {
            info.getTags()
                    .add(r.get(Tags.TAGS.TAG, String.class));
        });
        return info;
    }

    public List<SubjectInfo> fetchRunningInfo() {
        Result<Record> records = subjectInfoService.fetchRunningSubjectInfo();
        return records.stream()
                .map(r -> {
                    SubjectInfo info = new SubjectInfo();
                    info.setId(r.get(Subject.SUBJECT.ID, Integer.class));
                    info.setName(r.get(Subject.SUBJECT.NAME, String.class));
                    info.setContainer(r.get(Subject.SUBJECT.CONTAINER, String.class));
                    info.setProgress(r.get(Subject.SUBJECT.PROGRESS, Integer.class));
                    return info;
                })
                .collect(Collectors.toList());
    }

    public void updateSubjectInfo(SubjectInfo info) {
        subjectInfoService.updateRunning(info.getId(), info.getStatus(), info.getProgress());
    }

    public void setRunning(String name, String container) {
        subjectInfoService.setRunning(name, container);
    }

}
