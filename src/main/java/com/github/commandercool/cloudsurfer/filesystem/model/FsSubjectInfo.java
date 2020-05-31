package com.github.commandercool.cloudsurfer.filesystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FsSubjectInfo {

    private String name;
    private boolean exists;
    private boolean running;
    private String statusLog;

}
