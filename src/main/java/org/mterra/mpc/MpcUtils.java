package org.mterra.mpc;

import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.seq.MpcProject;
import org.mterra.mpc.seq.Reorderer;
import org.mterra.mpc.seq.SeqInfo;
import org.mterra.mpc.util.ProjectHelper;

import java.io.File;
import java.util.Map;
import java.util.Objects;

public class MpcUtils {

    public static final String PROJECT_FOLDER_SUFFIX = "_[ProjectData]";
    public static final String ALL_SEQS_FILE_NAME = "All Sequences & Songs.xal";
    public static final String SEQ_SUFFIX = "sxq";

    public static void main(String[] args) {
        assert (args.length == 2);
        String srcDir = args[0];
        String targetDir = args[1];
        assert (!Objects.equals(srcDir, targetDir));
        System.out.printf("srcDir '%s' targetDir '%s'%n", srcDir, targetDir);
        reorder(srcDir, targetDir);
    }

    private static void reorder(String scanDirPath, String targetDirPath) {
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
                Map<Integer, SeqInfo> reordered = inst.calculateNewOrder(mpcProject);
                ProjectHelper.copyProject(srcDir, targetDir, projectName);
                inst.updateFiles(mpcProject, reordered, targetDir, projectName);
            }
        }
    }

}
