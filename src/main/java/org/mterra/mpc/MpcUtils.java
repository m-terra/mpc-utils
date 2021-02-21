package org.mterra.mpc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;

public class MpcUtils {

    public static void main(String[] args) {
        String cmd = args[0];

        switch (cmd) {
            case "reorderSeqs":
                assert (args.length == 3);
                String srcDir = args[1];
                String targetDir = args[2];
                System.out.printf("Command '%s' srcDir '%s' targetDir '%s'%n", cmd, srcDir, targetDir);
                assert (!Objects.equals(srcDir, targetDir));
                ReorderSeqs.inPath(srcDir, targetDir);
                break;
            default:
                printUsage(cmd);
        }

    }

    private static void printUsage(String cmd) {
        System.out.printf("Command not supported: %s%n", cmd);
    }

}
