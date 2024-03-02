package org.mterra.mpc.service;

import org.apache.commons.io.FileUtils;
import org.mterra.mpc.model.*;
import org.mterra.mpc.util.Constants;
import org.mterra.mpc.util.Helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MpcUtilsService {
    public void reorderSequences(String scanDirPath, String targetDirPath, String songNumber, Boolean uniqueSeqs, boolean liveFirst) {
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        File targetDir = new File(targetDirPath);
        for (ProjectInfo projectInfo : projects) {
            System.out.printf("Reordering project '%s' by songNumber '%s'%n", projectInfo.getProjectName(), songNumber);
            Reorderer reorderer = new Reorderer();
            SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
            sequencesAndSongs.load(projectInfo, songNumber);
            Map<Integer, SeqInfo> reordered = reorderer.reorderSequences(sequencesAndSongs, uniqueSeqs, liveFirst);
            reorderer.writeReorderedProject(projectInfo, sequencesAndSongs, reordered, targetDir);
        }
    }

    public int filterProjects(String scanDirPath, String targetDirPath) {
        int loaded = 0;
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        File targetDir = new File(targetDirPath);
        for (ProjectInfo projectInfo : projects) {
            SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
            sequencesAndSongs.load(projectInfo);
            if (sequencesAndSongs.containsSequenceWithPrefix(Constants.LIVE_SEQUENCE_BASIC)) {
                System.out.printf("Project '%s' matches sequence filter %n", projectInfo.getProjectName());
                Helper.copyProject(projectInfo, targetDir);
                Project project = new Project();
                project.load(projectInfo);
                loaded++;
            }
        }
        return loaded;
    }

    public void createProjectBpmFile(String scanDirPath, String outputDirectoryPath) {
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        List<Bpm> bpms = new ArrayList<>();
        for (ProjectInfo projectInfo : projects) {
            Project project = new Project();
            project.load(projectInfo);
            System.out.printf("Found project '%s' with BPM '%s'%n", projectInfo.getProjectName(), project.getBpm());
            bpms.add(new Bpm(Double.parseDouble(project.getBpm()), projectInfo.getProjectName()));
        }
        if (!bpms.isEmpty()) {
            File bpmFile = new File(outputDirectoryPath + "/" + Constants.DEFAULT_BPM_FILE_NAME);
            Collections.sort(bpms);
            Helper.writeBpmFile(bpmFile, bpms);
        }
    }

    public void configureProjectQLinkMap(String scanDirPath, String targetDirPath) {
        File targetDir = new File(targetDirPath);
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        for (ProjectInfo projectInfo : projects) {
            System.out.printf("Configuring project QLink map for project '%s'%n", projectInfo.getProjectName());
            QLinks QLinks = new QLinks(projectInfo);
            QLinks.configureProjectQLinks();
            Helper.copyProject(projectInfo, targetDir);
            QLinks.updateProjectFile(targetDir);
        }
    }

    public void configureQLinkMode(String scanDirPath, String targetDirPath, String qlinkMode) {
        File targetDir = new File(targetDirPath);
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        for (ProjectInfo projectInfo : projects) {
            System.out.printf("Configuring QLink mode '%s' for project '%s'%n", qlinkMode, projectInfo.getProjectName());
            QLinks QLinks = new QLinks(projectInfo);
            QLinks.configureQLinkMode(qlinkMode);
            Helper.copyProject(projectInfo, targetDir);
            QLinks.updateProjectFile(targetDir);
        }
    }

    public void configureArpSettings(String scanDirPath, String targetDirPath) {
        File targetDir = new File(targetDirPath);
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        for (ProjectInfo projectInfo : projects) {
            if (projectInfo.hasProjectSettingsFile()) {
                System.out.printf("Configuring ARP Settings for project '%s'%n", projectInfo.getProjectName());
                ProjectSettings projectSettings = new ProjectSettings(projectInfo);
                projectSettings.configureArpLiveSettings();
                Helper.copyProject(projectInfo, targetDir);
                projectSettings.updateProjectSettingsFile(targetDir);
            } else {
                System.out.printf("Skipping ARP Settings for project '%s'%n", projectInfo.getProjectName());
                Helper.copyProject(projectInfo, targetDir);
            }
        }
    }

    public void copyToSpecificLivesetTargets(String srcPath, String targetDirPath) {
        File targetDir = new File(targetDirPath);
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(srcPath);
        for (ProjectInfo projectInfo : projects) {
            SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
            sequencesAndSongs.load(projectInfo);
            String targetSubDir = sequencesAndSongs.getLivesetSuffix();
            if (targetSubDir != null) {
                Helper.copyProject(projectInfo, new File(targetDir, targetSubDir));
            }
        }
    }

    public void createLiveset(String scanDirPath, String targetDirPath, String songNumber, Boolean uniqueSeqs, String qlinkMode) {
        List<File> stagingDirs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            File dir = new File(targetDirPath + "/" + i);
            stagingDirs.add(i, dir);
            if (!dir.mkdirs()) {
                System.out.printf("Unable to create staging subdirectories in directory '%s'%n", targetDirPath);
                return;
            }
        }

        System.out.printf("Creating liveset for projects in directory '%s'%n", scanDirPath);
        if (filterProjects(scanDirPath, stagingDirs.get(0).getPath()) < 1) {
            System.out.printf("Sequence filter returned no projects %n");
        } else {
            reorderSequences(stagingDirs.get(0).getPath(), stagingDirs.get(1).getPath(), songNumber, uniqueSeqs, true);
            configureQLinkMode(stagingDirs.get(1).getPath(), stagingDirs.get(2).getPath(), qlinkMode);
            configureProjectQLinkMap(stagingDirs.get(2).getPath(), stagingDirs.get(3).getPath());
            configureArpSettings(stagingDirs.get(3).getPath(), stagingDirs.get(4).getPath());
            copyToSpecificLivesetTargets(stagingDirs.get(4).getPath(), targetDirPath);
        }

        try {
            for (File dir : stagingDirs) {
                FileUtils.deleteDirectory(dir);
            }
        } catch (IOException e) {
            System.out.printf("Unable to delete staging subdirectories in directory '%s'%n", targetDirPath);
        }

        File[] scanDirs = new File(targetDirPath).listFiles();
        if (scanDirs != null) {
            for (File dir : scanDirs) {
                if (dir.isDirectory()) {
                    createProjectBpmFile(dir.getPath(), dir.getPath());
                }
            }
        }

    }
}
