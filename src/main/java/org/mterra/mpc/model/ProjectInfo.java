package org.mterra.mpc.model;

import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.util.Constants;

import java.io.File;

public class ProjectInfo {

    private final String projectName;
    private final File projectDataFolder;
    private final File projectFile;
    private final File projectSettingsFile;

    private final File sequencesAndSongsFile;

    public ProjectInfo(File projectDataFolder) {
        this.projectName = StringUtils.substringBefore(projectDataFolder.getName(), Constants.PROJECT_FOLDER_SUFFIX);
        this.projectDataFolder = projectDataFolder;
        this.projectFile = new File(projectDataFolder.getParentFile(), projectName + "." + Constants.PROJ_SUFFIX);
        this.projectSettingsFile = new File(projectDataFolder, Constants.PROJECT_SETTINGS_FILE_NAME);
        this.sequencesAndSongsFile = new File(projectDataFolder, Constants.ALL_SEQS_FILE_NAME);
    }

    public String getProjectName() {
        return projectName;
    }

    public File getProjectDataFolder() {
        return projectDataFolder;
    }

    public File getProjectFile() {
        return projectFile;
    }

    public File getProjectSettingsFile() {
        return projectSettingsFile;
    }


    public File getSequencesAndSongsFile() {
        return sequencesAndSongsFile;
    }
}
