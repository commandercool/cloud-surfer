package com.github.commandercool.cloudsurfer.controller.mri.model;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class Subject {

    private String name;
    private Integer status;
    private List<String> tags = new ArrayList<>();

    public static Subject formString(String name) {
        Subject subject = new Subject();
        subject.setName(name);
        return subject;
    }

}
