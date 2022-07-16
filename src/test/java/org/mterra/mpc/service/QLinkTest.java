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
        service.configureProjectQLinkMap(projectsDir.getPath(), resultDir.getPath());
        List<ProjectInfo> resultProjects = Helper.getProjectsInDirectory(resultDir.getPath());

        Assertions.assertEquals(4, resultProjects.size());
        for (ProjectInfo projectInfo : resultProjects) {
            Project project = new Project();
            project.load(projectInfo);

            Assertions.assertEquals(Constants.QLINK_TYPE_TRACK, project.getQLinkProjectAssignementType(Constants.QLINK_INDEX_BD_VOLUME));
            Assertions.assertEquals(Constants.QLINK_PARAMTER_VOLUME, project.getQLinkProjectAssignementParameter(Constants.QLINK_INDEX_BD_VOLUME));
            Assertions.assertEquals(Constants.QLINK_TYPE_TRACK, project.getQLinkProjectAssignementType(Constants.QLINK_INDEX_SN_VOLUME));
            Assertions.assertEquals(Constants.QLINK_PARAMTER_VOLUME, project.getQLinkProjectAssignementParameter(Constants.QLINK_INDEX_BD_VOLUME));
        }
    }

}
