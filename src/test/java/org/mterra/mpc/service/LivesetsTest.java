package org.mterra.mpc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.BaseTest;
import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.util.Helper;

import java.util.List;


public class LivesetsTest extends BaseTest {

    @Test
    public void createLiveset() {
        service.createLiveset(projectsDir.getPath(), resultDir.getPath());
        List<ProjectInfo> resultProjects = Helper.getProjectsInDirectory(resultDir.getPath());

        Assertions.assertEquals(3, resultProjects.size());
        for (ProjectInfo projectInfo : resultProjects) {
            Project project = new Project();
            project.load(projectInfo);
            Assertions.assertEquals(Liveset.QLINK_MODE_PROJECT, project.getQLinkMode());
        }
    }

}
