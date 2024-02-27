package org.mterra.mpc.service;

import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;

import java.io.File;

public class ProjectSettings {

    private final ProjectInfo projectInfo;
    private Project project;

    public ProjectSettings(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
    }

    private Project getProject() {
        if (project == null) {
            project = new Project();
            project.load(projectInfo);
        }
        return project;
    }

    public void configureArpLiveSettings() {
        getProject().setArpLiveSettings();
    }

    public void updateProjectSettingsFile(File targetDir) {
        getProject().writeProjectSettingsDocument(targetDir);
    }

}
