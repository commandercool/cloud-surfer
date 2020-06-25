package com.github.commandercool.cloudsurfer.controller.docker.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReconIsRunningAlreadyException extends Exception {

    private final String subjectName;

    @Override
    public String getMessage() {
        return "Recon-all is already running for " + subjectName;
    }

}
