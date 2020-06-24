package com.github.commandercool.cloudsurfer.controller.docker;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.commandercool.cloudsurfer.controller.docker.service.TransactionalDockerService;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchSubjectException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/container/v1")
public class DockerController {

    private final TransactionalDockerService dockerService;

    @CrossOrigin
    @RequestMapping(path = "/run", method = RequestMethod.POST)
    public void runReconAll(@RequestParam("subj") String subject) throws NoSuchSubjectException {
        dockerService.runReconAll(subject);
    }

}
