package com.github.commandercool.cloudsurfer.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.github.commandercool.cloudsurfer.filesystem.model.FsSubjectInfo;

@Service
public class FileSystemService {

    private static final String SUBJ_DIR = "freesurfer/";
    private static final String STATUS_LOG = "/scripts/recon-all-status.log";
    private static final String RUN_LOCK = "/scripts/IsRunning.lh+rh";

    public void saveFile(String fileName, InputStream input) {
        File file = new File(SUBJ_DIR + fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream out = new FileOutputStream(file)) {
            input.transferTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FsSubjectInfo fetchSubjectInfo(String name) throws Exception {
        File file = new File(SUBJ_DIR + name);
        if (file.exists()) {
            FsSubjectInfo subInfo = new FsSubjectInfo();
            subInfo.setName(name);
            subInfo.setExists(true);
            subInfo.setStatusLog(readStatusLog(name));
            subInfo.setRunning(checkIfRunning(name));
            return subInfo;
        } else {
            throw new IllegalArgumentException("No subject with name " + name);
        }
    }

    private boolean checkIfRunning(String name) {
        return new File(SUBJ_DIR + name + RUN_LOCK).exists();
    }

    private String readStatusLog(String name) {
        try {
            return Files.readString(Paths.get(SUBJ_DIR + name + STATUS_LOG));
        } catch (IOException e) {
            return "";
        }
    }

    public List<String> getRootFolders() {
        List<String> fileList = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(SUBJ_DIR), 1)) {
            fileList = walk
                    .filter(p -> !SUBJ_DIR.startsWith(p.getFileName().toString()))
                    .filter(p -> Files.isDirectory(p))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    public List<Path> getFiles(String rootFolder) {
        rootFolder = SUBJ_DIR + rootFolder;
        List<Path> fileList = new ArrayList<>();
        try {
            fileList = Files.walk(Paths.get(rootFolder))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }

}