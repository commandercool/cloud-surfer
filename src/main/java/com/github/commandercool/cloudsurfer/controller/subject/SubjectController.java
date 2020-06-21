package com.github.commandercool.cloudsurfer.controller.subject;

import static com.github.commandercool.cloudsurfer.security.UserHelper.getUserName;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.commandercool.cloudsurfer.controller.subject.model.SubjectInfo;
import com.github.commandercool.cloudsurfer.db.SubjectInfoAdapter;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchSubjectException;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/subject/v1")
public class SubjectController {

    private final SubjectInfoAdapter adapter;

    @RequestMapping(path = "/info", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SubjectInfo> getSubjectInfo(@RequestParam("name") String subjectName) {
        System.out.println("User name is: " + getUserName());
        try {
            SubjectInfo info = adapter.fetchInfo(subjectName);
            return ResponseEntity.ok(info);
        } catch (NoSuchSubjectException noSubject) {
            return ResponseEntity.badRequest().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
