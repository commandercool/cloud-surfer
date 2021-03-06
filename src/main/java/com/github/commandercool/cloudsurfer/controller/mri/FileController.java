package com.github.commandercool.cloudsurfer.controller.mri;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;

import com.github.commandercool.cloudsurfer.controller.mri.model.FolderContents;
import com.github.commandercool.cloudsurfer.controller.mri.model.FolderEntry;
import com.github.commandercool.cloudsurfer.controller.mri.model.Subject;
import com.github.commandercool.cloudsurfer.controller.mri.model.SubjectList;
import com.github.commandercool.cloudsurfer.controller.mri.model.UploadResult;
import com.github.commandercool.cloudsurfer.controller.mri.service.MriTransactionalService;
import com.github.commandercool.cloudsurfer.db.SubjectStorageService;
import com.github.commandercool.cloudsurfer.filesystem.FileSystemService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/mri/v1")
public class FileController {

    private final FileSystemService fsService;
    private final SubjectStorageService subjectStorageService;
    private final MriTransactionalService mriService;

    @RequestMapping(path = "/upload", method = RequestMethod.POST,
                    consumes = "multipart/form-data")
    public ResponseEntity<UploadResult> uploadMri(@RequestParam(name = "src") String src,
            @RequestParam(name = "upload") MultipartFile mriFile) {
        try (InputStream input = mriFile.getInputStream()) {
            mriService.addSubject(src, input);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new UploadResult("Error with upload: " + e.getMessage()));
        }
        return ResponseEntity.ok()
                .body(new UploadResult("Upload successful"));
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> delete(@RequestParam(name = "name") String name) {
        try {
            mriService.deleteSubject(name);
        } catch (Exception exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
        return ResponseEntity.ok("Subject was deleted successfully");
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity downloadResult(@RequestParam(name = "name") String name) {
        try {
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/tar+gzip"))
                    .body(new InputStreamResource(mriService.downloadResut(name)));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/download/aseg", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity downloadAseg(@RequestParam(name = "name") String name) {
        try {
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/plain"))
                    .body(new InputStreamResource(mriService.downloadAseg(name)));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/subjects", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SubjectList> getSubjects() {
        SubjectList subjectList = new SubjectList();
        subjectList.setSubjects(mriService.getSubjects());
        subjectList.getSubjects().sort(Comparator.comparing(Subject::getName));
        return ResponseEntity.ok()
                .body(subjectList);
    }

    @RequestMapping(path = "/subjects/tag", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SubjectList> getSubjectsByTag(@RequestParam(name = "tag") String tag) {
        List<Subject> subjects = subjectStorageService.fetchSubjectsByTag(tag)
                .stream()
                .map(Subject::formString)
                .collect(Collectors.toList());
        SubjectList subjectList = new SubjectList();
        subjectList.setSubjects(subjects);
        return ResponseEntity.ok()
                .body(subjectList);
    }

    @RequestMapping(path = "/explore", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<FolderContents> getContents(
            @RequestParam(name = "folder", required = false, defaultValue = "") String folder) {
        List<FolderEntry> contents = fsService.getFiles(folder)
                .stream()
                .map(FolderEntry::fromPath)
                .collect(Collectors.toList());
        FolderContents folderContents = new FolderContents();
        folderContents.setEntries(contents);
        return ResponseEntity.ok().body(folderContents);
    }



}
