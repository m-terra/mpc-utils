package org.mterra.mpc.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.util.Constants;

import java.io.File;

public class SequencesAndSongsTest {

    private ProjectInfo projectInfo;

    @BeforeEach
    public void setup() {
        File srcDir = new File("./src/test/resources/projects/WithLives" + Constants.PROJECT_FOLDER_SUFFIX);
        this.projectInfo = new ProjectInfo(srcDir);
    }

    @Test
    public void containsSequence() {
        SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
        sequencesAndSongs.load(projectInfo);
        Assertions.assertTrue(sequencesAndSongs.containsSequenceWithPrefix(Constants.LIVE_SEQUENCE_BASIC));
    }

    @Test
    public void notContainsSequence() {
        SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
        sequencesAndSongs.load(projectInfo);
        Assertions.assertFalse(sequencesAndSongs.containsSequenceWithPrefix("FooBar"));
    }


}
