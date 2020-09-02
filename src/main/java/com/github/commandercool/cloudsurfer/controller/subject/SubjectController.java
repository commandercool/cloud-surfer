package com.github.commandercool.cloudsurfer.controller.subject;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.commandercool.cloudsurfer.controller.subject.model.SubjectInfo;
import com.github.commandercool.cloudsurfer.controller.utils.JsonUtils;
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
    public ResponseEntity<String> getSubjectInfo(@RequestParam("name") String subjectName) {
        try {
            SubjectInfo info = adapter.fetchInfo(subjectName);
            return ResponseEntity.ok(JsonUtils.marshall(info));
        } catch (NoSuchSubjectException noSubject) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Subject not found");
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    @RequestMapping(path = "/tags", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> saveTags(@RequestParam(name = "tags", required = false) List<String> tags,
            @RequestParam("name") String subjectName) {
        List<String> currentTags = adapter.fetchTags(subjectName);
        List<String> toDelete;
        List<String> toAdd;
        if (tags != null) {
            toDelete = currentTags.stream()
                    .filter(t -> !tags.contains(t))
                    .collect(Collectors.toList());
            toAdd = tags.stream()
                    .filter(t -> !currentTags.contains(t))
                    .collect(Collectors.toList());
        } else {
            toDelete = currentTags;
            toAdd = emptyList();
        }
        adapter.addTags(toAdd, subjectName);
        adapter.deleteTags(toDelete, subjectName);
        return ResponseEntity.ok("{ \"deleted\": " + toDelete + ", \"added\": " + toAdd + "}");
    }

    @RequestMapping(path = "/tags", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getTags() throws JsonProcessingException {
        return ResponseEntity.ok(JsonUtils.marshall(adapter.fetchTags()));
    }

}
