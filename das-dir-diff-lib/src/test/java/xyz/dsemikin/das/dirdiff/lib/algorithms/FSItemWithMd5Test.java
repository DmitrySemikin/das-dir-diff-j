package xyz.dsemikin.das.dirdiff.lib.algorithms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.dsemikin.das.dirdiff.lib.testtools.SampleFsItemWithMd5InputProvider;

class FSItemWithMd5Test {

    // What we want to test:
    // - create a file with predefined content. Create FSItem out of it. Compare MD5 with pre-calculated value
    // - same with dir - md5 should be empty
    // -- in both cases test also other properties: root dir, absolute path, relative path, kind
    // (don't test for link - we don't support for now, especially on Windows).

    @Test
    void fileFsItemWithMd5Test() {
        try (final SampleFsItemWithMd5InputProvider sampleFSItemInputProvider = SampleFsItemWithMd5InputProvider.file()) {
            final FSItemWithMd5 fsItemWithMd5 = new FSItemWithMd5(sampleFSItemInputProvider.getLocalPath(), sampleFSItemInputProvider.getRootDir());
            Assertions.assertEquals(sampleFSItemInputProvider.getKind(), fsItemWithMd5.getKind());
            Assertions.assertEquals(sampleFSItemInputProvider.getRootDir(), fsItemWithMd5.getRootDir());
            Assertions.assertEquals(sampleFSItemInputProvider.getLocalPath(), fsItemWithMd5.getRelativePath());
            Assertions.assertEquals(sampleFSItemInputProvider.getRootDir().resolve(sampleFSItemInputProvider.getLocalPath()), fsItemWithMd5.getAbsolutePath());
            Assertions.assertEquals(sampleFSItemInputProvider.getExpectedMd5(), fsItemWithMd5.getMd5());
        }
    }

    @Test
    void directoryFsItemWithMd5Test() {
        try (final SampleFsItemWithMd5InputProvider sampleFsItemInputProvider = SampleFsItemWithMd5InputProvider.directory()) {
            final FSItemWithMd5 fsItemWithMd5 = new FSItemWithMd5(sampleFsItemInputProvider.getLocalPath(), sampleFsItemInputProvider.getRootDir());
            Assertions.assertEquals(sampleFsItemInputProvider.getKind(), fsItemWithMd5.getKind());
            Assertions.assertEquals(sampleFsItemInputProvider.getRootDir(), fsItemWithMd5.getRootDir());
            Assertions.assertEquals(sampleFsItemInputProvider.getLocalPath(), fsItemWithMd5.getRelativePath());
            Assertions.assertEquals(sampleFsItemInputProvider.getRootDir().resolve(sampleFsItemInputProvider.getLocalPath()), fsItemWithMd5.getAbsolutePath());
            Assertions.assertEquals(sampleFsItemInputProvider.getExpectedMd5(), fsItemWithMd5.getMd5());
        }
    }
}