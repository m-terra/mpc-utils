package org.mterra.mpc;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class BaseTest {

    protected static final File resultDir = new File("./target/result");

    @BeforeEach
    public void prepare() throws Exception {
        FileUtils.deleteDirectory(resultDir);
        Assertions.assertTrue(resultDir.mkdirs());
    }

}
