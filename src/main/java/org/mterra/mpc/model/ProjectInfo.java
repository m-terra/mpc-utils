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
        this.sequencesAndSongsFile = new File(projectDataFolder, Constants.ALL_SEQS_FILE_NAME);
        File projSett = new File(projectDataFolder, Constants.PROJECT_SETTINGS_FILE_NAME);
        File padSett = new File(projectDataFolder, Constants.PADPERFORM_SETTINGS_FILE_NAME);

        if (projSett.exists()) {
            this.projectSettingsFile = projSett;
        } else if (projSett.exists()) {
            this.projectSettingsFile = padSett;
        } else {
            this.projectSettingsFile = null;
        }

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

    public boolean hasProjectSettingsFile() {
        return projectSettingsFile != null;
    }

    public File getSequencesAndSongsFile() {
        return sequencesAndSongsFile;
    }
}
