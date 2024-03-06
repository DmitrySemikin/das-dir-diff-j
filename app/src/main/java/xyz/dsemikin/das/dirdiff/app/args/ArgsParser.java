package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.JCommander;

public class ArgsParser {

    private final CommonArgs commonArgs;
    private final SubCmdArgsCollectDirFsItems subCmdArgsCollectDirFsItems;
    private final SubCmdArgsCompareDirsFsItemsByName subCmdArgsCompareDirsFsItemsByName;
    private final SubCmdArgsCollectDirFsItemMd5Sums subCmdArgsCollectDirFsItemMd5Sums;
    private final JCommander jCommander;

    public static void printUsage() {
        final ArgsParser argsParser = new ArgsParser();
        argsParser.jCommander.usage();
    }

    public static ArgsParser parseArgs(final String[] args) {
        final ArgsParser argsParser = new ArgsParser();
        argsParser.jCommander.parse(args);
        if (argsParser.jCommander.getParsedCommand() == null) {
            throw new RuntimeException("No command was supplied.");
        }
        return argsParser;
    }

    public CommonArgs getCommonArgs() {
        return commonArgs;
    }

    public SubCmdArgs getSubcommand() {
        final String parsedCommand = jCommander.getParsedCommand();
        return switch (parsedCommand) {
            case SubCmdArgsCollectDirFsItems.COMMAND_NAME -> subCmdArgsCollectDirFsItems;
            case SubCmdArgsCompareDirsFsItemsByName.COMMAND_NAME -> subCmdArgsCompareDirsFsItemsByName;
            case SubCmdArgsCollectDirFsItemMd5Sums.COMMAND_NAME -> subCmdArgsCollectDirFsItemMd5Sums;
            default -> throw new IllegalStateException("Unexpected value: " + parsedCommand);
        };
    }

    private ArgsParser() {
        commonArgs = new CommonArgs();
        subCmdArgsCollectDirFsItems = new SubCmdArgsCollectDirFsItems();
        subCmdArgsCompareDirsFsItemsByName = new SubCmdArgsCompareDirsFsItemsByName();
        subCmdArgsCollectDirFsItemMd5Sums = new SubCmdArgsCollectDirFsItemMd5Sums();
        jCommander = JCommander.newBuilder()
                .addObject(commonArgs)
                .addCommand(SubCmdArgsCollectDirFsItems.COMMAND_NAME, subCmdArgsCollectDirFsItems)
                .addCommand(SubCmdArgsCompareDirsFsItemsByName.COMMAND_NAME, subCmdArgsCompareDirsFsItemsByName)
                .addCommand(SubCmdArgsCollectDirFsItemMd5Sums.COMMAND_NAME, subCmdArgsCollectDirFsItemMd5Sums)
                .build();
    }


}
