package org.mterra.mpc.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mterra.mpc.MpcUtils;

import java.io.File;

public class SequencesAndSongsTest {

    @Test
    public void containsSequence() throws Exception {
        File srcDir = new File("./src/test/resources/projects/WithLives" + MpcUtils.PROJECT_FOLDER_SUFFIX);
        SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
        sequencesAndSongs.load(srcDir);
        Assertions.assertTrue(sequencesAndSongs.containsSequence("Live"));
    }

    @Test
    public void notContainsSequence() throws Exception {
        File srcDir = new File("./src/test/resources/projects/WithLives" + MpcUtils.PROJECT_FOLDER_SUFFIX);
        SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
        sequencesAndSongs.load(srcDir);
        Assertions.assertFalse(sequencesAndSongs.containsSequence("FooBar"));
    }


}
