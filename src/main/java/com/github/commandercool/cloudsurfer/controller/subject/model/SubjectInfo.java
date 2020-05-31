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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE LLL dd HH:mm:ss v uuuu")
                .withLocale(Locale.US);
        return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
    }

}
