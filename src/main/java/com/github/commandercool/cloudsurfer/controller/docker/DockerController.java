package com.github.commandercool.cloudsurfer.controller.docker;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.commandercool.cloudsurfer.controller.docker.exception.ReconIsRunningAlreadyException;
import com.github.commandercool.cloudsurfer.controller.docker.service.TransactionalDockerService;
import com.github.commandercool.cloudsurfer.controller.mri.service.MriTransactionalService;
import com.github.commandercool.cloudsurfer.db.SubjectStorageService;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchSubjectException;
import com.github.commandercool.cloudsurfer.docker.DockerService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/container/v1")
public class DockerController {

    private final TransactionalDockerService transactionalDockerService;
    private final SubjectStorageService subjectStorageService;
    private final MriTransactionalService mriService;
    private final DockerService dockerService;

    @RequestMapping(path = "/run", method = RequestMethod.POST)
    public ResponseEntity<String> runReconAll(@RequestParam("subj") String subject) {
        try {
            transactionalDockerService.runReconAll(subject);
            return ResponseEntity.ok("");
        } catch (NoSuchSubjectException | ReconIsRunningAlreadyException noSuchSubjectException) {
            noSuchSubjectException.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(noSuchSubjectException.getMessage());
        }
    }

    @RequestMapping(path = "/download/aseg2table", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity downloadAseg2Table(@RequestParam(name = "tag") String tag) throws IOException {
        final String id = dockerService.runAseg(subjectStorageService.fetchSubjectsByTag(tag), tag);
        while (dockerService.isRunning(id)) {
            // TODO: limit here
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/plain"))
                .body(new InputStreamResource(mriService.downloadAsegTable(tag)));
    }

    @RequestMapping(path = "/download/aseg2table/preview", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> previewAseg2Table(@RequestParam(name = "tag") String tag)
            throws IOException {
        final String id = dockerService.runAseg(subjectStorageService.fetchSubjectsByTag(tag), tag);
        while (dockerService.isRunning(id)) {
            // TODO: limit here
        }
        return ResponseEntity.ok(mriService.downloadAsegTablePreview(tag));
    }

}
