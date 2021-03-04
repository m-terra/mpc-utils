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
        Assertions.assertTrue(bpmFileContent.contains("95.000000\tPieces and Fractures"));
        Assertions.assertTrue(bpmFileContent.contains("90.000000\tWithLives"));
        Assertions.assertTrue(bpmFileContent.contains("90.000000\tAerial"));
    }

}
