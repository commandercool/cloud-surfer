package com.github.commandercool.cloudsurfer.docker;

import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;

@Service
public class DockerService {

    public void runRecon(String subject) {
        DockerClient client = DockerClientBuilder.getInstance("tcp://localhost:2375").build();
        CreateContainerResponse containerRes = client.createContainerCmd("alerokhin/freesurfer6")
                .withBinds(new Bind("C:\\freesurfer\\license", new Volume("/usr/local/freesurfer/license")),
                        new Bind("C:\\mri\\cloud-surfer\\freesurfer\\ashurova", new Volume("/usr/local/freesurfer/subjects/ashurova")))
                .withEntrypoint("/bin/bash", "-c", "cp /usr/local/freesurfer/license/license.txt /usr/local/freesurfer/license.txt;"
                        + "export FREESURFER_HOME=/usr/local/freesurfer;"
                        + "source /usr/local/freesurfer/SetUpFreeSurfer.sh;"
                        + "echo $FREESURFER_HOME;"
                        + "recon-all -all -subjid " + subject)
                .exec();
        client.startContainerCmd(containerRes.getId())
                .exec();
    }

}
