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

        project.setQLinkProjectTrackAssignement(2, Constants.QLINK_TYPE_MIDI_TRACK, 3, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkProjectTrackAssignement(3, Constants.QLINK_TYPE_MIDI_TRACK, 3, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkProjectTrackAssignement(6, Constants.QLINK_TYPE_MIDI_TRACK, 2, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkProjectTrackAssignement(7, Constants.QLINK_TYPE_MIDI_TRACK, 2, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkProjectTrackAssignement(10, Constants.QLINK_TYPE_MIDI_TRACK, 1, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkProjectTrackAssignement(11, Constants.QLINK_TYPE_MIDI_TRACK, 1, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkProjectTrackAssignement(14, Constants.QLINK_TYPE_MIDI_TRACK, 0, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkProjectTrackAssignement(15, Constants.QLINK_TYPE_MIDI_TRACK, 0, Constants.QLINK_PARAMTER_MUTE, false);

        project.setQLinkProjectTrackAssignement(4, Constants.QLINK_TYPE_MASTER, 0, Constants.QLINK_PARAMTER_VOLUME, false);
        project.setQLinkProjectTrackAssignement(12, Constants.QLINK_TYPE_MASTER, 0, Constants.QLINK_PARAMTER_EQ_LOW_GAIN, true);
        project.setQLinkProjectTrackAssignement(16, Constants.QLINK_TYPE_MASTER, 0, Constants.QLINK_PARAMTER_EQ_HIGH_GAIN, true);
    }

    public void updateProjectFile(File targetDir) {
        getProject().writeDocument(targetDir);
    }

}
