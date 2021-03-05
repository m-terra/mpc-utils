package org.mterra.mpc.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.util.Constants;

import java.io.File;

public class ProjectTest {

    private Project project;

    @BeforeEach
    public void setup() {
        File srcDir = new File("./src/test/resources/projects/Aerial" + Constants.PROJECT_FOLDER_SUFFIX);
        ProjectInfo projectInfo = new ProjectInfo(srcDir);
        this.project = new Project();
        this.project.load(projectInfo);
    }

    @Test
    public void getMasterEqInsertIndex() {
        String index = project.getMasterEqInsertIndex();
        Assertions.assertEquals("3", index);
    }

}
