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
        Project project = getProject();
        project.setQLinkMode(Constants.QLINK_MODE_PROJECT);

        project.setQLinkProjectTrackAssignement(2, 3, Constants.QLINK_PARAMTER_VOLUME);
        project.setQLinkProjectTrackAssignement(3, 3, Constants.QLINK_PARAMTER_MUTE);

        project.setQLinkProjectTrackAssignement(6, 2, Constants.QLINK_PARAMTER_VOLUME);
        project.setQLinkProjectTrackAssignement(7, 2, Constants.QLINK_PARAMTER_MUTE);

        project.setQLinkProjectTrackAssignement(10, 1, Constants.QLINK_PARAMTER_VOLUME);
        project.setQLinkProjectTrackAssignement(11, 1, Constants.QLINK_PARAMTER_MUTE);

        project.setQLinkProjectTrackAssignement(14, 0, Constants.QLINK_PARAMTER_VOLUME);
        project.setQLinkProjectTrackAssignement(15, 0, Constants.QLINK_PARAMTER_MUTE);
    }

    public void updateProjectFile(File targetDir) {
        getProject().writeDocument(targetDir);
    }

}
