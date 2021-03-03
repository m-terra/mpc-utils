package org.mterra.mpc.service;

import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;

import java.io.File;

public class QLinks {

    public static final String QLINK_MODE_PROJECT = "Project";
    public static final String QLINK_TYPE_MIDI_TRACK = "MidiTrack";
    public static final String QLINK_PARAMTER_VOLUME = "7";
    public static final String QLINK_PARAMTER_MUTE = "256";
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
        project.setQLinkMode(QLINK_MODE_PROJECT);

        project.setQLinkProjectTrackAssignement(2, 3, QLINK_PARAMTER_VOLUME);
        project.setQLinkProjectTrackAssignement(3, 3, QLINK_PARAMTER_MUTE);

        project.setQLinkProjectTrackAssignement(6, 2, QLINK_PARAMTER_VOLUME);
        project.setQLinkProjectTrackAssignement(7, 2, QLINK_PARAMTER_MUTE);

        project.setQLinkProjectTrackAssignement(10, 1, QLINK_PARAMTER_VOLUME);
        project.setQLinkProjectTrackAssignement(11, 1, QLINK_PARAMTER_MUTE);

        project.setQLinkProjectTrackAssignement(14, 0, QLINK_PARAMTER_VOLUME);
        project.setQLinkProjectTrackAssignement(15, 0, QLINK_PARAMTER_MUTE);
    }

    public void updateProjectFile(File targetDir) {
        getProject().writeDocument(targetDir);
    }

}
