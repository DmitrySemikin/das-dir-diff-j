package xyz.dsemikin.das.dirdiff.lib.algorithms;

public abstract class FSItemWithMd5ConsumerWithCounterAbstract implements FSItemWithMd5Consumer {

    private Long consumedDirsCount = 0L;
    private Long consumedFilesCount = 0L;

    @Override
    public final void consumeFsItemWithMd5(FSItemWithMd5 fsItem) {
        switch (fsItem.getKind()) {
            case FS_ITEM_DIR -> consumedDirsCount++;
            case FS_ITEM_FILE -> consumedFilesCount++;
            case FS_ITEM_LINK -> throw new RuntimeException("Links not supported.");
            default -> throw new RuntimeException("Unknown FS Item kind: " + fsItem.getKind());
        }
        consumeFsItemWithMd5Impl(fsItem);
    }

    abstract void consumeFsItemWithMd5Impl(FSItemWithMd5 fsItem);

    @Override
    public long getConsumedFsItemsTotalCount() {
        return consumedFilesCount + consumedDirsCount;
    }

    @Override
    public long getConsumedDirsCount() {
        return consumedDirsCount;
    }

    @Override
    public long getConsumedFilesCount() {
        return consumedFilesCount;
    }

}
