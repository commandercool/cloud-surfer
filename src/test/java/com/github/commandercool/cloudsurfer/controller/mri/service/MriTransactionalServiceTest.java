package com.github.commandercool.cloudsurfer.controller.mri.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.commandercool.cloudsurfer.db.SubjectStorageService;
import com.github.commandercool.cloudsurfer.filesystem.FileSystemService;

@ExtendWith(MockitoExtension.class)
class MriTransactionalServiceTest {

    private MriTransactionalService transactionalService;
    @Mock
    private FileSystemService fileSystemService;
    @Mock
    private SubjectStorageService subjectStorageService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        transactionalService = new MriTransactionalService(fileSystemService, subjectStorageService);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn("principal");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void downloadAsegTablePreview() throws IOException {
        when(fileSystemService.downloadFile(anyString()))
                .thenReturn(this.getClass().getResourceAsStream("/asegtable"));
        final String preview = transactionalService.downloadAsegTablePreview("sometag");
        System.out.println(preview);
    }
}