package org.mterra.mpc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.BaseTest;
import org.mterra.mpc.util.Constants;

import java.io.File;


public class FilterTest extends BaseTest {

    @Test
    public void filter() {
        service.filterProjects(projectsDir.getPath(), resultDir.getPath());
        Assertions.assertTrue(new File(resultDir, "WithLives.xpj").exists());
        Assertions.assertTrue(new File(resultDir, "WithLives" + Constants.PROJECT_FOLDER_SUFFIX).exists());
        Assertions.assertTrue(new File(resultDir, "Pieces and Fractures.xpj").exists());
        Assertions.assertTrue(new File(resultDir, "Pieces and Fractures" + Constants.PROJECT_FOLDER_SUFFIX).exists());
        Assertions.assertFalse(new File(resultDir, "Aerial.xpj").exists());
        Assertions.assertFalse(new File(resultDir, "Aerial" + Constants.PROJECT_FOLDER_SUFFIX).exists());
    }

}
