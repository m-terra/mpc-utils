package org.mterra.mpc.seq;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.MpcUtils;
import org.mterra.mpc.util.ProjectHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Reorderer {

    public static void inPath(String scanDirPath, String targetDirPath) {
        File scanDir = new File(scanDirPath);
        File targetDir = new File(targetDirPath);
        for (File srcDir : scanDir.listFiles()) {
            System.out.printf("Checking %s%n", srcDir.getName());
            if (srcDir.getName().contains(MpcUtils.PROJECT_FOLDER_SUFFIX)) {
                String projectName = StringUtils.substringBefore(srcDir.getName(), MpcUtils.PROJECT_FOLDER_SUFFIX);
                System.out.printf("Found project '%s'%n", projectName);
                Reorderer inst = new Reorderer();
                MpcProject mpcProject = new MpcProject();
                mpcProject.loadSequencesAndSongs(srcDir);
                inst.calculateNewOrder(mpcProject);
                ProjectHelper.copyProject(srcDir, targetDir, projectName);
                inst.updateFiles(mpcProject, srcDir, targetDir, projectName);
            }
        }
    }

    private void calculateNewOrder(MpcProject mpcProject) {
        Map<Integer, SeqInfo> ordered = new TreeMap<>();
        boolean hasAir = mpcProject.seqInfoMap.values().stream()
                .anyMatch(seqInfo -> "air".equalsIgnoreCase(seqInfo.name));
        List<SeqInfo> notUsedInSong = new ArrayList<>();
        System.out.printf("Song has Air sequence '%s'%n", hasAir);
        for (SeqInfo seqInfo : mpcProject.seqInfoMap.values()) {
            if ("air".equalsIgnoreCase(seqInfo.name)) {
                seqInfo.newIdx = seqInfo.currentIdx;
                ordered.put(seqInfo.posInSong.get(0), seqInfo);
            } else if (seqInfo.posInSong.size() == 0) {
                notUsedInSong.add(seqInfo);
            } else {
                seqInfo.newIdx = String.valueOf(seqInfo.posInSong.get(0));
                ordered.put(seqInfo.posInSong.get(0), seqInfo);
            }
        }

        Map<Integer, SeqInfo> finalOrdered = new TreeMap<>();
        Iterator<SeqInfo> start = ordered.values().iterator();
        if (hasAir) {
            start.next();
        }
        Integer prev = Integer.parseInt(start.next().newIdx);


        for (SeqInfo seqInfo : ordered.values()) {
            seqInfo.newIdx = String.valueOf(prev);
            finalOrdered.put(prev, seqInfo);
            prev++;
        }
        for (SeqInfo seqInfo : notUsedInSong) {
            seqInfo.newIdx = String.valueOf(prev);
            finalOrdered.put(prev, seqInfo);
            prev++;
        }

        mpcProject.seqInfoMap.clear();
        mpcProject.seqInfoMap.putAll(finalOrdered);
    }

    private void updateFiles(MpcProject mpcProject, File srcDir, File targetDir, String projectName) {
        File targetProjDir = new File(targetDir, projectName + MpcUtils.PROJECT_FOLDER_SUFFIX);
        try {
            mpcProject.removeAllSequences();

            for (SeqInfo seqInfo : mpcProject.seqInfoMap.values()) {
                mpcProject.addSequence(seqInfo.newIdx, seqInfo.name);
                for (Integer pos : seqInfo.posInSong) {
                    mpcProject.changeSequenceIndexInSong(pos, seqInfo.newIdx);
                }
                if (seqInfo.needsMoving()) {
                    File src = new File(targetProjDir, seqInfo.currentIdx + "." + MpcUtils.SEQ_SUFFIX);
                    File dest = new File(targetProjDir, seqInfo.newIdx + "." + MpcUtils.SEQ_SUFFIX + "tmp");
                    FileUtils.copyFile(src, dest);
                    System.out.printf("Moved sequence '%s' from index '%s' to index '%s'%n", seqInfo.name, seqInfo.currentIdx, seqInfo.newIdx);
                } else {
                    System.out.printf("Sequence '%s' keeps index '%s'%n", seqInfo.name, seqInfo.currentIdx);
                }
            }

            mpcProject.writeDocument(targetProjDir);

            Collection<File> seqFiles = FileUtils.listFiles(targetProjDir, new String[]{MpcUtils.SEQ_SUFFIX + "tmp"}, false);
            for (File src : seqFiles) {
                Path fileToMovePath = Paths.get(src.getPath());
                Path targetPath = Paths.get(StringUtils.substringBefore(src.getPath(), "tmp"));
                Files.move(fileToMovePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
