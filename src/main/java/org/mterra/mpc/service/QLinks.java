package org.mterra.mpc.service;

import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.util.Constants;

import java.io.File;

public class QLinks {

    private final ProjectInfo projectInfo;
    private Project project;

    public QLinks(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
    }

    private Project getProject() {
        if (project == null) {
            project = new Project();
            project.load(projectInfo);
        }
        return project;
    }

    public void configureProjectQLinks() {
        Project project = getProject();
        configureQLinkMode(project, Constants.QLINK_MODE_PROJECT);
        configureTrackQLinks(project);
        configureMasterQLinks(project);

    }

    public void configureQLinkMode(Project project, String qlinkMode) {
        project.setQLinkMode(qlinkMode);
    }

    private void configureTrackQLinks(Project project) {
        project.setQLinkProjectTrackAssignement(2, Constants.QLINK_TYPE_MIDI_TRACK, 3, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkProjectTrackAssignement(3, Constants.QLINK_TYPE_MIDI_TRACK, 3, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkProjectTrackAssignement(6, Constants.QLINK_TYPE_MIDI_TRACK, 2, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkProjectTrackAssignement(7, Constants.QLINK_TYPE_MIDI_TRACK, 2, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkProjectTrackAssignement(10, Constants.QLINK_TYPE_MIDI_TRACK, 1, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkProjectTrackAssignement(11, Constants.QLINK_TYPE_MIDI_TRACK, 1, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkProjectTrackAssignement(14, Constants.QLINK_TYPE_MIDI_TRACK, 0, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkProjectTrackAssignement(15, Constants.QLINK_TYPE_MIDI_TRACK, 0, Constants.QLINK_PARAMTER_MUTE, false);
    }

    private void configureMasterQLinks(Project project) {
        String paramEqHiGain = null;
        String paramEqLoGain = null;
        switch (project.getMasterEqInsertIndex()) {
            case "1":
                paramEqHiGain = "24593";
                paramEqLoGain = "24580";
                break;
            case "2":
                paramEqHiGain = "28689";
                paramEqLoGain = "28676";
                break;
            case "3":
                paramEqHiGain = "32785";
                paramEqLoGain = "32772";
                break;
            case "4":
                paramEqHiGain = "36881";
                paramEqLoGain = "36868";
                break;
        }

        project.setQLinkProjectTrackAssignement(4, Constants.QLINK_TYPE_MASTER, 0, Constants.QLINK_PARAMTER_VOLUME, false);
        if (StringUtils.isNotBlank(paramEqHiGain)) {
            project.setQLinkProjectTrackAssignement(16, Constants.QLINK_TYPE_MASTER, 0, paramEqHiGain, true);
        }
        if (StringUtils.isNotBlank(paramEqHiGain)) {
            project.setQLinkProjectTrackAssignement(8, Constants.QLINK_TYPE_MASTER, 0, paramEqLoGain, false);
            project.setQLinkProjectTrackAssignement(12, Constants.QLINK_TYPE_MASTER, 0, paramEqLoGain, true);
        }
    }

    public void updateProjectFile(File targetDir) {
        getProject().writeDocument(targetDir);
    }

}
