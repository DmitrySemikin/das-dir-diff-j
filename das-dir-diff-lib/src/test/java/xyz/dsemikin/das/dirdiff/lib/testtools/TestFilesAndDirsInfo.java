package xyz.dsemikin.das.dirdiff.lib.testtools;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class TestFilesAndDirsInfo {

    private final List<Path> excludeDirsConfig;
    private final Path rootDir;
    private final Set<Path> dirsToBeConsumed;
    private final Set<Path> dirsToBeSkipped;
    private final Set<Path> filesToBeConsumed;
    private final Set<Path> filesToBeSkipped;

    public TestFilesAndDirsInfo(
            final List<Path> excludeDirsConfig,
            final Path rootDir,
            final Set<Path> dirsToBeConsumed,
            final Set<Path> dirsToBeSkipped,
            final Set<Path> filesToBeConsumed,
            final Set<Path> filesToBeSkipped
    ) {
        this.excludeDirsConfig = excludeDirsConfig;
        this.rootDir = rootDir;
        this.dirsToBeConsumed = dirsToBeConsumed;
        this.dirsToBeSkipped = dirsToBeSkipped;
        this.filesToBeConsumed = filesToBeConsumed;
        this.filesToBeSkipped = filesToBeSkipped;
    }

    public List<Path> getExcludeDirsConfig() {
        return excludeDirsConfig;
    }

    public Path getRootDir() {
        return rootDir;
    }

    // TODO: We actually don't need to check this, if we check the content of the Sets
    public long getCountOfDirsToBeConsumed() {
        return dirsToBeConsumed.size();
    }

    // TODO: We actually don't need to check this, if we check the content of the Sets
    public long getCountOfFilesToBeConsumed() {
        return filesToBeConsumed.size();
    }

    // TODO: We actually don't need to check this, if we check the content of the Sets
    public long getCountOfFsItemsToBeConsumed() {
        return getCountOfDirsToBeConsumed() + getCountOfFilesToBeConsumed();
    }

    public Set<Path> getFilesToBeConsumed() {
        return filesToBeConsumed;
    }

    public Set<Path> getDirsToBeConsumed() {
        return dirsToBeConsumed;
    }

    public Set<Path> getFilesToBeSkipped() {
        return filesToBeSkipped;
    }

    public Set<Path> getDirsToBeSkipped() {
        return dirsToBeSkipped;
    }
}
