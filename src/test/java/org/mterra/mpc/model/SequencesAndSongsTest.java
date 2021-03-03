package org.mterra.mpc.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.MpcUtils;

import java.io.File;

public class SequencesAndSongsTest {

    private ProjectInfo projectInfo;

    @BeforeEach
    public void setup() throws Exception {
        File srcDir = new File("./src/test/resources/projects/WithLives" + MpcUtils.PROJECT_FOLDER_SUFFIX);
        this.projectInfo = new ProjectInfo(srcDir);
    }

    @Test
    public void containsSequence() throws Exception {
        SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
        sequencesAndSongs.load(projectInfo);
        Assertions.assertTrue(sequencesAndSongs.containsSequence("Live"));
    }

    @Test
    public void notContainsSequence() throws Exception {
        SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
        sequencesAndSongs.load(projectInfo);
        Assertions.assertFalse(sequencesAndSongs.containsSequence("FooBar"));
    }


}
