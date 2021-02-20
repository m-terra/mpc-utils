package org.mterra.mpc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class MpcUtils {

    public static void main(String[] args) {
        assert (args.length == 2);
        String cmd = args[0];
        String dir = args[1];

        System.out.printf("Command '%s' directory '%s'%n", cmd, dir);

        switch (cmd) {
            case "reorderSeqs":
                reorderSeqs(dir);
                break;
            default:
                printUsage(cmd);
        }

    }

    private static void printUsage(String cmd) {
        System.out.printf("Command not supported: %s%n", cmd);
    }

    private static void reorderSeqs(String dir) {
        for (File file : new File(dir).listFiles()) {
            System.out.printf("Checking %s%n", file.getName());
            if (file.getName().contains("_[ProjectData]")) {
                System.out.printf("Found project directory '%s'%n", file.getName());
                ReorderSeqs.inPath(file);
            }
        }

    }

}
