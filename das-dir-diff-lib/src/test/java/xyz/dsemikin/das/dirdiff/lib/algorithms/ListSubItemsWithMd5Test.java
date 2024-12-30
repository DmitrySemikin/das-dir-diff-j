package xyz.dsemikin.das.dirdiff.lib.algorithms;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.dsemikin.das.dirdiff.lib.testtools.TestFilesAndDirsInfo;
import xyz.dsemikin.das.dirdiff.lib.testtools.TestFilesAndDirsProvider;
import xyz.dsemikin.das.dirdiff.lib.testtools.XyzTestFilesUtils;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListSubItemsWithMd5Test {

    // TODO:
    // What we actually want to test here?
    // * Simple win is - getters
    //   * getRootDir
    //   * getAbortOnError
    //   * getTimer
    //   * getFsItemConsumer
    //   * getExcludedDirConfig
    //   * getSkippedDirExcluded
    //   * getSkippedDirNoAccess
    // * Actual matter of the tests is the constructor. For it we need to test following:
    //   * Create the tmp dir with known content
    //   * Run the algorithm in it. Check that the fsItemConsumer was called for each fsItem in the dir (and only for them)
    //   * Run it with excludes. Check, that excludes were excluded
    //   * NOTICE: abortOnAccessError we don't test automatically, because it is not clear, how to organize it.
    //     To test it manually one can run the algorithm on a usb drive with NTFS - the system information dir
    //     there cannot be accessed and it causes such an error.
    //   * Test that timer was triggered (e.g. that the time span has not zero length).


    // TODO: assertions are in: static org.junit.jupiter.api.Assertions.*;

    private static final boolean dontAbortOnAccessError = false; // false means "don't abort". It is always false - we don't test this option in this class

    // these are replaced before execution of each test method
    private TestFilesAndDirsProvider testFilesAndDirsProvider;

    @BeforeEach
    void setUp() {
        testFilesAndDirsProvider = new TestFilesAndDirsProvider();
    }

    @AfterEach
    void tearDown() {
        XyzTestFilesUtils.deleteDirRecursively(testFilesAndDirsProvider.getRootDir());
    }

    @Test
    void listSubItemsWithMd5Test() {

        final TestFilesAndDirsInfo testFilesAndDirsInfo = testFilesAndDirsProvider.generateTestFilesAndDirs();
        final ConsumerMock fsItemConsumer = new ConsumerMock();

        final List<Path> excludeDirsConfig = testFilesAndDirsInfo.getExcludeDirsConfig();
        final ListSubItemsWithMd5 results = new ListSubItemsWithMd5(
                testFilesAndDirsInfo.getRootDir(),
                excludeDirsConfig,
                dontAbortOnAccessError,
                fsItemConsumer
        );

        Assertions.assertEquals(testFilesAndDirsInfo.getRootDir(), results.getRootDir());
        Assertions.assertEquals(dontAbortOnAccessError, results.getAbortOnError());
        Assertions.assertSame(fsItemConsumer, results.getFsItemConsumer());
        Assertions.assertEquals(excludeDirsConfig, results.getExcludeDirConfig());

        Assertions.assertTrue(results.getSkippedDirsNoAccess().isEmpty());

        Assertions.assertEquals(testFilesAndDirsInfo.getCountOfDirsToBeConsumed(), fsItemConsumer.getConsumedDirsCount());
        Assertions.assertEquals(testFilesAndDirsInfo.getCountOfFilesToBeConsumed(), fsItemConsumer.getConsumedFilesCount());
        Assertions.assertEquals(testFilesAndDirsInfo.getCountOfFsItemsToBeConsumed(), fsItemConsumer.getConsumedFsItemsTotalCount());
        Assertions.assertEquals(testFilesAndDirsInfo.getFilesToBeConsumed(), fsItemConsumer.GetConsumedFiles());
        Assertions.assertEquals(testFilesAndDirsInfo.getDirsToBeConsumed(), fsItemConsumer.GetConsumedDirs());
        Assertions.assertTrue(fsItemConsumer.GetConsumedFiles().stream().noneMatch(consumedFile -> testFilesAndDirsInfo.getFilesToBeSkipped().contains(consumedFile)));
        Assertions.assertTrue(fsItemConsumer.GetConsumedDirs().stream().noneMatch(consumedDir -> testFilesAndDirsInfo.getDirsToBeSkipped().contains(consumedDir)));
    }

    private static class ConsumerMock implements FSItemWithMd5Consumer {

        final Set<Path> consumedFiles = new HashSet<>();
        final Set<Path> consumedDirs = new HashSet<>();

        @Override
        public void consumeFsItemWithMd5(FSItemWithMd5 fsItem) {
            switch (fsItem.getKind()) {
                case FS_ITEM_DIR -> consumedDirs.add(fsItem.getRelativePath());
                case FS_ITEM_FILE -> consumedFiles.add(fsItem.getRelativePath());
                default -> throw new RuntimeException("Unsupported FS Item kind: " + fsItem.getKind());
            }
        }

        @Override
        public long getConsumedFsItemsTotalCount() {
            return getConsumedDirsCount() + getConsumedFilesCount();
        }

        @Override
        public long getConsumedDirsCount() {
            return consumedDirs.size();
        }

        @Override
        public long getConsumedFilesCount() {
            return consumedFiles.size();
        }

        @Override
        public void close() {
        }

        public Set<Path> GetConsumedFiles() {
            return consumedFiles;
        }

        public Set<Path> GetConsumedDirs() {
            return consumedDirs;
        }

    }
}
