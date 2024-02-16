package xyz.dsemikin.das.dirdiff.lib;

import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItem;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CompareFsItemsOfTwoDirsByName {

    private final Map<Path, FsItem> dir1FsItems;
    private final Map<Path, FsItem> dir2FsItems;

    private final Set<Path> commonFsItemPaths;
    private final Set<Path> differentKindsFsItemPaths;
    private final Set<Path> dir1OnlyFsItemPaths;
    private final Set<Path> dir2OnlyFsItemPaths;

    public CompareFsItemsOfTwoDirsByName(final Path dir1, final Path dir2) {
        dir1FsItems = new FindFsSubItems(dir1).getFsItems();
        dir2FsItems = new FindFsSubItems(dir2).getFsItems();

        final Set<Path> dir1AllFsItemPaths = dir1FsItems.keySet();
        final Set<Path> dir2AllFsItemPaths = dir2FsItems.keySet();

        commonFsItemPaths = new HashSet<>();
        differentKindsFsItemPaths = new HashSet<>();
        dir1OnlyFsItemPaths = new HashSet<>(dir1AllFsItemPaths);
        dir2OnlyFsItemPaths = new HashSet<>(dir2AllFsItemPaths);

        for (final Path dir1FsItemPath : dir1AllFsItemPaths) {
            if (dir2OnlyFsItemPaths.remove(dir1FsItemPath)) {
                dir1OnlyFsItemPaths.remove(dir1FsItemPath);
                if (Objects.equals(dir1FsItems.get(dir1FsItemPath), dir2FsItems.get(dir1FsItemPath))) {
                    commonFsItemPaths.add(dir1FsItemPath);
                } else {
                    differentKindsFsItemPaths.add(dir1FsItemPath);
                }
            }
        }
    }

    public Map<Path, FsItem> getDir1FsItems() {
        return dir1FsItems;
    }

    public Map<Path, FsItem> getDir2FsItems() {
        return dir2FsItems;
    }

    public Set<Path> getCommonFsItemPaths() {
        return commonFsItemPaths;
    }

    public Set<Path> getDifferentKindsFsItemPaths() {
        return differentKindsFsItemPaths;
    }

    public Set<Path> getDir1OnlyFsItemPaths() {
        return dir1OnlyFsItemPaths;
    }

    public Set<Path> getDir2OnlyFsItemPaths() {
        return dir2OnlyFsItemPaths;
    }
}
