package xyz.dsemikin.das.dirdiff.lib.fsitem;

import java.nio.file.Path;

public class FsItemDir extends FsItem {
    public FsItemDir(final Path path) {
        super(path);
    }

    @Override
    public FsItemKind getKind() {
        return FsItemKind.FS_ITEM_DIR;
    }
}
