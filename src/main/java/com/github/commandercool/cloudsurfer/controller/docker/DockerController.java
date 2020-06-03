package com.github.commandercool.cloudsurfer.controller.docker;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.commandercool.cloudsurfer.docker.DockerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/container/v1")
public class DockerController {

    private final DockerService dockerService;

    @CrossOrigin
    @RequestMapping(path = "/run", method = RequestMethod.POST)
    public void runReconAll(@RequestParam("subj") String subject) {
        dockerService.runRecon(subject);
    }

}
