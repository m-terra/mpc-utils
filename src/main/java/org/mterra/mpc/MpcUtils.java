package org.mterra.mpc;

import org.mterra.mpc.seq.Reorderer;

import java.util.Objects;

public class MpcUtils {

    public static final String PROJECT_FOLDER_SUFFIX = "_[ProjectData]";
    public static final String ALL_SEQS_FILE_NAME = "All Sequences & Songs.xal";
    public static final String SEQ_SUFFIX = "sxq";

    public static void main(String[] args) {
        String cmd = args[0];

        switch (cmd) {
            case "reorderSeqs":
                assert (args.length == 3);
                String srcDir = args[1];
                String targetDir = args[2];
                System.out.printf("Command '%s' srcDir '%s' targetDir '%s'%n", cmd, srcDir, targetDir);
                assert (!Objects.equals(srcDir, targetDir));
                Reorderer.inPath(srcDir, targetDir);
                break;
            default:
                printUsage(cmd);
        }

    }

    private static void printUsage(String cmd) {
        System.out.printf("Command not supported: %s%n", cmd);
    }

}
