package org.mterra.mpc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.BaseTest;
import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.util.Helper;

import java.util.List;


public class QLinkTest extends BaseTest {

    @Test
    public void configureProjectQLinks() {
        service.configureProjectQLinks(projectsDir.getPath(), resultDir.getPath());
        List<ProjectInfo> resultProjects = Helper.getProjectsInDirectory(resultDir.getPath());

        Assertions.assertEquals(3, resultProjects.size());
        for (ProjectInfo projectInfo : resultProjects) {
            Project project = new Project();
            project.load(projectInfo);

            Assertions.assertEquals(QLinks.QLINK_MODE_PROJECT, project.getQLinkMode());
            assertQLinkConfig(project, 2, 3);
            assertQLinkConfig(project, 6, 7);
            assertQLinkConfig(project, 10, 11);
            assertQLinkConfig(project, 14, 15);
        }
    }

    private void assertQLinkConfig(Project project, Integer volumeIndex, Integer muteIndex) {
        Assertions.assertEquals(QLinks.QLINK_TYPE_MIDI_TRACK, project.getQLinkProjectAssignementType(volumeIndex));
        Assertions.assertEquals(QLinks.QLINK_TYPE_MIDI_TRACK, project.getQLinkProjectAssignementType(muteIndex));

        Assertions.assertEquals(QLinks.QLINK_PARAMTER_VOLUME, project.getQLinkProjectAssignementParameter(volumeIndex));
        Assertions.assertEquals(QLinks.QLINK_PARAMTER_MUTE, project.getQLinkProjectAssignementParameter(muteIndex));
    }
}
