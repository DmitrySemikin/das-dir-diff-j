package xyz.dsemikin.das.dirdiff.lib;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItem;
import xyz.dsemikin.das.dirdiff.lib.testtools.CreateSampleRootDir;
import xyz.dsemikin.das.dirdiff.lib.testtools.XyzTestFilesUtils;

import java.nio.file.Path;

class FindFsSubItemsTest {
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
    void findSubItemsTest() {
        System.out.println("Root Dir:");
        System.out.println(rootDir);
        final FindFsSubItems findResults = new FindFsSubItems(rootDir);
        for (FsItem fsItem : findResults.getFsItems().values()) {
            // TODO: Actually we need to compare the results with the expected dataset and not just print it out.
            System.out.println(fsItem.getKind().toString() + " : " + fsItem.getPath());
        }
    }
}