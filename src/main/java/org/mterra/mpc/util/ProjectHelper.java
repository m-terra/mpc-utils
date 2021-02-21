package org.mterra.mpc.util;

import org.apache.commons.io.FileUtils;
import org.mterra.mpc.MpcUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectHelper {

    public static void copyProject(File srcDir, File targetDir, String projectName) {
        try {
            targetDir.createNewFile();

            Path orig = Paths.get(srcDir.getParentFile().getPath(), projectName + ".xpj");
            Path target = Paths.get(targetDir.getPath(), projectName + ".xpj");
            FileUtils.copyFile(orig.toFile(), target.toFile());

            orig = Paths.get(srcDir.getParentFile().getPath(), projectName + MpcUtils.PROJECT_FOLDER_SUFFIX);
            target = Paths.get(targetDir.getPath(), projectName + MpcUtils.PROJECT_FOLDER_SUFFIX);
            FileUtils.copyDirectory(orig.toFile(), target.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
