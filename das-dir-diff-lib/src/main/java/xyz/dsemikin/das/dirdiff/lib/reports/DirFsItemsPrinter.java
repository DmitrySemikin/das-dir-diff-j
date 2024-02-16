package xyz.dsemikin.das.dirdiff.lib.reports;

import xyz.dsemikin.das.dirdiff.lib.FindFsSubItems;

public class DirFsItemsPrinter {
    public void print(final FindFsSubItems fsSubItems) {
        fsSubItems.getFsItems().forEach((path, fsItem) -> System.out.println(fsItem.getKind().toString() + " --- " + path.toString()));
    }
}
