package com.github.commandercool.cloudsurfer.docker;

import static com.github.commandercool.cloudsurfer.security.UserHelper.getUserName;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
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
                .withHostConfig(getConfig(subjectNameTrimmed, path))
                .withEntrypoint("/bin/bash", "-c",
                                "cp /usr/local/freesurfer/license/license.txt /usr/local/freesurfer/license.txt;"
                                + "export FREESURFER_HOME=/usr/local/freesurfer;"
                                + "source /usr/local/freesurfer/SetUpFreeSurfer.sh;"
                                + "echo $FREESURFER_HOME;"
                                + "cd /usr/local/freesurfer/subjects/" + subjectNameTrimmed + ";"
                                + "pwd;"
                                + "ls -l;"
                                + "mkdir mri;" + "cd mri/;" + "mkdir orig;"
                                + "mri_convert ../" + subject + " orig/001.mgz;"
                                + "cd ../;"
                                + "rm -f /usr/local/freesurfer/subjects/" + subjectNameTrimmed + "/scripts/IsRunning.lh+rh;"
                                + "rm -f /usr/local/freesurfer/subjects/" + subjectNameTrimmed + "/scripts/recon-all-status.log;"
                                + "recon-all -all -subjid " + subjectNameTrimmed)
                .exec();
        client.startContainerCmd(containerRes.getId())
                .exec();
        return containerRes.getId();
    }

    public String runAseg(List<String> subjects) {
        CreateContainerResponse containerRes = client.createContainerCmd("alerokhin/asegtools")
                .withHostConfig(getAsegConfig())
                .withEntrypoint("/bin/bash", "-c",
                        "cd /script/;"
                        + "export SUBJECTS_DIR=/subjects;"
                        + "./asegstats2table --subjects " + getSubjects(subjects) + "--tablefile /subjects/asegtable;")
                .exec();
        client.startContainerCmd(containerRes.getId())
                .exec();
        return containerRes.getId();
    }

    private String getSubjects(List<String> subjects) {
        StringBuilder paramBuilder = new StringBuilder();
        subjects.forEach(s -> {
            paramBuilder.append(s.split("\\.")[0]);
            paramBuilder.append(" ");
        });
        return paramBuilder.toString();
    }

    private HostConfig getAsegConfig() {
        final HostConfig hostConfig = new HostConfig();
        hostConfig.withBinds(new Bind("/home/ubuntu/freesurfer/" + getUserName(), new Volume("/subjects")));
        return hostConfig;
    }

    private HostConfig getConfig(String subjectNameTrimmed, String path) {
        final HostConfig hostConfig = new HostConfig();
        hostConfig.withCpuQuota(70000L);
        hostConfig.withBinds(new Bind("/home/ubuntu/freesurfer/" + getUserName() + "/license", new Volume("/usr/local/freesurfer/license")),
                new Bind("/home/ubuntu/freesurfer" + path, new Volume("/usr/local/freesurfer/subjects/" + subjectNameTrimmed)));
        return hostConfig;
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
