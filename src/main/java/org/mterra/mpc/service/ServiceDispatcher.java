package org.mterra.mpc.service;

import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.MpcUtils;
import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.SeqInfo;
import org.mterra.mpc.model.SequencesAndSongs;
import org.mterra.mpc.util.Helper;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class ServiceDispatcher {


    public void reorder(String scanDirPath, String targetDirPath, String songNumber) {
        File scanDir = new File(scanDirPath);
        File targetDir = new File(targetDirPath);
        for (File srcDir : scanDir.listFiles()) {
            if (srcDir.getName().endsWith(MpcUtils.PROJECT_FOLDER_SUFFIX)) {
                String projectName = StringUtils.substringBefore(srcDir.getName(), MpcUtils.PROJECT_FOLDER_SUFFIX);
                System.out.printf("Found project to reorder '%s'%n", projectName);
                Reorderer inst = new Reorderer();
                SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
                sequencesAndSongs.load(srcDir, songNumber);
                Map<Integer, SeqInfo> reordered = inst.calculateNewOrder(sequencesAndSongs);
                Helper.copyProject(srcDir, targetDir, projectName);
                inst.updateFiles(sequencesAndSongs, reordered, targetDir, projectName);
            }
        }
    }

    public void livesets(String scanDirPath, String targetDirPath, String sequenceName) {
        File scanDir = new File(scanDirPath);
        File targetDir = new File(targetDirPath);
        Map<String, String> bpmSongMap = new TreeMap<>();
        for (File srcDir : scanDir.listFiles()) {
            if (srcDir.getName().endsWith(MpcUtils.PROJECT_FOLDER_SUFFIX)) {
                String projectName = StringUtils.substringBefore(srcDir.getName(), MpcUtils.PROJECT_FOLDER_SUFFIX);
                SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
                sequencesAndSongs.load(srcDir);
                if (sequencesAndSongs.containsSequence(sequenceName)) {
                    System.out.printf("Found project '%s' with '%s' sequence%n", sequenceName, projectName);
                    Helper.copyProject(srcDir, targetDir, projectName);
                    Project project = new Project();
                    project.load(srcDir.getParentFile(), projectName);
                    bpmSongMap.put(project.getBpm(), projectName);
                }
            }
        }
        if (!bpmSongMap.isEmpty()) {
            File bpmFile = new File(targetDir + "/Project_BPM.txt");
            Helper.writeMapFile(bpmFile, bpmSongMap);
        }
    }

}
