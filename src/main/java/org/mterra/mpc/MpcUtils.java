package org.mterra.mpc;

import org.apache.commons.cli.*;
import org.mterra.mpc.service.ServiceDispatcher;
import org.mterra.mpc.util.Helper;

import java.util.Objects;

public class MpcUtils {

    public static final String PROJECT_FOLDER_SUFFIX = "_[ProjectData]";
    public static final String ALL_SEQS_FILE_NAME = "All Sequences & Songs.xal";
    public static final String SEQ_SUFFIX = "sxq";
    public static final String PROJ_SUFFIX = "xpj";

    private static final String HELP = "java -jar <mpc-utils-jar>";

    public static void main(String[] args) {
        Options options = new Options();
        Option helpOpt = Option.builder("h").longOpt("help")
                .optionalArg(true).desc("show help").build();
        options.addOption(helpOpt);
        Option commandOpt = Option.builder("c").longOpt("command")
                .optionalArg(false).hasArg(true).desc("reorder|filter|bpm").build();
        options.addOption(commandOpt);
        Option inputDirOpt = Option.builder("i").longOpt("inputDirectory")
                .optionalArg(false).hasArg(true).desc("input directory path").build();
        options.addOption(inputDirOpt);
        Option outputDirOpt = Option.builder("o").longOpt("outputDirectory")
                .optionalArg(true).hasArg(true).desc("output directory path").build();
        options.addOption(outputDirOpt);
        Option songNumberOpt = Option.builder().longOpt("songNumber")
                .optionalArg(true).hasArg(true).desc("the songNumber to use").build();
        options.addOption(songNumberOpt);
        Option sequenceNameOpt = Option.builder().longOpt("sequenceName")
                .optionalArg(true).hasArg(true).desc("the sequenceName to use").build();
        options.addOption(sequenceNameOpt);

        CommandLine cmd = null;
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            new HelpFormatter().printHelp(HELP, options);
            System.exit(1);
        }

        if (cmd.hasOption(helpOpt.getOpt())) {
            new HelpFormatter().printHelp(HELP, options);
            return;
        }

        String command = cmd.getOptionValue(commandOpt.getOpt());
        String inputDirectoryPath = cmd.getOptionValue(inputDirOpt.getOpt());
        String outputDirectoryPath = cmd.getOptionValue(outputDirOpt.getOpt());

        if (inputDirectoryPath != null && Objects.equals(inputDirectoryPath, outputDirectoryPath)) {
            System.out.printf("%s %s must be different%n", inputDirOpt.getLongOpt(), outputDirOpt.getLongOpt());
            System.exit(1);
        }
        Helper.createDirs(outputDirectoryPath);
        System.out.printf("Executing command '%s' srcDir '%s' targetDir '%s'%n", command, inputDirectoryPath, outputDirectoryPath);
        ServiceDispatcher service = new ServiceDispatcher();

        switch (command) {
            case "reorder":
                String songNumber = cmd.getOptionValue(songNumberOpt.getLongOpt(), "1");
                service.reorder(inputDirectoryPath, outputDirectoryPath, songNumber);
                break;
            case "filter":
                String sequenceName = cmd.getOptionValue(sequenceNameOpt.getLongOpt(), "Live");
                service.filterProjects(inputDirectoryPath, outputDirectoryPath, sequenceName);
                break;
            case "bpm":
                service.createProjectBpmFile(inputDirectoryPath);
                break;
        }

    }

}
