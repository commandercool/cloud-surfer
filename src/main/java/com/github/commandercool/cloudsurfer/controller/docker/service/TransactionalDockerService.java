package com.github.commandercool.cloudsurfer.controller.docker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.commandercool.cloudsurfer.db.SubjectInfoAdapter;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchSubjectException;
import com.github.commandercool.cloudsurfer.docker.DockerService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionalDockerService {

    private final DockerService dockerService;
    private final SubjectInfoAdapter adapter;

    public void runReconAll(String name) throws NoSuchSubjectException {
        String path = adapter.fetchInfo(name)
                .getPath();
        String container = dockerService.runRecon(name, path);
        adapter.setRunning(name, container);
    }

}
