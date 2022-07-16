package org.mterra.mpc.service;

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
        configureTrackQLinks();
        configureMasterVolumeQLink();
        turnQlinksOff(3, 4, 7, 8, 11, 12, 15, 15);

    }

    private void turnQlinksOff(int... qLinkIndexes) {
        Project project = getProject();
        for (int qlinkIndex : qLinkIndexes) {
            project.setQLinkAssignement(qlinkIndex, null, 0, Constants.QLINK_PARAMTER_OFF, false);
        }
    }

    public void configureQLinkMode(String qlinkMode) {
        getProject().setQLinkMode(qlinkMode);
    }

    private void configureTrackQLinks() {
        Project project = getProject();
        project.setQLinkAssignement(Constants.QLINK_INDEX_BD_VOLUME, Constants.QLINK_TYPE_TRACK, Constants.TRACK_INDEX_BD, Constants.QLINK_PARAMTER_VOLUME, false);
    }

    private void configureMasterVolumeQLink() {
        Project project = getProject();
        project.setQLinkAssignement(Constants.QLINK_INDEX_MASTER_VOLUME, Constants.QLINK_TYPE_MASTER, 0, Constants.QLINK_PARAMTER_VOLUME, false);
    }

    public void updateProjectFile(File targetDir) {
        getProject().writeDocument(targetDir);
    }

}
