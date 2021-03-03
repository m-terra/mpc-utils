package org.mterra.mpc.service;

import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;

import java.io.File;

public class Liveset {

    public static final String QLINK_MODE_PROJECT = "Project";
    private ProjectInfo projectInfo;
    private Project project;

    public Liveset(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
    }

    private Project getProject() {
        if (project == null) {
            project = new Project();
            project.load(projectInfo);
        }
        return project;
    }

    public void configureLivesetQLinks() {
        Project project = getProject();
        project.setQLinkMode(QLINK_MODE_PROJECT);
    }

    public void updateProjectFile(File targetDir) {
        getProject().writeDocument(targetDir);
    }

}
