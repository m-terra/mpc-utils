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
    public void createLiveset() throws Exception {
        service.createLiveset(projectsDir.getPath(), resultDir.getPath(), Constants.DEFAULT_FILTER_SEQUENCE_NAME,
                Constants.DEFAULT_SONG_NUMBER, false, Constants.QLINK_MODE_PROJECT);
        File bpmFile = new File(resultDir, Constants.DEFAULT_BPM_FILE_NAME);
        Assertions.assertTrue(bpmFile.exists());

        String bpmFileContent = Files.readString(bpmFile.toPath());
        Assertions.assertEquals(" 95.0\tPieces and Fractures\n100.0\tDeep Stop\n121.0\tWithLives\n", bpmFileContent);

        Assertions.assertTrue(new File(resultDir, "WithLives.xpj").exists());
        Assertions.assertTrue(new File(resultDir, "Pieces and Fractures.xpj").exists());
        Assertions.assertFalse(new File(resultDir, "Aerial.xpj").exists());

        List<ProjectInfo> resultProjects = Helper.getProjectsInDirectory(resultDir.getPath());
        Assertions.assertEquals(3, resultProjects.size());
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
