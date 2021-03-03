package org.mterra.mpc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.BaseTest;
import org.mterra.mpc.MpcUtils;

import java.io.File;


public class FilterTest extends BaseTest {

    @Test
    public void filter() throws Exception {
        String[] args = new String[]{"-c", "filter", "-i", "./src/test/resources/projects", "-o", resultDir.getPath()};
        MpcUtils.main(args);
        Assertions.assertTrue(new File(resultDir, "WithLives.xpj").exists());
        Assertions.assertTrue(new File(resultDir, "WithLives" + MpcUtils.PROJECT_FOLDER_SUFFIX).exists());
        Assertions.assertTrue(new File(resultDir, "Pieces and Fractures.xpj").exists());
        Assertions.assertTrue(new File(resultDir, "Pieces and Fractures" + MpcUtils.PROJECT_FOLDER_SUFFIX).exists());
        Assertions.assertFalse(new File(resultDir, "Aerial.xpj").exists());
        Assertions.assertFalse(new File(resultDir, "Aerial" + MpcUtils.PROJECT_FOLDER_SUFFIX).exists());
    }

}
