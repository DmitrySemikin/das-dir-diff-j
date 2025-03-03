package xyz.dsemikin.das.dirdiff.lib.testtools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class SampleRootDirProvider implements AutoCloseable {

    public static final String TEST_ROOT_DIR_PREFIX = "das-dir-diff-lib-test";

    private Optional<Path> maybeRootDirPath;

    public SampleRootDirProvider() {
        try {
            final Path rootDirectory = Files.createTempDirectory(TEST_ROOT_DIR_PREFIX);
            maybeRootDirPath = Optional.of(rootDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getRootDir() {
        //noinspection OptionalGetWithoutIsPresent
        return maybeRootDirPath.get(); // we want it to throw, if it is empty
    }

    @Override
    public void close() {
        if (maybeRootDirPath.isPresent()) {
            final Path rootDir = maybeRootDirPath.get();
            if (Files.isDirectory(rootDir)) {
                XyzTestFilesUtils.deleteDirRecursively(rootDir);
            }
            maybeRootDirPath = Optional.empty();
        }
    }
}
