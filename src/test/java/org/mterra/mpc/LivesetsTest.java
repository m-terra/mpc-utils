package org.mterra.mpc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;


public class LivesetsTest extends BaseTest {

    @Test
    public void livesets() throws Exception {
        File resultDir = new File("./target/result");
        String[] args = new String[]{"filter", "./src/test/resources/projects", resultDir.getPath()};
        MpcUtils.main(args);
        Assertions.assertTrue(new File(resultDir, "WithLives.xpj").exists());
        Assertions.assertTrue(new File(resultDir, "WithLives" + MpcUtils.PROJECT_FOLDER_SUFFIX).exists());
        Assertions.assertTrue(new File(resultDir, "Pieces and Fractures.xpj").exists());
        Assertions.assertTrue(new File(resultDir, "Pieces and Fractures" + MpcUtils.PROJECT_FOLDER_SUFFIX).exists());
        Assertions.assertFalse(new File(resultDir, "Aerial.xpj").exists());
        Assertions.assertFalse(new File(resultDir, "Aerial" + MpcUtils.PROJECT_FOLDER_SUFFIX).exists());

        File bpmFile = new File(resultDir, "Project_BPM.txt");
        Assertions.assertTrue(bpmFile.exists());

        String bpmFileContent = Files.readString(bpmFile.toPath());
        Assertions.assertTrue(bpmFileContent.contains("95.000000\tPieces and Fractures"));
        Assertions.assertTrue(bpmFileContent.contains("90.000000\tWithLives"));


    }


}
