package com.github.commandercool.cloudsurfer.controller.aseg.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Measure:volume
 * Left-Lateral-Ventricle
 * Left-Inf-Lat-Vent
 * Left-Cerebellum-White-Matter
 * Left-Cerebellum-Cortex
 * Left-Thalamus-Proper
 * Left-Caudate
 * Left-Putamen
 * Left-Pallidum
 * 3rd-Ventricle
 * 4th-Ventricle
 * Brain-Stem
 * Left-Hippocampus
 * Left-Amygdala
 * CSF
 * Left-Accumbens-area
 * Left-VentralDC
 * Left-vessel
 * Left-choroid-plexus
 * Right-Lateral-Ventricle
 * Right-Inf-Lat-Vent
 * Right-Cerebellum-White-Matter
 * Right-Cerebellum-Cortex
 * Right-Thalamus-Proper
 * Right-Caudate
 * Right-Putamen
 * Right-Pallidum
 * Right-Hippocampus
 * Right-Amygdala
 * Right-Accumbens-area
 * Right-VentralDC
 * Right-vessel
 * Right-choroid-plexus
 * 5th-Ventricle
 * WM-hypointensities
 * Left-WM-hypointensities
 * Right-WM-hypointensities
 * non-WM-hypointensities
 * Left-non-WM-hypointensities
 * Right-non-WM-hypointensities
 * Optic-Chiasm
 * CC_Posterior
 * CC_Mid_Posterior
 * CC_Central
 * CC_Mid_Anterior
 * CC_Anterior
 * BrainSegVol
 * BrainSegVolNotVent
 * BrainSegVolNotVentSurf
 * lhCortexVol
 * rhCortexVol
 * CortexVol
 * lhCerebralWhiteMatterVol
 * rhCerebralWhiteMatterVol
 * CerebralWhiteMatterVol
 * SubCortGrayVol
 * TotalGrayVol
 * SupraTentorialVol
 * SupraTentorialVolNotVent
 * SupraTentorialVolNotVentVox
 * MaskVol
 * BrainSegVol-to-eTIV
 * MaskVol-to-eTIV
 * lhSurfaceHoles
 * rhSurfaceHoles
 * SurfaceHoles
 * EstimatedTotalIntraCranialVol
 */
@Getter
@Setter
public class SubjectAsegStats {

    private String volume;
    private Double leftLateralVentricle;
    private Double leftInfLatVent;
    private Double leftCerebellumWhiteMatter;
    private Double LeftCerebellumCortex;
    private Double LeftThalamusProper;
    private Double LeftCaudate;
    private Double LeftPutamen;
    private Double LeftPallidum;
    private Double _3rdVentricle;
    private Double _4thVentricle;
    private Double BrainStem;
    private Double LeftHippocampus;
    private Double LeftAmygdala;
    private Double CSF;
    private Double LeftAccumbensarea;
    private Double LeftVentralDC;
    private Double Leftvessel;
    private Double Leftchoroidplexus;
    private Double RightLateralVentricle;
    private Double RightInfLatVent;
    private Double RightCerebellumWhiteMatter;
    private Double RightCerebellumCortex;
    private Double RightThalamusProper;
    private Double RightCaudate;
    private Double RightPutamen;
    private Double RightPallidum;
    private Double RightHippocampus;
    private Double RightAmygdala;
    private Double RightAccumbensarea;
    private Double RightVentralDC;
    private Double Rightvessel;
    private Double Rightchoroidplexus;
    private Double _5thVentricle;
    private Double WMhypointensities;
    private Double LeftWMhypointensities;
    private Double RightWMhypointensities;
    private Double nonWMhypointensities;
    private Double LeftnonWMhypointensities;
    private Double RightnonWMhypointensities;
    private Double OpticChiasm;
    private Double CC_Posterior;
    private Double CC_Mid_Posterior;
    private Double CC_Central;
    private Double CC_Mid_Anterior;
    private Double CC_Anterior;
    private Double BrainSegVol;
    private Double BrainSegVolNotVent;
    private Double BrainSegVolNotVentSurf;
    private Double lhCortexVol;
    private Double rhCortexVol;
    private Double CortexVol;
    private Double lhCerebralWhiteMatterVol;
    private Double rhCerebralWhiteMatterVol;
    private Double CerebralWhiteMatterVol;
    private Double SubCortGrayVol;
    private Double TotalGrayVol;
    private Double SupraTentorialVol;
    private Double SupraTentorialVolNotVent;
    private Double SupraTentorialVolNotVentVox;
    private Double MaskVol;
    private Double BrainSegVoltoeTIV;
    private Double MaskVoltoeTIV;
    private Double lhSurfaceHoles;
    private Double rhSurfaceHoles;
    private Double SurfaceHoles;
    private Double EstimatedTotalIntraCranialVol;

}
