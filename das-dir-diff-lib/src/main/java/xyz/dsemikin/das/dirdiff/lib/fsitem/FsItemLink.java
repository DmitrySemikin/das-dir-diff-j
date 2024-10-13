package xyz.dsemikin.das.dirdiff.lib.fsitem;

import java.nio.file.Path;

public class FsItemLink extends FsItem {
    public FsItemLink(final Path path) {
        super(path);
    }

    @Override
    public FsItemKind getKind() {
        return FsItemKind.FS_ITEM_LINK;
    }
}
