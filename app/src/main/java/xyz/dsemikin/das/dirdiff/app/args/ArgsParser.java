package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.JCommander;

public class ArgsParser {

    public void parseArgs(final String[] args) {
        final CommonArgs commonArgs = new CommonArgs();
        final CommandInspectDir commandInspectDir = new CommandInspectDir();
        final JCommander jCommander = JCommander.newBuilder()
                .addObject(commonArgs)
                .addCommand(commandInspectDir)
                .build();
        jCommander.parse(args);

        jCommander.usage();
    }
}
