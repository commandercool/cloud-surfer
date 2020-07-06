package com.github.commandercool.cloudsurfer.controller.user.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.github.commandercool.cloudsurfer.db.UserAdapter;
import com.github.commandercool.cloudsurfer.filesystem.FileSystemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAdapter userAdapter;
    private final FileSystemService fileSystemService;

    public void saveLicense(String login, InputStream licenseFile) {
        fileSystemService.saveLicense(licenseFile);
        userAdapter.saveLicense(login, login + "/license.txt");
    }

}
