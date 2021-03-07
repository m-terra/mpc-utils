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
        configureTrackQLinks(true);
        configureMasterQLinks();

    }

    public void configureQLinkMode(String qlinkMode) {
        getProject().setQLinkMode(qlinkMode);
    }

    private void configureTrackQLinks(boolean usePrograms) {
        String type = usePrograms ? Constants.QLINK_TYPE_PROGRAM : Constants.QLINK_TYPE_TRACK;
        Project project = getProject();
        project.setQLinkAssignement(2, type, 3, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkAssignement(3, type, 3, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkAssignement(6, type, 2, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkAssignement(7, type, 2, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkAssignement(10, type, 1, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkAssignement(11, type, 1, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkAssignement(14, type, 0, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkAssignement(15, type, 0, Constants.QLINK_PARAMTER_MUTE, false);
    }

    private void configureMasterQLinks() {
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

        Project project = getProject();
        project.setQLinkAssignement(4, Constants.QLINK_TYPE_MASTER, 0, Constants.QLINK_PARAMTER_VOLUME, false);
        if (StringUtils.isNotBlank(paramEqHiGain)) {
            project.setQLinkAssignement(16, Constants.QLINK_TYPE_MASTER, 0, paramEqHiGain, true);
        }
        if (StringUtils.isNotBlank(paramEqHiGain)) {
            project.setQLinkAssignement(8, Constants.QLINK_TYPE_MASTER, 0, paramEqLoGain, false);
            project.setQLinkAssignement(12, Constants.QLINK_TYPE_MASTER, 0, paramEqLoGain, true);
        }
    }

    public void updateProjectFile(File targetDir) {
        getProject().writeDocument(targetDir);
    }

}
