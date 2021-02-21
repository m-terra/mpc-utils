package org.mterra.mpc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class MpcUtilsTest {

    @Test
    public void reorderSeqs() throws Exception {
        String[] args = new String[]{"./src/test/resources", "./target"};
        MpcUtils.main(args);
        assertFileContent("1.sxq", "1");
        assertFileContent("20.sxq", "20");
        assertFileContent("5.sxq", "4");
    }

    private void assertFileContent(String filename, String expectedContent) throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of("target", "Aerial_[ProjectData]", filename));
        String content = new String(bytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(expectedContent, content);
    }

}
