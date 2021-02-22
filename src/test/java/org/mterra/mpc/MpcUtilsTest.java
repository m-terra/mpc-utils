package org.mterra.mpc;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.seq.MpcProject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class MpcUtilsTest {

    private static final File resultDir = new File("./target/result");

    @BeforeAll
    public static void prepare() throws Exception {
        FileUtils.deleteDirectory(resultDir);
        Assertions.assertTrue(resultDir.mkdirs());
    }

    @Test
    public void noLives() throws Exception {
        String projectName = "Aerial";
        String[] args = new String[]{"./src/test/resources/" + projectName, "./target/result"};
        MpcUtils.main(args);
        assertSequenceNumber("./target/result/" + projectName);
        assertFileContent(projectName, "1.sxq", "20");
    }

    @Test
    public void withLives() throws Exception {
        String projectName = "WithLives";
        String[] args = new String[]{"./src/test/resources/" + projectName, "./target/result"};
        MpcUtils.main(args);
        assertSequenceNumber("./target/result/" + projectName);
        assertFileContent(projectName, "1.sxq", "20");
        assertFileContent(projectName, "14.sxq", "live1");
        assertFileContent(projectName, "15.sxq", "live2");
        assertFileContent(projectName, "16.sxq", "live3");
        Assertions.assertFalse(new File("./target/result/" + projectName + MpcUtils.PROJECT_FOLDER_SUFFIX + "/22.sxq").exists());
    }

    private void assertFileContent(String projDirPrefix, String filename, String expectedContent) throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of("target", "result", projDirPrefix + "_[ProjectData]", filename));
        String content = new String(bytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(expectedContent, content);
    }

    private void assertSequenceNumber(String parentDir) {
        MpcProject project = new MpcProject();
        project.loadSequencesAndSongs(new File(parentDir + MpcUtils.PROJECT_FOLDER_SUFFIX));
        NodeList seqNodes = project.getSeqNodeList();

        int seqIdx = 1;
        for (int i = 0; i < seqNodes.getLength(); i++) {
            Element seq = (Element) seqNodes.item(i);
            String number = seq.getAttribute("number");
            Assertions.assertEquals(seqIdx++, Integer.parseInt(number));
        }
    }

}
