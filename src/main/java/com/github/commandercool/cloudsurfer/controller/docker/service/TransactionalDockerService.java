package com.github.commandercool.cloudsurfer.controller.docker.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.commandercool.cloudsurfer.controller.docker.exception.ReconIsRunningAlreadyException;
import com.github.commandercool.cloudsurfer.controller.subject.model.SubjectInfo;
import com.github.commandercool.cloudsurfer.db.SubjectInfoAdapter;
import com.github.commandercool.cloudsurfer.db.UserAdapter;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchSubjectException;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchUserException;
import com.github.commandercool.cloudsurfer.docker.DockerService;
import com.github.commandercool.cloudsurfer.security.UserHelper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionalDockerService {

    private final DockerService dockerService;
    private final SubjectInfoAdapter adapter;
    private final UserAdapter userAdapter;

    public void runReconAll(String name)
            throws NoSuchSubjectException, ReconIsRunningAlreadyException, NoSuchUserException {
        userAdapter.checkLicense(UserHelper.getUserName());
        SubjectInfo subjectInfo = adapter.fetchInfo(name);
        if (subjectInfo.getStatus() == 1) {
            throw new ReconIsRunningAlreadyException(name);
        }
        String path = subjectInfo.getPath();
        String container = dockerService.runRecon(name, path);
        adapter.setRunning(name, container);
    }

    public void runAseg(List<String> tags) {
        // TODO: run aseg to table
    }

}
