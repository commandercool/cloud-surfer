package com.github.commandercool.cloudsurfer.controller.subject.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.commandercool.cloudsurfer.filesystem.model.FsSubjectInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public static SubjectInfo fromFileInfo(FsSubjectInfo fsSubjectInfo) {
        SubjectInfo subjectInfo = new SubjectInfo();
        subjectInfo.setName(fsSubjectInfo.getName());
        if (!fsSubjectInfo.getStatusLog().isEmpty()) {
            String[] statusLog = fsSubjectInfo.getStatusLog()
                    .split("\n");
            List<ReconAllStep> steps = Arrays.stream(statusLog)
                    .filter(s -> s.startsWith("#@#"))
                    .map(s -> new ReconAllStep(getStepName(s), getDateTime(s), 2))
                    .collect(Collectors.toList());
            if (fsSubjectInfo.isRunning()) {
                subjectInfo.setStatus(1);
                subjectInfo.reconRunning = true;
                steps.get(steps.size() - 1).setStatus(1);
            } else {
                subjectInfo.reconRunning = false;
                if (statusLog[statusLog.length - 1].contains("exited with ERRORS")) {
                    subjectInfo.setStatus(3);
                    steps.get(steps.size() - 1).setStatus(3);
                } else {
                    subjectInfo.setStatus(2);
                    steps.get(steps.size() - 1).setStatus(2);
                }
            }
            if (steps.size() < STEPS.size()) {
                STEPS.subList(steps.size(), STEPS.size()).forEach(s -> {
                    steps.add(new ReconAllStep(s, null, 0));
                });
            }
            subjectInfo.setStepStatList(steps);
        } else {
            subjectInfo.setStatus(0);
        }
        return subjectInfo;
    }

    private static int getStatus(String full, String runningCommand, String failedCommand) {
        if (!runningCommand.isEmpty() && runningCommand.equals(full)) {
            return 1;
        } else if (!failedCommand.isEmpty() && failedCommand.equals(full)) {
            return 3;
        }
        return 2;
    }

    private static String getStepName(String full) {
        int length = full.length();
        return full.substring(4, length - 29);
    }

    private static LocalDateTime getDateTime(String full) {
        int length = full.length();
        String dateTimeStr = full.substring(length - 28, length);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE LLL  d HH:mm:ss v uuuu")
                .withLocale(Locale.US);
        return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
    }

}
