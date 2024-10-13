package xyz.dsemikin.das.dirdiff.lib.algorithms;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dsemikin.das.dirdiff.lib.utils.DasNanoTimer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
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

public class ListSubItemsWithHashes {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListSubItemsWithHashes.class);

    private static final long ITEMS_PER_PROFILING_CHECKPOINT = 1000;

    // TODO: these fields are results. Pack them into dedicated nested static class.
    private final List<Path> excludeDirConfig;
    private final List<Path> skippedDirsExcluded;
    private final Map<Path, Exception> skippedDirsNoAccess;

    public ListSubItemsWithHashes(
            final Path rootDir,
            final Path outputFilePath,
            final List<Path> excludeDirectories,
            final boolean overwrite,
            final boolean abortOnAccessError
    ) {
        excludeDirConfig = excludeDirectories;
        skippedDirsExcluded = new ArrayList<>();
        skippedDirsNoAccess = new HashMap<>();

        if (!overwrite && Files.exists(outputFilePath)) {
            throw new IllegalStateException("File " + outputFilePath + " already exists. Use overwrite option to force overwriting.");
        }

        try (final BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFilePath.toFile()))) {

            final DasNanoTimer timer = DasNanoTimer.start();

            final CustomFileVisitor fileVisitor = new CustomFileVisitor(rootDir, excludeDirectories, outputFile, timer, abortOnAccessError);
            // TODO: We can also use the "file options" argument of "walk directory tree" to deal with symbolic links.
            Files.walkFileTree(rootDir, fileVisitor);

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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class CustomCounter {
        public Long counter = 0L;
    }

    private static class CustomFileVisitor implements FileVisitor<Path> {

        private final Path rootDir;
        private final Set<Path> absoluteExcludeDirectoryPaths;
        private final Writer outputFile;
        private final CustomCounter itemCounter;
        private final DasNanoTimer timer;
        private final boolean abortOnAccessError;

        private final List<Path> skippedDirsExcluded;
        private final Map<Path, Exception> skippedDirsErrors;

        public CustomFileVisitor(
                final Path rootDir,
                final Collection<Path> excludeDirectories,
                final Writer outputFile,
                final DasNanoTimer timer,
                final boolean abortOnAccessError
        ) {
            skippedDirsExcluded = new ArrayList<>();
            skippedDirsErrors = new HashMap<>();
            this.itemCounter = new CustomCounter();

            this.rootDir = rootDir;
            this.outputFile = outputFile;
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

        private void processFsItem(Path path) {
            final Path relativePath = Paths.get(".").resolve(rootDir.relativize(path)); // I want to have it in the form "./dirName" with "./" in front

            final String line;
            if (Files.isDirectory(path)) {
                // 32-char empty field - to denote skipped md5 hash
                line = "d " + "                                " + " " + relativePath + "\n";
            } else if (Files.isRegularFile(path)) {
                line = generateLineForFileFsItem(path, relativePath);
            } else if (Files.isSymbolicLink(path)) {
                // 32-char empty field - to denote skipped md5 hash
                line = "l " + "                                " + " "  + relativePath + "\n";
            } else {
                throw new RuntimeException("Unsupported/unknown FS-Item kind (not one of: dir, file, link): " + path);
            }
            writeLineToFile(outputFile, line);

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

    public List<Path> getExcludeDirConfig() {
        return new ArrayList<>(excludeDirConfig);
    }

    public List<Path> getSkippedDirsExcluded() {
        return new ArrayList<>(skippedDirsExcluded);
    }

    public HashMap<Path, Exception> getSkippedDirsNoAccess() {
        return new HashMap<>(skippedDirsNoAccess);
    }

    private static String generateLineForFileFsItem(Path path, Path relativePath) {
        final String line;
        try (final InputStream inputStream = Files.newInputStream(path)) {
            line = "f " + DigestUtils.md5Hex(inputStream) + " " + relativePath + "\n";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line;
    }

    private static void writeLineToFile(final Writer outputFile, final String line) {
        try {
            outputFile.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
