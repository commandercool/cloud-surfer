package com.github.commandercool.cloudsurfer.controller.aseg.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectAsegStats {

    private String subjectName;
    private List<Double> stats = new ArrayList<>();

}
