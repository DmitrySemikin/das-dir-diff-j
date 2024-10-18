package xyz.dsemikin.das.dirdiff.lib.algorithms;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.dsemikin.das.dirdiff.lib.testtools.CreateSampleRootDir;
import xyz.dsemikin.das.dirdiff.lib.testtools.XyzTestFilesUtils;

import java.nio.file.Path;

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

    private Path rootDir;

    @BeforeEach
    void setUp() {
        rootDir = CreateSampleRootDir.createSampleRootDir();
    }

    @AfterEach
    void tearDown() {
        XyzTestFilesUtils.deleteDirRecursively(rootDir);
    }

    @Test
    void listSubItesmWithMd5Test() {

        // TODO

    }
}
