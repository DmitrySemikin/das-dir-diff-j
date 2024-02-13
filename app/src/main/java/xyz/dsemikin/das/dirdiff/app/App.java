package xyz.dsemikin.das.dirdiff.app;

import xyz.dsemikin.das.dirdiff.app.args.ArgsParser;
import xyz.dsemikin.das.dirdiff.app.args.CmdSubcommand;
import xyz.dsemikin.das.dirdiff.app.args.CommandInspectDir;
import xyz.dsemikin.das.dirdiff.lib.FindFsSubItems;

public class App {
    public static void main(String[] args) {

        final ArgsParser argsParser;
        try {
            argsParser = ArgsParser.parseArgs(args);
        } catch (Exception e) {
            // TODO:
            e.printStackTrace();
            ArgsParser.printUsage();
            return;
        }

        final CmdSubcommand subcommand = argsParser.getSubcommand();
        if (subcommand instanceof CommandInspectDir commandInspectDir) {
            final FindFsSubItems findResults = new FindFsSubItems(commandInspectDir.getDirToInspect());
            // TODO: We need some more advanced report here.
            findResults.getFsItems().forEach((path, fsItem) -> {
                System.out.println(fsItem.getKind().toString() + " --- " + path.toString());
            });
        } else {
            throw new IllegalStateException("Unknown subcommand type: " + subcommand.getClass());
        }
    }
}
