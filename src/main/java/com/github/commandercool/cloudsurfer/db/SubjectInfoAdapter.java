package com.github.commandercool.cloudsurfer.db;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import com.github.commandercool.cloudsurfer.controller.subject.model.SubjectInfo;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchSubjectException;
import com.github.commandercool.cloudsurfer.db.tables.Subject;
import com.github.commandercool.cloudsurfer.db.tables.Tags;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubjectInfoAdapter {

    private final SubjectInfoService subjectInfoService;

    public List<String> fetchTags(String subjectName) {
        return subjectInfoService.fetchTags(subjectName);
    }

    public void deleteTags(List<String> tags, String subjectName) {
        subjectInfoService.deleteTags(tags, subjectName);
    }

    public void addTags(List<String> tags, String subjectName) {
        subjectInfoService.addTags(tags, subjectName);
    }

    public SubjectInfo fetchInfo(String name) throws NoSuchSubjectException {
        Result<Record> records = subjectInfoService.fetchSubjectInfo(name);
        if (records.isEmpty()) {
            throw new NoSuchSubjectException(name);
        }
        return fetchInfo(records.get(0), fetchTags(records));
    }

    private SubjectInfo fetchInfo(Record record, Set<String> tags) {
        SubjectInfo info = new SubjectInfo(record.get(Subject.SUBJECT.NAME, String.class));
        info.setStatus(record.get(Subject.SUBJECT.STATUS, Integer.class));
        info.setProgress(record.get(Subject.SUBJECT.PROGRESS, Integer.class));
        info.setPath(record.get(Subject.SUBJECT.PATH, String.class));
        info.setTags(tags);
        return info;
    }

    private Set<String> fetchTags(Result<Record> records) {
        return records.stream().map(record -> record.get(Tags.TAGS.TAG, String.class)).collect(Collectors.toSet());
    }

    public List<SubjectInfo> fetchRunningInfo() {
        Result<Record> records = subjectInfoService.fetchRunningSubjectInfo();
        return records.stream().map(r -> {
            SubjectInfo info = new SubjectInfo();
            info.setId(r.get(Subject.SUBJECT.ID, Integer.class));
            info.setName(r.get(Subject.SUBJECT.NAME, String.class));
            info.setContainer(r.get(Subject.SUBJECT.CONTAINER, String.class));
            info.setProgress(r.get(Subject.SUBJECT.PROGRESS, Integer.class));
            info.setPath(r.get(Subject.SUBJECT.PATH, String.class));
            return info;
        }).collect(Collectors.toList());
    }

    public void updateSubjectInfo(SubjectInfo info) {
        subjectInfoService.updateRunning(info.getId(), info.getStatus(), info.getProgress());
    }

    public void setRunning(String name, String container) {
        subjectInfoService.setRunning(name, container);
    }

}
