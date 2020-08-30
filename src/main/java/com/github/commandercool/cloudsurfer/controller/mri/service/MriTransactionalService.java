package com.github.commandercool.cloudsurfer.controller.mri.service;

import static com.github.commandercool.cloudsurfer.db.Tables.TAGS;
import static com.github.commandercool.cloudsurfer.db.tables.Subject.SUBJECT;
import static com.github.commandercool.cloudsurfer.filesystem.FileSystemService.ASEG_PATH;
import static com.github.commandercool.cloudsurfer.security.UserHelper.getUserName;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.commandercool.cloudsurfer.controller.mri.model.Subject;
import com.github.commandercool.cloudsurfer.db.SubjectStorageService;
import com.github.commandercool.cloudsurfer.filesystem.FileSystemService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MriTransactionalService {

    private final FileSystemService fsService;
    private final SubjectStorageService subjectStorageService;

    public InputStream downloadResut(String name) throws IOException {
        String fileName = getPath(name) + ".tar.gz";
        return fsService.downloadFile(fileName);
    }

    public InputStream downloadAseg(String name) throws IOException {
        String fileName = getPath(name) + ASEG_PATH;
        return fsService.downloadFile(fileName);
    }

    public InputStream downloadAsegTable(String tag) throws IOException {
        String fileName = "/" + getUserName() + "/" + tag;
        return fsService.downloadFile(fileName);
    }

    public void addSubject(String name, InputStream mriInput) {
        String path = getPath(name);
        subjectStorageService.addSubject(name, path);
        fsService.saveFile(path + "/" + name, mriInput);
    }

    public void deleteSubject(String name) {
        fsService.deleteFolder(getPath(name));
        subjectStorageService.removeSubject(name);
    }

    public List<Subject> getSubjects() {
        Map<String, Subject> subjectMap = new HashMap<>();
        subjectStorageService.fetchSubjects()
                .forEach(s -> {
                    String name = s.get(SUBJECT.NAME, String.class);
                    Subject subject = subjectMap.computeIfAbsent(name, k -> new Subject());
                    subject.setName(name);
                    subject.setStatus(s.get(SUBJECT.STATUS, Integer.class));
                    subject.getTags().add(s.get(TAGS.TAG, String.class));
                });
        return new ArrayList<>(subjectMap.values());
    }

    private String getPath(String name) {
        return "/" + getUserName() + "/" + name.split("\\.")[0];
    }

}
