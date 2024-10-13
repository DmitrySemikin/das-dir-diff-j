package xyz.dsemikin.das.dirdiff.app.cmdimpl;

import xyz.dsemikin.das.dirdiff.app.args.SubCmdArgsCompareDirsFsItemsByName;
import xyz.dsemikin.das.dirdiff.lib.algorithms.CompareDirsFsItemsByName;
import xyz.dsemikin.das.dirdiff.lib.reports.ComparisonByNameResultsPrinter;

public class SubCmdImplCompareDirsFsItemsByName {
    public void run(final SubCmdArgsCompareDirsFsItemsByName subCmdArgsCompareDirsFsItemsByName) {
        // TODO: handle the case, when the directories are the same.
        final CompareDirsFsItemsByName comparisonResults = new CompareDirsFsItemsByName(
                subCmdArgsCompareDirsFsItemsByName.getDir1(),
                subCmdArgsCompareDirsFsItemsByName.getDir2()
        );
        new ComparisonByNameResultsPrinter().print(comparisonResults);
    }
}
