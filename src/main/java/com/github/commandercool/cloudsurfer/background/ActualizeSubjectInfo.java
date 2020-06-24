package com.github.commandercool.cloudsurfer.background;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.commandercool.cloudsurfer.db.SubjectInfoAdapter;
import com.github.commandercool.cloudsurfer.docker.DockerService;
import com.github.commandercool.cloudsurfer.filesystem.FileSystemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActualizeSubjectInfo {

    private final SubjectInfoAdapter adapter;
    private final DockerService dockerService;
    private final FileSystemService fs;

    @Scheduled(fixedRate = 10000)
    public void actualizeRunningTasks() {
        adapter.fetchRunningInfo()
                .forEach(i -> {
                    i.setProgress(fs.getProgress(i.getPath()));
                    if (dockerService.isRunning(i.getContainer())) {
                        i.setStatus(1);
                    } else if (dockerService.finishedSuccessfully(i.getContainer())) {
                        i.setStatus(2);
                    } else {
                        i.setStatus(3);
                    }
                    adapter.updateSubjectInfo(i);
                });
    }

}
