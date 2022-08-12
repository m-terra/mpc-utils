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
    public void uniqueSeqswithAir() throws Exception {
        boolean uniqueSeqs = true;
        String projectName = "Aerial";
        service.reorderSequences(projectsDir.getPath(), resultDir.getPath(), Constants.DEFAULT_SONG_NUMBER, uniqueSeqs,
                false, Constants.DEFAULT_FILTER_SEQUENCE_NAME);
        File srcProjectDataFolder = new File(projectsDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        File targetDataFolder = new File(resultDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        ProjectInfo srcProjectInfo = new ProjectInfo(srcProjectDataFolder);
        ProjectInfo targetProjectInfo = new ProjectInfo(targetDataFolder);

        assertSequenceNumber(srcProjectInfo, targetProjectInfo, true);
        assertFileContent(projectName, "20.sxq", "Air");
    }

    @Test
    public void uniqueSeqsWithLive() throws Exception {
        boolean uniqueSeqs = true;
        String projectName = "WithLives";
        service.reorderSequences(projectsDir.getPath(), resultDir.getPath(), Constants.DEFAULT_SONG_NUMBER, uniqueSeqs,
                false, Constants.DEFAULT_FILTER_SEQUENCE_NAME);
        File srcProjectDataFolder = new File(projectsDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        File targetDataFolder = new File(resultDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        ProjectInfo srcProjectInfo = new ProjectInfo(srcProjectDataFolder);
        ProjectInfo targetProjectInfo = new ProjectInfo(targetDataFolder);

        assertSequenceNumber(srcProjectInfo, targetProjectInfo, uniqueSeqs);
        assertFileContent(projectName, "1.sxq", "20");
        assertFileContent(projectName, "14.sxq", "live1");
        assertFileContent(projectName, "15.sxq", "live2");
        assertFileContent(projectName, "16.sxq", "live3");
        Assertions.assertFalse(new File(targetDataFolder.getPath() + "/22.sxq").exists());
        assertContainsSequences(targetProjectInfo, "Live", "Live2", "Live3");
    }

    @Test
    public void replicateSeqsWithLive() throws Exception {
        boolean uniqueSeqs = false;
        String projectName = "Deep Stop";
        service.reorderSequences(projectsDir.getPath(), resultDir.getPath(), Constants.DEFAULT_SONG_NUMBER, uniqueSeqs,
                true, Constants.DEFAULT_FILTER_SEQUENCE_NAME);
        File srcProjectDataFolder = new File(projectsDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        File targetDataFolder = new File(resultDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        ProjectInfo srcProjectInfo = new ProjectInfo(srcProjectDataFolder);
        ProjectInfo targetProjectInfo = new ProjectInfo(targetDataFolder);

        assertSequenceNumber(srcProjectInfo, targetProjectInfo, uniqueSeqs);
        assertContainsSequences(targetProjectInfo, "Live", "Air");
        assertFileContent(projectName, "1.sxq", "Live");
        assertFileContent(projectName, "30.sxq", "Air");
    }

    @Test
    public void replicateSequencesWithUnused() {
        boolean uniqueSeqs = false;
        String projectName = "WithLives";
        service.reorderSequences(projectsDir.getPath(), resultDir.getPath(), Constants.DEFAULT_SONG_NUMBER, uniqueSeqs,
                false, Constants.DEFAULT_FILTER_SEQUENCE_NAME);
        File srcProjectDataFolder = new File(projectsDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        File targetDataFolder = new File(resultDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        ProjectInfo srcProjectInfo = new ProjectInfo(srcProjectDataFolder);
        ProjectInfo targetProjectInfo = new ProjectInfo(targetDataFolder);

        assertSequenceNumber(srcProjectInfo, targetProjectInfo, uniqueSeqs);
        assertContainsSequences(targetProjectInfo, "Live", "Live2", "Live3");
    }

    @Test
    public void customLiveSequenceName() {
        boolean uniqueSeqs = false;
        String projectName = "WithLives";
        service.reorderSequences(projectsDir.getPath(), resultDir.getPath(), Constants.DEFAULT_SONG_NUMBER, uniqueSeqs,
                true, "Live2");
        File targetDataFolder = new File(resultDir.getPath() + "/" + projectName + Constants.PROJECT_FOLDER_SUFFIX);
        ProjectInfo targetProjectInfo = new ProjectInfo(targetDataFolder);

        assertSequenceAtPosition(targetProjectInfo, "Live2", 1);
    }

    private void assertFileContent(String projDirPrefix, String filename, String expectedContent) throws Exception {
        byte[] bytes = Files.readAllBytes(Path.of(resultDir.getPath(), projDirPrefix + Constants.PROJECT_FOLDER_SUFFIX, filename));
        String content = new String(bytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(expectedContent, content);
    }

    private void assertSequenceNumber(ProjectInfo origProjectInfo, ProjectInfo targetProjectInfo, boolean uniqueSeqs) {
        SequencesAndSongs srcSeqAndSong = new SequencesAndSongs();
        srcSeqAndSong.load(origProjectInfo);
        SequencesAndSongs targetSeqAndSong = new SequencesAndSongs();
        targetSeqAndSong.load(targetProjectInfo);

        if (uniqueSeqs) {
            Assertions.assertEquals(srcSeqAndSong.getSeqInfoMap().size(), targetSeqAndSong.getSeqInfoMap().size());
        } else {
            Assertions.assertTrue(srcSeqAndSong.getSeqInfoMap().size() < targetSeqAndSong.getSeqInfoMap().size());
        }
        NodeList seqNodes = targetSeqAndSong.getSeqNodeList();

        for (int i = 0; i < seqNodes.getLength(); i++) {
            Element seq = (Element) seqNodes.item(i);
            String name = seq.getElementsByTagName("Name").item(0).getTextContent();
            Integer seqNumber = Integer.parseInt(seq.getAttribute("number"));
            if (name.equalsIgnoreCase("air")) {
                Assertions.assertEquals(0, seqNumber % 10);
            } else {
                Assertions.assertEquals(i + 1, seqNumber);
            }
            File seqFile = new File(targetProjectInfo.getProjectDataFolder(), seqNumber + ".sxq");
            Assertions.assertTrue(seqFile.exists(), "Missing sequence file " + seqFile.getAbsolutePath());
        }
    }

    private void assertContainsSequences(ProjectInfo projectInfo, String... seqNames) {
        SequencesAndSongs songSeqs = new SequencesAndSongs();
        songSeqs.load(projectInfo);

        for (String seqName : seqNames) {
            Assertions.assertTrue(songSeqs.containsSequence(seqName));
        }
    }

    private void assertSequenceAtPosition(ProjectInfo projectInfo, String seqName, Integer seqPosition) {
        SequencesAndSongs songSeqs = new SequencesAndSongs();
        songSeqs.load(projectInfo);
        Assertions.assertEquals(1, songSeqs.getSequenceNumber(seqName));
    }

}
