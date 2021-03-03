package org.mterra.mpc.model;

import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.MpcUtils;

import java.io.File;

public class ProjectInfo {

    private final String projectName;
    private final File projectDataFolder;
    private final File projectFile;
    private final File sequencesAndSongsFile;

    public ProjectInfo(File projectDataFolder) {
        this.projectName = StringUtils.substringBefore(projectDataFolder.getName(), MpcUtils.PROJECT_FOLDER_SUFFIX);
        this.projectDataFolder = projectDataFolder;
        this.projectFile = new File(projectDataFolder.getParentFile(), projectName + "." + MpcUtils.PROJ_SUFFIX);
        this.sequencesAndSongsFile = new File(projectDataFolder, MpcUtils.ALL_SEQS_FILE_NAME);
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

    public File getSequencesAndSongsFile() {
        return sequencesAndSongsFile;
    }
}
