package xyz.dsemikin.das.dirdiff.app.cmdimpl;

import xyz.dsemikin.das.dirdiff.app.args.SubCmdArgsCollectDirFsItemMd5Sums;
import xyz.dsemikin.das.dirdiff.lib.ListSubItemsWithHashes;

public class SubCmdImplCollectDirFsItemMd5Subs {
    public void run(final SubCmdArgsCollectDirFsItemMd5Sums subCmdArgsCollectDirFsItemMd5Sums) {
        // TODO: use overwrite
        final ListSubItemsWithHashes findResults = new ListSubItemsWithHashes(subCmdArgsCollectDirFsItemMd5Sums.getDirToInspect(), subCmdArgsCollectDirFsItemMd5Sums.getOutputFilePath());
        // TODO: print out report.
    }
}
