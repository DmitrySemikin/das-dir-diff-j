package xyz.dsemikin.das.dirdiff.lib.algorithms;

import java.io.Closeable;

public interface FSItemWithMd5Consumer extends Closeable {

    void consumeFsItemWithMd5(FSItemWithMd5 fsItem);

    long getConsumedFsItemsTotalCount();
    long getConsumedDirsCount();
    long getConsumedFilesCount();
}
