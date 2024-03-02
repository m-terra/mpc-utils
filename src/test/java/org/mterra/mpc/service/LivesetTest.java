package org.mterra.mpc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.BaseTest;
import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.util.Constants;
import org.mterra.mpc.util.Helper;

import java.io.File;
import java.nio.file.Files;
import java.util.List;


public class LivesetTest extends BaseTest {

    @Test
    public void createLiveset() {
        File resultDirBasic = new File(resultDir, "/Basic");
        File resultDirParty = new File(resultDir, "/Party");

        service.createLiveset(projectsDir.getPath(), resultDir.getPath(), Constants.DEFAULT_SONG_NUMBER, false, Constants.QLINK_MODE_PROJECT);

        File bpmFile = new File(resultDir + "/Party", Constants.DEFAULT_BPM_FILE_NAME);
        Assertions.assertTrue(bpmFile.exists());

        Assertions.assertTrue(new File(resultDirBasic, "WithLives.xpj").exists());
        Assertions.assertTrue(new File(resultDirParty, "Pieces and Fractures.xpj").exists());
        Assertions.assertTrue(new File(resultDirBasic, "Deep Stop.xpj").exists());

        List<ProjectInfo> resultProjects = Helper.getProjectsInDirectory(resultDirBasic.getPath());
        Assertions.assertEquals(2, resultProjects.size());
        for (ProjectInfo projectInfo : resultProjects) {
            Project project = new Project();
            project.load(projectInfo);
            Assertions.assertEquals(Constants.QLINK_MODE_PROJECT, project.getQLinkMode());
            if (projectInfo.hasProjectSettingsFile()) {
                Assertions.assertTrue(project.isArpLatched());
            }
        }
    }

}
