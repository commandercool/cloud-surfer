package com.github.commandercool.cloudsurfer.controller.mri.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subject {

    private String name;

    public static Subject formString(String name) {
        Subject subject = new Subject();
        subject.setName(name);
        return subject;
    }

}
