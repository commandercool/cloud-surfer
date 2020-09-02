package com.github.commandercool.cloudsurfer.controller.docker;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.commandercool.cloudsurfer.controller.docker.exception.ReconIsRunningAlreadyException;
import com.github.commandercool.cloudsurfer.controller.docker.service.TransactionalDockerService;
import com.github.commandercool.cloudsurfer.controller.mri.service.MriTransactionalService;
import com.github.commandercool.cloudsurfer.db.SubjectStorageService;
import com.github.commandercool.cloudsurfer.db.exceptions.NoSuchSubjectException;
import com.github.commandercool.cloudsurfer.docker.DockerService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/container/v1")
public class DockerController {

    private final TransactionalDockerService transactionalDockerService;
    private final SubjectStorageService subjectStorageService;
    private final MriTransactionalService mriService;
    private final DockerService dockerService;

    @RequestMapping(path = "/run", method = RequestMethod.POST)
    public ResponseEntity<String> runReconAll(@RequestParam("subj") String subject) {
        try {
            transactionalDockerService.runReconAll(subject);
            return ResponseEntity.ok("");
        } catch (NoSuchSubjectException | ReconIsRunningAlreadyException noSuchSubjectException) {
            noSuchSubjectException.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(noSuchSubjectException.getMessage());
        }
    }

