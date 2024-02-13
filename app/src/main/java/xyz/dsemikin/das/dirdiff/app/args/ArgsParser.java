package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.JCommander;

public class ArgsParser {

    private final CommonArgs commonArgs;
    private final CommandInspectDir commandInspectDir;
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

    public CmdSubcommand getSubcommand() {
        final String parsedCommand = jCommander.getParsedCommand();
        //noinspection SwitchStatementWithTooFewBranches
        return switch (parsedCommand) {
            case CommandInspectDir.COMMAND_NAME -> commandInspectDir;
            default -> throw new IllegalStateException("Unexpected value: " + parsedCommand);
        };
    }

    private ArgsParser() {
        commonArgs = new CommonArgs();
        commandInspectDir = new CommandInspectDir();
        jCommander = JCommander.newBuilder()
                .addObject(commonArgs)
                .addCommand(CommandInspectDir.COMMAND_NAME, commandInspectDir)
                .build();
    }


}
