package xyz.dsemikin.das.dirdiff.app.cmdimpl;

import xyz.dsemikin.das.dirdiff.app.args.SubCmdArgsCollectDirFsItemMd5Sums;
import xyz.dsemikin.das.dirdiff.lib.algorithms.FSItemWithMd5ConsumerFileWriter;
import xyz.dsemikin.das.dirdiff.lib.algorithms.ListSubItemsWithMd5;
import xyz.dsemikin.das.dirdiff.lib.reports.ListSubItemsWithMd5ResultsPrinter;

import java.io.IOException;

public class SubCmdImplCollectDirFsItemMd5Subs {
    public void run(final SubCmdArgsCollectDirFsItemMd5Sums subCmdArgsCollectDirFsItemMd5Sums) {
        try(final FSItemWithMd5ConsumerFileWriter fsItemConsumer = new FSItemWithMd5ConsumerFileWriter(
                subCmdArgsCollectDirFsItemMd5Sums.getOutputFilePath(),
                subCmdArgsCollectDirFsItemMd5Sums.isOverwrite())
        ) {
            final ListSubItemsWithMd5 results =
                new ListSubItemsWithMd5(
                        subCmdArgsCollectDirFsItemMd5Sums.getDirToInspect(),
                        subCmdArgsCollectDirFsItemMd5Sums.getExcludeDirectories(),
                        subCmdArgsCollectDirFsItemMd5Sums.isAbortOnAccessDenied(),
                        fsItemConsumer
                );
            new ListSubItemsWithMd5ResultsPrinter().print(
                    results,
                    subCmdArgsCollectDirFsItemMd5Sums.getOutputFilePath(),
                    subCmdArgsCollectDirFsItemMd5Sums.isOverwrite()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
