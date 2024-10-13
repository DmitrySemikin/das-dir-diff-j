package xyz.dsemikin.das.dirdiff.lib.fsitem;

import java.nio.file.Path;

public class FsItemFile extends FsItem {
    public FsItemFile(final Path path) {
        super(path);
    }

    @Override
    public FsItemKind getKind() {
        return FsItemKind.FS_ITEM_FILE;
    }
}
