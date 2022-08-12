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

    public int filterProjects(String scanDirPath, String targetDirPath, String sequenceName) {
        int loaded = 0;
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        File targetDir = new File(targetDirPath);
        for (ProjectInfo projectInfo : projects) {
            SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
            sequencesAndSongs.load(projectInfo);
            if (sequencesAndSongs.containsSequence(sequenceName)) {
                System.out.printf("Project '%s' matches sequence filter '%s'%n", projectInfo.getProjectName(), sequenceName);
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

    public void createLiveset(String scanDirPath, String targetDirPath, String sequenceName, String songNumber,
                              Boolean uniqueSeqs, String qlinkMode) {
        File filteredPath = new File(targetDirPath + "/filtered");
        File reorderedPath = new File(targetDirPath + "/reordered");
        File qlinkModePath = new File(targetDirPath + "/qlink");
        if (!(filteredPath.mkdirs() && reorderedPath.mkdirs())) {
            System.out.printf("Unable to create staging subdirectories in directory '%s'%n", targetDirPath);
            return;
        }
        System.out.printf("Creating liveset for projects in directory '%s'%n", scanDirPath);
        if (filterProjects(scanDirPath, filteredPath.getPath(), sequenceName) < 1) {
            System.out.printf("Sequence filter '%s' returned no projects %n", sequenceName);
        } else {
            reorderSequences(filteredPath.getPath(), reorderedPath.getPath(), songNumber, uniqueSeqs, true);
            configureQLinkMode(reorderedPath.getPath(), qlinkModePath.getPath(), qlinkMode);
            configureProjectQLinkMap(qlinkModePath.getPath(), targetDirPath);
            createProjectBpmFile(targetDirPath, targetDirPath);
        }
        try {
            FileUtils.deleteDirectory(filteredPath);
            FileUtils.deleteDirectory(reorderedPath);
            FileUtils.deleteDirectory(qlinkModePath);
        } catch (IOException e) {
            System.out.printf("Unable to delete staging subdirectories in directory '%s'%n", targetDirPath);
        }
    }
}
