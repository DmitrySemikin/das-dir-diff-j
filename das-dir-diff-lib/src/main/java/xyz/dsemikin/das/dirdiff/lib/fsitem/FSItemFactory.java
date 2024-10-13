package xyz.dsemikin.das.dirdiff.lib.fsitem;

import java.nio.file.Files;
import java.nio.file.Path;

public class FSItemFactory {
    public FsItem createFsItemObjectForExistingFsItem(final Path path) {
        if (!Files.exists(path)) {
            throw new RuntimeException("While creating FSItem provided fs item with given path must exist. FS Item for path " + path + " does not exist.");
        }
        final FsItem fsItem;
        if (Files.isDirectory(path)) {
            fsItem = new FsItemDir(path);
        } else if (Files.isRegularFile(path)) {
            fsItem = new FsItemFile(path);
        } else if (Files.isSymbolicLink(path)) {
            fsItem = new FsItemLink(path);
        } else {
            throw new RuntimeException("Unsupported/unknown FS-Item kind (not one of: dir, file, link): " + path);
        }
        return fsItem;
    }
}
