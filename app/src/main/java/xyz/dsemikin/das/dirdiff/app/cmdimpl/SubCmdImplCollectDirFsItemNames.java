package xyz.dsemikin.das.dirdiff.app.cmdimpl;

import xyz.dsemikin.das.dirdiff.app.args.SubCmdArgsCollectDirFsItems;
import xyz.dsemikin.das.dirdiff.lib.algorithms.FindFsSubItems;

public class SubCmdImplCollectDirFsItemNames {
    public void run(final SubCmdArgsCollectDirFsItems subCmdArgsCollectDirFsItems) {
        final FindFsSubItems findResults = new FindFsSubItems(subCmdArgsCollectDirFsItems.getDirToInspect());
//        new DirFsItemsPrinter().print(findResults);
    }
}
