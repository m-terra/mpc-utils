package org.mterra.mpc;

import org.mterra.mpc.seq.Reorderer;

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
        Reorderer.inPath(srcDir, targetDir);
    }

}
