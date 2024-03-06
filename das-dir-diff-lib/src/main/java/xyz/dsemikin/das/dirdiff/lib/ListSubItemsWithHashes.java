package xyz.dsemikin.das.dirdiff.lib;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dsemikin.das.dirdiff.lib.utils.DasNanoTimer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ListSubItemsWithHashes {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListSubItemsWithHashes.class);

    private static final long ITEMS_PER_PROFILING_CHECKPOINT = 1000;

    public ListSubItemsWithHashes(
            final Path rootDir,
            final Path outputFilePath
    ) {
        try (final BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFilePath.toFile()))) {
            try (final Stream<Path> pathStream = Files.walk(rootDir)) {
                final DasNanoTimer timer = DasNanoTimer.start();
                class InnerCounter { public Long counter = 0L; }
                final InnerCounter itemCounter = new InnerCounter();
                pathStream.forEach(
                        path -> {
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
                );
                timer.checkpointAndLogElapsedTimeSec(LOGGER);
                LOGGER.atInfo().setMessage("").addKeyValue("Total processed items", itemCounter.counter).log();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private static void writeLineToFile(BufferedWriter outputFile, String line) {
        try {
            outputFile.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
