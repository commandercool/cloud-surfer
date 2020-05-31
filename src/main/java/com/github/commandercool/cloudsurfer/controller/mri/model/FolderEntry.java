package com.github.commandercool.cloudsurfer.controller.mri.model;

import java.nio.file.Files;
import java.nio.file.Path;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderEntry {

    private String name;
    private boolean isFolder;

    public static FolderEntry fromPath(Path path) {
        FolderEntry folderEntry = new FolderEntry();
        folderEntry.setName(path.getFileName().toString());
        folderEntry.setFolder(Files.isDirectory(path));
        return folderEntry;
    }

}
