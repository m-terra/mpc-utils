package org.mterra.mpc.service;

import org.apache.commons.io.FileUtils;
import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.model.SeqInfo;
import org.mterra.mpc.model.SequencesAndSongs;
import org.mterra.mpc.util.Constants;
import org.mterra.mpc.util.Helper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MpcUtilsService {


    public void reorderSequences(String scanDirPath, String targetDirPath, String songNumber, Boolean uniqueSeqs) {
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        File targetDir = new File(targetDirPath);
        for (ProjectInfo projectInfo : projects) {
            System.out.printf("Found project to reorder '%s'%n", projectInfo.getProjectName());
            Reorderer reorderer = new Reorderer();
            SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
            sequencesAndSongs.load(projectInfo, songNumber);
            Map<Integer, SeqInfo> reordered = reorderer.calculateNewOrder(sequencesAndSongs);
            Helper.copyProject(projectInfo, targetDir);
            reorderer.updateFiles(sequencesAndSongs, reordered, targetDir, projectInfo.getProjectName());
        }
    }

    public void filterProjects(String scanDirPath, String targetDirPath, String sequenceName) {
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        File targetDir = new File(targetDirPath);
        for (ProjectInfo projectInfo : projects) {
            SequencesAndSongs sequencesAndSongs = new SequencesAndSongs();
            sequencesAndSongs.load(projectInfo);
            if (sequencesAndSongs.containsSequence(sequenceName)) {
                System.out.printf("Found project '%s' with '%s' sequence%n", projectInfo.getProjectName(), sequenceName);
                Helper.copyProject(projectInfo, targetDir);
                Project project = new Project();
                project.load(projectInfo);
            }
        }
    }

    public void createProjectBpmFile(String scanDirPath) {
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        Map<String, String> bpmSongMap = new TreeMap<>();
        for (ProjectInfo projectInfo : projects) {
            Project project = new Project();
            project.load(projectInfo);
            System.out.printf("Found project '%s' with BPM '%s'%n", projectInfo.getProjectName(), project.getBpm());
            bpmSongMap.put(projectInfo.getProjectName(), project.getBpm());
        }
        if (!bpmSongMap.isEmpty()) {
            File bpmFile = new File(scanDirPath + "/" + Constants.DEFAULT_BPM_FILE_NAME);
            Helper.writeMapFile(bpmFile, bpmSongMap);
        }
    }

    public void configureProjectQLinks(String scanDirPath, String targetDirPath) {
        File targetDir = new File(targetDirPath);
        List<ProjectInfo> projects = Helper.getProjectsInDirectory(scanDirPath);
        for (ProjectInfo projectInfo : projects) {
            System.out.printf("Configuring project for project '%s'%n", projectInfo.getProjectName());
            QLinks QLinks = new QLinks(projectInfo);
            QLinks.configureProjectQLinks();
            Helper.copyProject(projectInfo, targetDir);
            QLinks.updateProjectFile(targetDir);
        }
    }

    public void createLiveset(String scanDirPath, String targetDirPath, String sequenceName, String songNumber, Boolean uniqueSeqs) {
        File filteredPath = new File(targetDirPath + "./filtered");
        File reorderedPath = new File(targetDirPath + "./reordered");
        if (!(filteredPath.mkdirs() && reorderedPath.mkdirs())) {
            System.out.printf("Unable to create staging subdirectories in directory '%s'%n", targetDirPath);
        }
        System.out.printf("Creating liveset for all filtered projects in directory '%s'%n", targetDirPath);
        filterProjects(scanDirPath, filteredPath.getPath(), sequenceName);
        reorderSequences(filteredPath.getPath(), reorderedPath.getPath(), songNumber, uniqueSeqs);
        configureProjectQLinks(reorderedPath.getPath(), targetDirPath);
        createProjectBpmFile(targetDirPath);
        try {
            FileUtils.deleteDirectory(filteredPath);
            FileUtils.deleteDirectory(reorderedPath);
        } catch (IOException e) {
            System.out.printf("Unable to delete staging subdirectories in directory '%s'%n", targetDirPath);
        }
    }
}
