package org.mterra.mpc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.BaseTest;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.model.SequencesAndSongs;
import org.mterra.mpc.util.Constants;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class ReorderTest extends BaseTest {

    @Test
    public void withAirSequence() throws Exception {
        String projectName = "Aerial";
        service.reorderSequences(projectsDir.getPath(), resultDir.getPath(), Constants.DEFAULT_SONG_NUMBER);
        File projectDataFolder = new File(resultDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        assertSequenceNumber(new ProjectInfo(projectDataFolder), resultDir.getPath() + "/" + projectName);
        assertFileContent(projectName, "20.sxq", "Air");
    }

    @Test
    public void withUnusedSequences() throws Exception {
        String projectName = "WithLives";
        service.reorderSequences(projectsDir.getPath(), resultDir.getPath(), Constants.DEFAULT_SONG_NUMBER);
        File projectDataFolder = new File(resultDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        assertSequenceNumber(new ProjectInfo(projectDataFolder), resultDir.getPath() + "/" + projectName);
        assertFileContent(projectName, "1.sxq", "20");
        assertFileContent(projectName, "14.sxq", "live1");
        assertFileContent(projectName, "15.sxq", "live2");
        assertFileContent(projectName, "16.sxq", "live3");
        Assertions.assertFalse(new File("./target/result/" + projectName + Constants.PROJECT_FOLDER_SUFFIX + "/22.sxq").exists());
    }

    private void assertFileContent(String projDirPrefix, String filename, String expectedContent) throws Exception {
        byte[] bytes = Files.readAllBytes(Path.of(resultDir.getPath(), projDirPrefix + Constants.PROJECT_FOLDER_SUFFIX, filename));
        String content = new String(bytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(expectedContent, content);
    }

    private void assertSequenceNumber(ProjectInfo projectInfo, String targetDir) {
        SequencesAndSongs origProj = new SequencesAndSongs();
        origProj.load(projectInfo);

        File targetProjFolder = new File(targetDir + Constants.PROJECT_FOLDER_SUFFIX);
        SequencesAndSongs targetProj = new SequencesAndSongs();
        targetProj.load(new ProjectInfo(targetProjFolder));

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
