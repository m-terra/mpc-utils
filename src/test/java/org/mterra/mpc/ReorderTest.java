package org.mterra.mpc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.model.SequencesAndSongs;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class ReorderTest extends BaseTest {

    @Test
    public void withAirSequence() throws Exception {
        String projectName = "Aerial";
        String[] args = new String[]{"reorder", "./src/test/resources/projects", "./target/result"};
        MpcUtils.main(args);
        assertSequenceNumber(args[1] + "/" + projectName, "./target/result/" + projectName);
        assertFileContent(projectName, "20.sxq", "Air");
    }

    @Test
    public void withUnusedSequences() throws Exception {
        String projectName = "WithLives";
        String[] args = new String[]{"reorder", "./src/test/resources/projects", "./target/result"};
        MpcUtils.main(args);
        assertSequenceNumber(args[1] + "/" + projectName, "./target/result/" + projectName);
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

    private void assertSequenceNumber(String sourceDir, String targetDir) {
        SequencesAndSongs origProj = new SequencesAndSongs();
        origProj.load(new File(sourceDir + MpcUtils.PROJECT_FOLDER_SUFFIX));

        File targetProjFolder = new File(targetDir + MpcUtils.PROJECT_FOLDER_SUFFIX);
        SequencesAndSongs targetProj = new SequencesAndSongs();
        targetProj.load(targetProjFolder);

        Assertions.assertEquals(origProj.getSeqInfoMap().size(), targetProj.getSeqInfoMap().size());
        NodeList seqNodes = targetProj.getSeqNodeList();

        int seqIdx = 1;
        for (int i = 0; i < seqNodes.getLength(); i++) {
            Element seq = (Element) seqNodes.item(i);
            String name = seq.getElementsByTagName("Name").item(0).getTextContent();
            String number = seq.getAttribute("number");
            Integer seqNumber = Integer.parseInt(number);
            if (name.equalsIgnoreCase("air")) {
                seqIdx = 20;
            }
            Assertions.assertEquals(seqIdx++, seqNumber);
            File seqFile = new File(targetProjFolder, seqNumber + ".sxq");
            Assertions.assertTrue(seqFile.exists(), "Missing sequence file " + seqFile.getAbsolutePath());
        }
    }

}
