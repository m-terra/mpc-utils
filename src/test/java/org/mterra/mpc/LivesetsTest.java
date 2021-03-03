package org.mterra.mpc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.util.Helper;

import java.io.File;
import java.nio.file.Files;
import java.util.List;


public class LivesetsTest extends BaseTest {

    @Test
    public void filter() throws Exception {
        String[] args = new String[]{"filter", "./src/test/resources/projects", resultDir.getPath()};
        MpcUtils.main(args);
        Assertions.assertTrue(new File(resultDir, "WithLives.xpj").exists());
        Assertions.assertTrue(new File(resultDir, "WithLives" + MpcUtils.PROJECT_FOLDER_SUFFIX).exists());
        Assertions.assertTrue(new File(resultDir, "Pieces and Fractures.xpj").exists());
        Assertions.assertTrue(new File(resultDir, "Pieces and Fractures" + MpcUtils.PROJECT_FOLDER_SUFFIX).exists());
        Assertions.assertFalse(new File(resultDir, "Aerial.xpj").exists());
        Assertions.assertFalse(new File(resultDir, "Aerial" + MpcUtils.PROJECT_FOLDER_SUFFIX).exists());
    }

    @Test
    public void songBpm() throws Exception {
        List<ProjectInfo> projectInfoList = Helper.getProjectsInDirectory("./src/test/resources/projects");
        for (ProjectInfo projectInfo : projectInfoList) {
            Helper.copyProject(projectInfo, resultDir);
        }
        String[] args = new String[]{"bpm", resultDir.getPath()};
        MpcUtils.main(args);

        File bpmFile = new File(resultDir, "Project_BPM.txt");
        Assertions.assertTrue(bpmFile.exists());

        String bpmFileContent = Files.readString(bpmFile.toPath());
        Assertions.assertTrue(bpmFileContent.contains("95.000000\tPieces and Fractures"));
        Assertions.assertTrue(bpmFileContent.contains("90.000000\tWithLives"));
        Assertions.assertTrue(bpmFileContent.contains("90.000000\tAerial"));
    }


}
