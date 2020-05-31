package com.github.commandercool.cloudsurfer.controller.mri;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.commandercool.cloudsurfer.controller.mri.model.FolderContents;
import com.github.commandercool.cloudsurfer.controller.mri.model.FolderEntry;
import com.github.commandercool.cloudsurfer.controller.mri.model.Subject;
import com.github.commandercool.cloudsurfer.controller.mri.model.SubjectList;
import com.github.commandercool.cloudsurfer.controller.mri.model.UploadResult;
import com.github.commandercool.cloudsurfer.filesystem.FileSystemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mri/v1")
public class FileController {

    private final FileSystemService fsService;

    @CrossOrigin
    @RequestMapping(path = "/upload", method = RequestMethod.POST,
                    consumes = "multipart/form-data")
    ResponseEntity<UploadResult> uploadMri(@RequestParam(name = "src") String src,
            @RequestParam(name = "subj") String subj, @RequestParam(name = "upload") MultipartFile mriFile) {
        try (InputStream input = mriFile.getInputStream()) {
            fsService.saveFile(subj + "/" + src, input);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new UploadResult("Error with upload: " + e.getMessage()));
        }
        return ResponseEntity
                .ok()
                .body(new UploadResult("Upload successful"));
    }

    @RequestMapping(path = "/subjects", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<SubjectList> getSubjects() {
        List<Subject> subjects = fsService.getRootFolders()
                .stream()
                .map(Subject::formString)
                .collect(Collectors.toList());
        SubjectList subjectList = new SubjectList();
        subjectList.setSubjects(subjects);
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
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
        return ResponseEntity.ok().header("Access-Control-Allow-Origin", "*").body(folderContents);
    }



}