    @RequestMapping(path = "/download/aseg2table", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity downloadAseg2Table(@RequestParam(name = "tag") String tag) throws IOException {
        final String id = dockerService.runAseg(subjectStorageService.fetchSubjectsByTag(tag), tag);
        while (dockerService.isRunning(id)) {
            // TODO: limit here
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/plain"))
                .body(new InputStreamResource(mriService.downloadAsegTable(tag)));
    }

    @RequestMapping(path = "/download/aseg2table/preview", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> previewAseg2Table(@RequestParam(name = "tag") String tag)
            throws IOException {
//        if (tag.equals("cloudsurfer")) {
//            return ResponseEntity
//                    .ok("{\"stats\":[{\"volume\":\"PERLE_D_E\",\"leftLateralVentricle\":5268.3,\"leftInfLatVent\":235.7,\"leftCerebellumWhiteMatter\":8266.2,\"_3rdVentricle\":700.5,\"_4thVentricle\":1603.0,\"_5thVentricle\":0.0,\"nonWMhypointensities\":0.0,\"lhCortexVol\":252208.555302,\"rhCortexVol\":253440.367735,\"lhCerebralWhiteMatterVol\":146760.614996,\"rhCerebralWhiteMatterVol\":148494.184865,\"lhSurfaceHoles\":18.0,\"rhSurfaceHoles\":25.0,\"leftCerebellumCortex\":47104.2,\"leftThalamusProper\":5924.4,\"leftAccumbensarea\":479.6,\"leftchoroidplexus\":124.3,\"rightLateralVentricle\":5631.6,\"leftnonWMhypointensities\":0.0,\"rightAccumbensarea\":488.3,\"wmhypointensities\":1282.5,\"rightnonWMhypointensities\":0.0,\"cerebralWhiteMatterVol\":295254.799861,\"rightWMhypointensities\":0.0,\"rightHippocampus\":2377.8,\"estimatedTotalIntraCranialVol\":1111086.92317,\"rightCerebellumCortex\":48969.5,\"supraTentorialVolNotVent\":846644.722899,\"rightCerebellumWhiteMatter\":8407.8,\"brainSegVolNotVentSurf\":956893.722899,\"rightchoroidplexus\":225.9,\"rightThalamusProper\":5660.5,\"supraTentorialVolNotVentVox\":845597.0,\"supraTentorialVol\":858226.722899,\"leftWMhypointensities\":0.0,\"cc_Mid_Posterior\":413.0,\"brainSegVolNotVent\":957516.0,\"brainSegVoltoeTIV\":0.874738,\"leftCaudate\":2751.2,\"brainStem\":12715.8,\"leftPutamen\":4492.9,\"leftPallidum\":1366.1,\"cc_Mid_Anterior\":417.6,\"cc_Anterior\":830.1,\"cortexVol\":505648.923037,\"brainSegVol\":971910.0,\"rightInfLatVent\":526.3,\"rightCaudate\":3032.6,\"rightVentralDC\":2735.9,\"leftAmygdala\":1091.2,\"rightPutamen\":4378.4,\"leftHippocampus\":2660.4,\"csf\":694.7,\"rightvessel\":14.0,\"rightPallidum\":1385.5,\"leftvessel\":32.6,\"opticChiasm\":156.5,\"cc_Posterior\":768.7,\"rightAmygdala\":1274.4,\"leftVentralDC\":2680.5,\"cc_Central\":351.1,\"totalGrayVol\":645452.923037,\"subCortGrayVol\":44162.0,\"surfaceHoles\":43.0,\"maskVol\":1239940.0,\"maskVoltoeTIV\":1.11597},{\"volume\":\"DZHALILOVA_A__EH\",\"leftLateralVentricle\":4249.9,\"leftInfLatVent\":510.2,\"leftCerebellumWhiteMatter\":7085.4,\"_3rdVentricle\":772.3,\"_4thVentricle\":1067.4,\"_5thVentricle\":0.0,\"nonWMhypointensities\":0.0,\"lhCortexVol\":211361.540398,\"rhCortexVol\":207679.173983,\"lhCerebralWhiteMatterVol\":104907.634769,\"rhCerebralWhiteMatterVol\":105684.928346,\"lhSurfaceHoles\":36.0,\"rhSurfaceHoles\":52.0,\"leftCerebellumCortex\":45059.7,\"leftThalamusProper\":5159.6,\"leftAccumbensarea\":401.2,\"leftchoroidplexus\":289.9,\"rightLateralVentricle\":3975.3,\"leftnonWMhypointensities\":0.0,\"rightAccumbensarea\":441.0,\"wmhypointensities\":1926.9,\"rightnonWMhypointensities\":0.0,\"cerebralWhiteMatterVol\":210592.563115,\"rightWMhypointensities\":0.0,\"rightHippocampus\":2592.4,\"estimatedTotalIntraCranialVol\":963410.9972,\"rightCerebellumCortex\":44442.8,\"supraTentorialVolNotVent\":668519.277496,\"rightCerebellumWhiteMatter\":7301.8,\"brainSegVolNotVentSurf\":770036.277496,\"rightchoroidplexus\":311.7,\"rightThalamusProper\":5215.2,\"supraTentorialVolNotVentVox\":667182.0,\"supraTentorialVol\":677846.277496,\"leftWMhypointensities\":0.0,\"cc_Mid_Posterior\":215.8,\"brainSegVolNotVent\":770870.0,\"brainSegVoltoeTIV\":0.812177,\"leftCaudate\":2468.6,\"brainStem\":11501.2,\"leftPutamen\":2516.8,\"leftPallidum\":752.7,\"cc_Mid_Anterior\":301.1,\"cc_Anterior\":362.9,\"cortexVol\":419040.714381,\"brainSegVol\":782460.0,\"rightInfLatVent\":505.9,\"rightCaudate\":2496.9,\"rightVentralDC\":2700.4,\"leftAmygdala\":607.8,\"rightPutamen\":2792.9,\"leftHippocampus\":2519.0,\"csf\":610.6,\"rightvessel\":2.3,\"rightPallidum\":1240.6,\"leftvessel\":0.0,\"opticChiasm\":131.8,\"cc_Posterior\":424.1,\"rightAmygdala\":906.9,\"leftVentralDC\":2504.0,\"cc_Central\":280.5,\"totalGrayVol\":545516.714381,\"subCortGrayVol\":36825.0,\"surfaceHoles\":88.0,\"maskVol\":1071509.0,\"maskVoltoeTIV\":1.112203}]}");
//        }
        final String id = dockerService.runAseg(subjectStorageService.fetchSubjectsByTag(tag), tag);
        while (dockerService.isRunning(id)) {
            // TODO: limit here
        }
        return ResponseEntity.ok(mriService.downloadAsegTablePreview(tag));
    }

}
