package org.mterra.mpc.service;

import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.model.SeqInfo;
import org.mterra.mpc.model.SequencesAndSongs;
import org.mterra.mpc.util.Constants;
import org.mterra.mpc.util.Helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Reorderer {

    public Map<Integer, SeqInfo> reorderSequences(SequencesAndSongs sequencesAndSongs, Boolean uniqueSeqs) {
        Map<Integer, SeqInfo> ordered = new TreeMap<>();
        List<SeqInfo> notUsedInSong = new ArrayList<>();
        SeqInfo airSeq = null;
        for (SeqInfo seqInfo : sequencesAndSongs.getSeqInfoMap().values()) {
            if (seqInfo.getName().equalsIgnoreCase("air")) {
                airSeq = seqInfo;
            } else if (seqInfo.getPosInSong().size() == 0) {
                notUsedInSong.add(seqInfo);
            } else {
                if (uniqueSeqs) {
                    ordered.put(seqInfo.getPosInSong().get(0), seqInfo);
                } else {
                    for (Integer pos : seqInfo.getPosInSong()) {
                        ordered.put(pos + 1, seqInfo.cloneWithSongPosition(pos));
                    }
                }
            }
        }

        int newSeqNumber = 1;
        Map<Integer, SeqInfo> finalOrdered = new TreeMap<>();
        for (SeqInfo seqInfo : ordered.values()) {
            finalOrdered.put(newSeqNumber++, seqInfo);
        }
        for (SeqInfo seqInfo : notUsedInSong) {
            finalOrdered.put(newSeqNumber++, seqInfo);
        }
        addAirSequence(finalOrdered, airSeq, newSeqNumber++);
        return finalOrdered;
    }

    private void addAirSequence(Map<Integer, SeqInfo> seqs, SeqInfo airSeq, Integer nextFreeSequenceNumber) {
        if (airSeq != null) {
            while (nextFreeSequenceNumber % 10 != 0) {
                nextFreeSequenceNumber++;
            }
            seqs.put(nextFreeSequenceNumber, airSeq);
        }
    }

    public void writeReorderedProject(ProjectInfo projectInfo, SequencesAndSongs sequencesAndSongs, Map<Integer, SeqInfo> reordered, File targetDir) {
        Helper.copyProject(projectInfo, targetDir);
        File newProjectDataFolder = new File(targetDir.getPath(), projectInfo.getProjectDataFolder().getName());
        Helper.deleteFilesByExtension(newProjectDataFolder, Constants.SEQ_SUFFIX);
        try {
            sequencesAndSongs.removeAllSequences();

            for (Map.Entry<Integer, SeqInfo> entry : reordered.entrySet()) {
                SeqInfo seqInfo = entry.getValue();
                Integer newSeqNumber = entry.getKey();
                sequencesAndSongs.addSequence(newSeqNumber, seqInfo.getName());
                for (Integer pos : seqInfo.getPosInSong()) {
                    sequencesAndSongs.changeSequenceIndexInSong(pos, String.valueOf(entry.getKey() - 1));
                }
                Path src = Paths.get(projectInfo.getProjectDataFolder().getPath(), seqInfo.getSeqNumber() + "." + Constants.SEQ_SUFFIX);
                Path dest = Paths.get(newProjectDataFolder.getPath(), newSeqNumber + "." + Constants.SEQ_SUFFIX);
                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            }

            sequencesAndSongs.writeDocument(newProjectDataFolder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
