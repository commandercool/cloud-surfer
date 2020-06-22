package com.github.commandercool.cloudsurfer.controller.subject.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubjectInfo {

    private static final List<String> STEPS =
            Arrays.asList("MotionCor", "Talairach ", "Talairach Failure Detection", "Nu Intensity Correction",
                    "Intensity Normalization", "Skull Stripping", "EM Registration", "CA Normalize", "CA Reg",
                    "SubCort Seg", "Merge ASeg", "Intensity Normalization2", "Mask BFS", "WM Segmentation", "Fill",
                    "Tessellate lh", "Tessellate rh", "Smooth1 lh", "Smooth1 rh", "Inflation1 lh", "Inflation1 rh",
                    "QSphere lh", "QSphere rh", "Fix Topology Copy lh", "Fix Topology Copy rh", "Fix Topology lh",
                    "Fix Topology rh", "Make White Surf lh", "Make White Surf rh", "Smooth2 lh", "Smooth2 rh",
                    "Inflation2 lh", "Inflation2 rh", "Curv .H and .K lh", "Curv .H and .K rh", "Curvature Stats lh",
                    "Curvature Stats rh", "Sphere lh", "Sphere rh", "Surf Reg lh", "Surf Reg rh", "Jacobian white lh",
                    "Jacobian white rh", "AvgCurv lh", "AvgCurv rh", "Cortical Parc lh", "Cortical Parc rh",
                    "Make Pial Surf lh", "Make Pial Surf rh", "Surf Volume lh", "Surf Volume rh",
                    "Cortical ribbon mask", "Parcellation Stats lh", "Parcellation Stats rh", "Cortical Parc 2 lh",
                    "Cortical Parc 2 rh", "Parcellation Stats 2 lh", "Parcellation Stats 2 rh", "Cortical Parc 3 lh",
                    "Cortical Parc 3 rh", "Parcellation Stats 3 lh", "Parcellation Stats 3 rh", "WM/GM Contrast lh",
                    "WM/GM Contrast rh", "Relabel Hypointensities", "AParc-to-ASeg aparc", "AParc-to-ASeg a2009s",
                    "AParc-to-ASeg DKTatlas", "APas-to-ASeg", "ASeg Stats", "WMParc", "BA_exvivo Labels lh",
                    "BA_exvivo Labels rh");

    @JsonProperty("name")
    private String name;

    @JsonProperty("reconRunning")
    private boolean reconRunning;
    /**
     * 0 - no run
     * 1 - running
     * 2 - success
     * 3 - failed
     */
    @JsonProperty("status")
    private int status;

    @JsonProperty("tags")
    private List<String> tags = new ArrayList<>();

    @JsonProperty("steps")
    private List<ReconAllStep> stepStatList = new ArrayList<>();

    @JsonIgnore
    private String container;

    @JsonIgnore
    private Integer id;

    private Integer progress;

    public SubjectInfo(String name) {
        this.name = name;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        AtomicInteger counter = new AtomicInteger(1);
        stepStatList.addAll(STEPS.stream()
                .map(s -> new ReconAllStep(s, LocalDateTime.now(), getStatus(counter.getAndIncrement(), progress)))
                .collect(Collectors.toList()));
    }

    public int getStatus(int counter, int progress) {
        if (counter < progress) {
            return 2;
        } else if (counter > progress) {
            return 0;
        } else {
            return status;
        }
    }

}
