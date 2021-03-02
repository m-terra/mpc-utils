package org.mterra.mpc.seq;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.MpcUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Reorderer {

    public Map<Integer, SeqInfo> calculateNewOrder(SequencesAndSongs sequencesAndSongs) {
        Map<Integer, SeqInfo> ordered = new TreeMap<>();
        List<SeqInfo> notUsedInSong = new ArrayList<>();
        SeqInfo airSeq = null;
        for (SeqInfo seqInfo : sequencesAndSongs.getSeqInfoMap().values()) {
            if (seqInfo.getName().equalsIgnoreCase("air")) {
                airSeq = seqInfo;
            } else if (seqInfo.getPosInSong().size() == 0) {
                notUsedInSong.add(seqInfo);
            } else {
                ordered.put(seqInfo.getPosInSong().get(0), seqInfo);
            }
        }

        Map<Integer, SeqInfo> finalOrdered = new TreeMap<>();
        int newSeqNumber = 1;
        for (SeqInfo seqInfo : ordered.values()) {
            finalOrdered.put(newSeqNumber++, seqInfo);
        }
        for (SeqInfo seqInfo : notUsedInSong) {
            finalOrdered.put(newSeqNumber++, seqInfo);
        }
        if (airSeq != null) {
            while (newSeqNumber % 10 != 0) {
                newSeqNumber++;
            }
            finalOrdered.put(newSeqNumber, airSeq);
        }
        return finalOrdered;
    }

    public void updateFiles(SequencesAndSongs sequencesAndSongs, Map<Integer, SeqInfo> reordered, File targetDir, String projectName) {
        File targetProjDir = new File(targetDir, projectName + MpcUtils.PROJECT_FOLDER_SUFFIX);
        try {
            sequencesAndSongs.removeAllSequences();

            for (Map.Entry<Integer, SeqInfo> entry : reordered.entrySet()) {
                SeqInfo seqInfo = entry.getValue();
                Integer newSeqNumber = entry.getKey();
                sequencesAndSongs.addSequence(newSeqNumber, seqInfo.getName());
                for (Integer pos : seqInfo.getPosInSong()) {
                    sequencesAndSongs.changeSequenceIndexInSong(pos, String.valueOf(entry.getKey() - 1));
                }
                if (seqInfo.needsMoving(newSeqNumber)) {
                    Path src = Paths.get(targetProjDir.getPath(), seqInfo.getSeqNumber() + "." + MpcUtils.SEQ_SUFFIX);
                    Path dest = Paths.get(targetProjDir.getPath(), newSeqNumber + "." + MpcUtils.SEQ_SUFFIX + "tmp");
                    Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
                    System.out.printf("Moved sequence '%s' from number '%s' to number '%s'%n", seqInfo.getName(), seqInfo.getSeqNumber(), newSeqNumber);
                } else {
                    System.out.printf("Sequence '%s' keeps number '%s'%n", seqInfo.getName(), seqInfo.getSeqNumber());
                }
            }

            sequencesAndSongs.writeDocument(targetProjDir);

            Collection<?> seqFiles = FileUtils.listFiles(targetProjDir, new String[]{MpcUtils.SEQ_SUFFIX + "tmp"}, false);
            for (Object el : seqFiles) {
                if (el instanceof File) {
                    File src = (File) el;
                    Path fileToMovePath = Paths.get(src.getPath());
                    Path targetPath = Paths.get(StringUtils.substringBefore(src.getPath(), "tmp"));
                    Files.move(fileToMovePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
