package com.github.commandercool.cloudsurfer.controller.user;

import static com.github.commandercool.cloudsurfer.controller.utils.JsonUtils.marshall;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.commandercool.cloudsurfer.controller.user.model.UserInfo;
import com.github.commandercool.cloudsurfer.db.UserAdapter;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchUserException;
import com.github.commandercool.cloudsurfer.filesystem.FileSystemService;
import com.github.commandercool.cloudsurfer.security.UserHelper;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/v1")
public class UserController {

    private final UserAdapter userAdapter;
    private final FileSystemService fileSystemService;

    @RequestMapping(path = "/info", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getUserInfo() throws JsonProcessingException {
        UserInfo userInfo;
        try {
            userInfo = userAdapter.getUserInfo(UserHelper.getUserName());
        } catch (NoSuchUserException e) {
            userInfo = UserHelper.getUserInfo();
            userAdapter.saveUser(userInfo.getLogin(), userInfo.getFullName());
        }
        return ResponseEntity.ok(marshall(userInfo));
    }

    @RequestMapping(path = "/license", method = RequestMethod.POST, produces = "application/json")
    public void saveLicense(@RequestParam(name = "upload") MultipartFile licenseFile) {
        try (InputStream inputStream = licenseFile.getInputStream()) {
            fileSystemService.saveLicense(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
