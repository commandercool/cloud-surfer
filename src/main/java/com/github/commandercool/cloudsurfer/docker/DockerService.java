package com.github.commandercool.cloudsurfer.docker;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;

@Service
public class DockerService {

    private DockerClient client;

    @PostConstruct
    private void initClient() {
        try {
            client = DockerClientBuilder.getInstance("tcp://localhost:2375")
                    .build();
        } catch (Exception e) {
            System.out.println("Error creating docker client");
        }
    }

    public String runRecon(String subject, String path) {
        String subjectNameTrimmed = subject.split("\\.")[0];
        CreateContainerResponse containerRes = client.createContainerCmd("alerokhin/freesurfer6")
                .withBinds(new Bind("C:\\freesurfer\\license", new Volume("/usr/local/freesurfer/license")),
                        new Bind("C:\\mri\\cloud-surfer\\freesurfer" + path,
                                new Volume("/usr/local/freesurfer/subjects/" + subjectNameTrimmed)))
                .withEntrypoint("/bin/bash", "-c",
                                "cp /usr/local/freesurfer/license/license.txt /usr/local/freesurfer/license.txt;"
                                + "export FREESURFER_HOME=/usr/local/freesurfer;"
                                + "source /usr/local/freesurfer/SetUpFreeSurfer.sh;"
                                + "echo $FREESURFER_HOME;"
                                + "cd /usr/local/freesurfer/subjects/" + subjectNameTrimmed + ";"
                                        + "pwd;"
                                + "ls -l;"
                                + "mkdir mri mri/orig;"
                                + "mri_convert " + subject + " mri/orig/001.mgz;"
                                + "rm -f /usr/local/freesurfer/subjects/" + subjectNameTrimmed + "/scripts/IsRunning.lh+rh;"
                                + "rm -f /usr/local/freesurfer/subjects/" + subjectNameTrimmed + "/scripts/recon-all-status.log;"
                                + "recon-all -all -subjid " + subjectNameTrimmed)
                .exec();
        client.startContainerCmd(containerRes.getId())
                .exec();
        return containerRes.getId();
    }

    public boolean isRunning(String id) {
        boolean running = false;
        try {
            running = client.inspectContainerCmd(String.valueOf(id))
                    .exec()
                    .getState()
                    .getRunning();
        } catch (Exception e) {
            // no op
        }
        return running;
    }

    public boolean finishedSuccessfully(String id) {
        boolean success = false;
        try {
            Long code = client.inspectContainerCmd(String.valueOf(id))
                    .exec()
                    .getState()
                    .getExitCodeLong();
            success = code == 0;
        } catch (Exception e) {
            // no op
        }
        return success;
    }

}
