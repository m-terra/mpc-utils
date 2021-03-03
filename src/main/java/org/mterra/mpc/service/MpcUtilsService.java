package org.mterra.mpc.service;

import org.mterra.mpc.model.Project;
import org.mterra.mpc.model.ProjectInfo;
import org.mterra.mpc.model.SeqInfo;
import org.mterra.mpc.model.SequencesAndSongs;
import org.mterra.mpc.util.Helper;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MpcUtilsService {


    public void reorderSequences(String scanDirPath, String targetDirPath, String songNumber) {
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
            File bpmFile = new File(scanDirPath + "/Project_BPM.txt");
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

}
