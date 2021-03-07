package org.mterra.mpc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.BaseTest;
import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.util.Constants;
import org.mterra.mpc.util.Helper;

import java.util.List;


public class QLinkTest extends BaseTest {

    @Test
    public void configureQLinkMode() {
        service.configureQLinkMode(projectsDir.getPath(), resultDir.getPath(), "Program");
        List<ProjectInfo> resultProjects = Helper.getProjectsInDirectory(resultDir.getPath());

        Assertions.assertEquals(4, resultProjects.size());
        for (ProjectInfo projectInfo : resultProjects) {
            Project project = new Project();
            project.load(projectInfo);

            Assertions.assertEquals("Program", project.getQLinkMode());
        }
    }

    @Test
    public void configureProjectQLinkMap() {
        service.configureProjectQLinkMap(projectsDir.getPath(), resultDir.getPath(), false);
        List<ProjectInfo> resultProjects = Helper.getProjectsInDirectory(resultDir.getPath());

        Assertions.assertEquals(4, resultProjects.size());
        for (ProjectInfo projectInfo : resultProjects) {
            Project project = new Project();
            project.load(projectInfo);

            assertQLinkConfig(project, 2, 3);
            assertQLinkConfig(project, 6, 7);
            assertQLinkConfig(project, 10, 11);
            assertQLinkConfig(project, 14, 15);
        }
    }

    private void assertQLinkConfig(Project project, Integer volumeIndex, Integer muteIndex) {
        Assertions.assertEquals(Constants.QLINK_TYPE_PROGRAM, project.getQLinkProjectAssignementType(volumeIndex));
        Assertions.assertEquals(Constants.QLINK_TYPE_PROGRAM, project.getQLinkProjectAssignementType(muteIndex));

        Assertions.assertEquals(Constants.QLINK_PARAMTER_VOLUME, project.getQLinkProjectAssignementParameter(volumeIndex));
        Assertions.assertEquals(Constants.QLINK_PARAMTER_MUTE, project.getQLinkProjectAssignementParameter(muteIndex));

        Assertions.assertEquals(Constants.QLINK_PARAMTER_VOLUME, project.getQLinkProjectAssignementParameter(4));

        Assertions.assertEquals("32785", project.getQLinkProjectAssignementParameter(16));
    }

}
