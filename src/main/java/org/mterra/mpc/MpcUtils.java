package org.mterra.mpc;

import org.apache.commons.cli.*;
import org.mterra.mpc.service.MpcUtilsService;
import org.mterra.mpc.util.Constants;
import org.mterra.mpc.util.Helper;

import java.util.Objects;

public class MpcUtils {

    public static void main(String[] args) {
        Options options = new Options();
        Option helpOpt = Option.builder("h").longOpt("help")
                .optionalArg(true).desc("show help").build();
        options.addOption(helpOpt);
        Option commandOpt = Option.builder("c").longOpt("command").required()
                .optionalArg(false).hasArg(true).desc("reorder|filter|bpm|qlinks|liveset").build();
        options.addOption(commandOpt);
        Option inputDirOpt = Option.builder("i").longOpt("inputDirectory").required()
                .optionalArg(false).hasArg(true).desc("input directory path").build();
        options.addOption(inputDirOpt);
        Option outputDirOpt = Option.builder("o").longOpt("outputDirectory")
                .optionalArg(false).hasArg(true).desc("output directory path").build();
        options.addOption(outputDirOpt);
        Option songNumberOpt = Option.builder().longOpt("songNumber")
                .optionalArg(false).hasArg(true).desc("optional songNumber to use for reordering").build();
        options.addOption(songNumberOpt);
        Option sequenceNameOpt = Option.builder().longOpt("sequenceName")
                .optionalArg(false).hasArg(true).desc("optional sequenceName to use for filtering").build();
        options.addOption(sequenceNameOpt);
        Option uniqueSequencesOpt = Option.builder().longOpt("uniqueSequences")
                .hasArg(false).desc("keeps the sequences unique when reordering").build();
        options.addOption(uniqueSequencesOpt);

        CommandLine cmd;
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            cmd = null;
        }

        if (cmd == null || cmd.hasOption(helpOpt.getOpt())) {
            printUsage(options);
            return;
        }

        String command = cmd.getOptionValue(commandOpt.getOpt());
        String inputDirectoryPath = cmd.getOptionValue(inputDirOpt.getOpt());
        String outputDirectoryPath = cmd.getOptionValue(outputDirOpt.getOpt());
        String songNumber = cmd.getOptionValue(songNumberOpt.getLongOpt(), Constants.DEFAULT_SONG_NUMBER);
        String sequenceName = cmd.getOptionValue(sequenceNameOpt.getLongOpt(), Constants.DEFAULT_FILTER_SEQUENCE_NAME);
        Boolean uniqueSequences = cmd.hasOption(uniqueSequencesOpt.getLongOpt());


        if (inputDirectoryPath != null && Objects.equals(inputDirectoryPath, outputDirectoryPath)) {
            System.out.printf("%s %s must be different%n", inputDirOpt.getLongOpt(), outputDirOpt.getLongOpt());
            System.exit(1);
        }
        Helper.createDirs(outputDirectoryPath);
        System.out.printf("Executing command '%s' srcDir '%s' targetDir '%s'%n", command, inputDirectoryPath, outputDirectoryPath);
        MpcUtilsService service = new MpcUtilsService();

        switch (command) {
            case "reorder":
                service.reorderSequences(inputDirectoryPath, outputDirectoryPath, songNumber, uniqueSequences);
                break;
            case "filter":
                service.filterProjects(inputDirectoryPath, outputDirectoryPath, sequenceName);
                break;
            case "bpm":
                service.createProjectBpmFile(inputDirectoryPath);
                break;
            case "qlinks":
                service.configureProjectQLinks(inputDirectoryPath, outputDirectoryPath);
                break;
            case "liveset":
                service.createLiveset(inputDirectoryPath, outputDirectoryPath, sequenceName, songNumber, uniqueSequences);
                break;
            default:
                printUsage(options);
        }
    }

    private static void printUsage(Options options) {
        new HelpFormatter().printHelp("java -jar <mpc-utils-jar>", options);
    }

}
