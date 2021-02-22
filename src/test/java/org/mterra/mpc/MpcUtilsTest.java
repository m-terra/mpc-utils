package org.mterra.mpc;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class MpcUtilsTest {

    private final File resultDir = new File("./target/result");

    @BeforeEach
    public void prepare() throws Exception {
        FileUtils.deleteDirectory(resultDir);
        resultDir.mkdirs();
    }

    @Test
    public void withAir() throws Exception {

        String[] args = new String[]{"./src/test/resources/Air", "./target/result"};
        MpcUtils.main(args);
        assertFileContent("Aerial", "1.sxq", "1");
        assertFileContent("Aerial", "20.sxq", "20");
        assertFileContent("Aerial", "5.sxq", "4");
    }

    @Test
    public void noAir() throws Exception {
        String[] args = new String[]{"./src/test/resources/NoAir", "./target/result"};
        MpcUtils.main(args);
        assertFileContent("NoAir", "1.sxq", "1");
        assertFileContent("NoAir", "20.sxq", "20");
        assertFileContent("NoAir", "5.sxq", "4");
    }

    private void assertFileContent(String projDirPrefix, String filename, String expectedContent) throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of("target", "result", projDirPrefix + "_[ProjectData]", filename));
        String content = new String(bytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(expectedContent, content);
    }

}
