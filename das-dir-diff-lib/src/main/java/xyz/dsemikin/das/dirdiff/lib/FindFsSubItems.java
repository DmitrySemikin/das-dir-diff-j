package xyz.dsemikin.das.dirdiff.lib;

import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItem;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItemDir;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItemFile;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItemLink;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FindFsSubItems {

    private final Path rootDir;
    private final Map<Path, FsItem> fsItems;

    public FindFsSubItems(final Path rootDir) {
        fsItems = new HashMap<>();
        this.rootDir = rootDir;
        if (!Files.isDirectory(rootDir)) {
            throw new RuntimeException("FindFsSubItems: rootDir parameter must be path of existing directory");
        }

        try (Stream<Path> pathStream = Files.walk(rootDir)) {
            pathStream.forEach(
                    path -> {
                        final Path relativePath = rootDir.relativize(path);
                        final FsItem fsItem;
                        if (Files.isDirectory(path)) {
                            fsItem = new FsItemDir(relativePath);
                        } else if (Files.isRegularFile(path)) {
                            fsItem = new FsItemFile(relativePath);
                        } else if (Files.isSymbolicLink(path)) {
                            fsItem = new FsItemLink(relativePath);
                        } else {
                            throw new RuntimeException("Unsupported/unknown FS-Item kind (not one of: dir, file, link): " + path);
                        }
                        fsItems.put(relativePath, fsItem);
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getRootDir() {
        return rootDir;
    }

    public Map<Path, FsItem> getFsItems() {
        return fsItems;
    }
}
