package org.mterra.mpc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.BaseTest;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.util.Constants;
import org.mterra.mpc.util.Helper;

import java.io.File;
import java.nio.file.Files;
import java.util.List;


public class BpmTest extends BaseTest {

    @Test
    public void songBpm() throws Exception {
        List<ProjectInfo> projectInfoList = Helper.getProjectsInDirectory("./src/test/resources/projects");
        for (ProjectInfo projectInfo : projectInfoList) {
            Helper.copyProject(projectInfo, resultDir);
        }
        service.createProjectBpmFile(resultDir.getPath());
        File bpmFile = new File(resultDir, Constants.DEFAULT_BPM_FILE_NAME);
        Assertions.assertTrue(bpmFile.exists());
        String bpmFileContent = Files.readString(bpmFile.toPath());
        Assertions.assertEquals("90.0\tAerial\n95.0\tPieces and Fractures\n121.0\tWithLives\n", bpmFileContent);
    }

}
