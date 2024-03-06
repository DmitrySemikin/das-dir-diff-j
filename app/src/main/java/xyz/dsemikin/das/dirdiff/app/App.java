package xyz.dsemikin.das.dirdiff.app;

import xyz.dsemikin.das.dirdiff.app.args.ArgsParser;
import xyz.dsemikin.das.dirdiff.app.args.SubCmdArgs;
import xyz.dsemikin.das.dirdiff.app.args.SubCmdArgsCollectDirFsItemMd5Sums;
import xyz.dsemikin.das.dirdiff.app.args.SubCmdArgsCollectDirFsItems;
import xyz.dsemikin.das.dirdiff.app.args.SubCmdArgsCompareDirsFsItemsByName;
import xyz.dsemikin.das.dirdiff.app.cmdimpl.SubCmdImplCollectDirFsItemMd5Subs;
import xyz.dsemikin.das.dirdiff.app.cmdimpl.SubCmdImplCollectDirFsItemNames;
import xyz.dsemikin.das.dirdiff.app.cmdimpl.SubCmdImplCompareDirsFsItemsByName;

public class App {
    public static void main(String[] args) {

        final ArgsParser argsParser;
        try {
            argsParser = ArgsParser.parseArgs(args);
        } catch (Exception e) {
            // TODO: remove it, when dealing with --help etc is stabilied.
            e.printStackTrace();
            ArgsParser.printUsage();
            return;
        }

        final SubCmdArgs subcommand = argsParser.getSubcommand();
        if (subcommand instanceof SubCmdArgsCollectDirFsItems subCmdArgsCollectDirFsItems) {
            new SubCmdImplCollectDirFsItemNames().run(subCmdArgsCollectDirFsItems);
        } else if (subcommand instanceof SubCmdArgsCompareDirsFsItemsByName subCmdArgsCompareDirsFsItemsByName) {
            new SubCmdImplCompareDirsFsItemsByName().run(subCmdArgsCompareDirsFsItemsByName);
        } else if (subcommand instanceof SubCmdArgsCollectDirFsItemMd5Sums subCmdArgsCollectDirFsItemMd5Sums) {
            new SubCmdImplCollectDirFsItemMd5Subs().run(subCmdArgsCollectDirFsItemMd5Sums);
        } else {
            throw new IllegalStateException("Unknown subcommand type: " + subcommand.getClass());
        }
    }
}
