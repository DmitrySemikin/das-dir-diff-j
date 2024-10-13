package xyz.dsemikin.das.dirdiff.lib.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dsemikin.das.dirdiff.lib.utils.DasNanoTimer;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListSubItemsWithMd5 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListSubItemsWithMd5.class);

    private static final long ITEMS_PER_PROFILING_CHECKPOINT = 1000;

    // TODO: these fields are results. Pack them into dedicated nested static class.
    private final List<Path> excludeDirConfig;
    private final FSItemWithMd5Consumer fsItemConsumer;
    private final List<Path> skippedDirsExcluded;
    private final Map<Path, Exception> skippedDirsNoAccess;

    public ListSubItemsWithMd5(
            final Path rootDir,
            final List<Path> excludeDirectories,
            final boolean abortOnAccessError,
            final FSItemWithMd5Consumer fsItemConsumer
    ) {
        excludeDirConfig = excludeDirectories;
        this.fsItemConsumer = fsItemConsumer;
        skippedDirsExcluded = new ArrayList<>();
        skippedDirsNoAccess = new HashMap<>();


        final DasNanoTimer timer = DasNanoTimer.start();

        final CustomFileVisitor fileVisitor = new CustomFileVisitor(rootDir, excludeDirectories, fsItemConsumer, timer, abortOnAccessError);
        // TODO: We can also use the "file options" argument of "walk directory tree" to deal with symbolic links.
        try {
            Files.walkFileTree(rootDir, fileVisitor);
        } catch (IOException e) {
            // TODO: Think - do we actually want to do explicitly in case of error???
            throw new RuntimeException(e);
        }

        skippedDirsExcluded.clear();
        skippedDirsExcluded.addAll(fileVisitor.getSkippedDirsExcluded());

        skippedDirsNoAccess.clear();
        skippedDirsNoAccess.putAll(fileVisitor.getSkippedDirsErrors());

        timer.checkpointAndLogElapsedTimeSec(LOGGER);
        LOGGER.atInfo().setMessage("").addKeyValue("Total processed items", fileVisitor.getItemCount()).log();

        // TODO: put this into report and remove from here:
        LOGGER.atInfo().setMessage("Directories skipped because of access error:").log();
        for (final Map.Entry<Path, Exception> entry : skippedDirsNoAccess.entrySet()) {
            final Path path = entry.getKey();
            final Exception exception = entry.getValue();
            LOGGER.atInfo().setMessage(path.toString()).log();
            LOGGER.atInfo().setMessage(exception.toString()).log();
        }

    }

    private static class CustomCounter {
        public Long counter = 0L;
    }

    private static class CustomFileVisitor implements FileVisitor<Path> {

        private final Path rootDir;
        private final Set<Path> absoluteExcludeDirectoryPaths;
        private final FSItemWithMd5Consumer fsItemConsumer;
        private final CustomCounter itemCounter;
        private final DasNanoTimer timer;
        private final boolean abortOnAccessError;

        private final List<Path> skippedDirsExcluded;
        private final Map<Path, Exception> skippedDirsErrors;

        public CustomFileVisitor(
                final Path rootDir,
                final Collection<Path> excludeDirectories,
                final FSItemWithMd5Consumer fsItemConsumer,
                final DasNanoTimer timer,
                final boolean abortOnAccessError
        ) {
            skippedDirsExcluded = new ArrayList<>();
            skippedDirsErrors = new HashMap<>();
            this.itemCounter = new CustomCounter();

            this.rootDir = rootDir;
            this.fsItemConsumer = fsItemConsumer;
            this.timer = timer;
            this.abortOnAccessError = abortOnAccessError;
            absoluteExcludeDirectoryPaths = excludeDirectories.stream()
                    .map(p -> rootDir.resolve(p).toAbsolutePath().normalize())
                    .collect(Collectors.toSet());
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path path, final BasicFileAttributes attrs) {
            final Path relativePath = Paths.get(".").resolve(rootDir.relativize(path));
            final Path absoluteNormalizedPath = path.toAbsolutePath().normalize();
            if (absoluteExcludeDirectoryPaths.contains(absoluteNormalizedPath)) {
                skippedDirsExcluded.add(relativePath); // we check absolute path, but store relative path
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(final Path path, final IOException exception) {
            if (abortOnAccessError) {
                throw new RuntimeException(exception);
            }
            skippedDirsErrors.put(path, exception);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(
                final Path path,
                final BasicFileAttributes attrs
        ) {
            processFsItem(path);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(final Path path, final IOException exc) {
            processFsItem(path);
            return FileVisitResult.CONTINUE;
        }

        private void processFsItem(final Path path) {

            // TODO: arguments for FSItem
            fsItemConsumer.consumeFsItemWithMd5(new FSItemWithMd5(path, rootDir));

            itemCounter.counter++;
            if (itemCounter.counter % ITEMS_PER_PROFILING_CHECKPOINT == 0) {
                final double lastIntervalSec = timer.checkpointAndGetLastIntervalSec();
                LOGGER.atInfo()
                        .setMessage("Processed {} items in {} sec with average speed {} items/sec")
                        .addArgument(itemCounter.counter)
                        .addArgument(lastIntervalSec)
                        .addArgument(((double)ITEMS_PER_PROFILING_CHECKPOINT)/ lastIntervalSec)
                        .log();
            }
        }

        public List<Path> getSkippedDirsExcluded() {
            return new ArrayList<>(skippedDirsExcluded);
        }

        public Map<Path, Exception> getSkippedDirsErrors() {
            return new HashMap<>(skippedDirsErrors);
        }

        public Long getItemCount() {
            return itemCounter.counter;
        }
    }

    public FSItemWithMd5Consumer getFsItemConsumer() {
        return fsItemConsumer;
    }

    public List<Path> getExcludeDirConfig() {
        return new ArrayList<>(excludeDirConfig);
    }

    public List<Path> getSkippedDirsExcluded() {
        return new ArrayList<>(skippedDirsExcluded);
    }

    public HashMap<Path, Exception> getSkippedDirsNoAccess() {
        return new HashMap<>(skippedDirsNoAccess);
    }



}
