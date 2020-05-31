package com.github.commandercool.cloudsurfer.controller.subject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.commandercool.cloudsurfer.controller.subject.model.SubjectInfo;
import com.github.commandercool.cloudsurfer.filesystem.FileSystemService;
import com.github.commandercool.cloudsurfer.filesystem.model.FsSubjectInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subject/v1")
public class SubjectController {

    private final FileSystemService fsService;

    @CrossOrigin
    @RequestMapping(path = "/info", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SubjectInfo> getSubjectInfo(@RequestParam("name") String subjectName) {
        try {
            FsSubjectInfo fsSubjectInfo = fsService.fetchSubjectInfo(subjectName);
            return ResponseEntity.ok(SubjectInfo.fromFileInfo(fsSubjectInfo));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
