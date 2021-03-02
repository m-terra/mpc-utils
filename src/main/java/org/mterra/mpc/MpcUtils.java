package org.mterra.mpc;

import org.mterra.mpc.service.ServiceDispatcher;
import org.mterra.mpc.util.Helper;

import java.util.Objects;

public class MpcUtils {

    public static final String PROJECT_FOLDER_SUFFIX = "_[ProjectData]";
    public static final String ALL_SEQS_FILE_NAME = "All Sequences & Songs.xal";
    public static final String SEQ_SUFFIX = "sxq";
    public static final String PROJ_SUFFIX = "xpj";

    public static void main(String[] args) {
        String cmd = args[0];
        if (args.length < 3) {
            printUsage();
        }
        String srcDir = args[1];
        String targetDir = args[2];
        if (Objects.equals(srcDir, targetDir)) {
            System.out.printf("<scanDirectory> <targetDirectory> must be different%n");
        }
        Helper.createDirs(targetDir);
        System.out.printf("Executing command '%s' srcDir '%s' targetDir '%s'%n", cmd, srcDir, targetDir);
        ServiceDispatcher service = new ServiceDispatcher();
        if ("reorder".equalsIgnoreCase(cmd)) {
            String songNumber = "1";
            if (args.length == 4) {
                songNumber = args[3];
            }
            service.reorder(srcDir, targetDir, songNumber);
        } else if ("filter".equalsIgnoreCase(cmd)) {
            String sequenceName = "Live";
            if (args.length == 4) {
                sequenceName = args[3];
            }
            service.livesets(srcDir, targetDir, sequenceName);
        } else {
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.printf("java -jar <mpc-utils-jar> <command> <scanDirectory> <targetDirectory> [songNumber|sequenceName]%n");
        System.out.printf("See README.md%n");
    }

}
