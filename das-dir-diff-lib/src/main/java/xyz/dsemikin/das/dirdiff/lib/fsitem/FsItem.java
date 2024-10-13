package xyz.dsemikin.das.dirdiff.lib.fsitem;

import java.nio.file.Path;

public abstract class FsItem {

    private final Path path;

    public FsItem(Path path) {
        this.path = path;
    }

    public abstract FsItemKind getKind();

    public Path getPath() {
        return path;
    }
}
