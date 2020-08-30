package com.github.commandercool.cloudsurfer.controller.docker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.commandercool.cloudsurfer.controller.docker.exception.ReconIsRunningAlreadyException;
import com.github.commandercool.cloudsurfer.controller.docker.service.TransactionalDockerService;
import com.github.commandercool.cloudsurfer.db.SubjectStorageService;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchSubjectException;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/container/v1")
public class DockerController {

    private final TransactionalDockerService dockerService;
    private final SubjectStorageService subjectStorageService;

    @RequestMapping(path = "/run", method = RequestMethod.POST)
    public ResponseEntity<String> runReconAll(@RequestParam("subj") String subject) {
        try {
            dockerService.runReconAll(subject);
            return ResponseEntity.ok("");
        } catch (NoSuchSubjectException | ReconIsRunningAlreadyException noSuchSubjectException) {
            noSuchSubjectException.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(noSuchSubjectException.getMessage());
        }
    }

    @RequestMapping(path = "/download/aseg2table", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity downloadAseg2Table(@RequestParam(name = "tag") String tag) {
        dockerService.runAseg(subjectStorageService.fetchSubjectsByTag(tag));
        return ResponseEntity.ok("");
    }

}
