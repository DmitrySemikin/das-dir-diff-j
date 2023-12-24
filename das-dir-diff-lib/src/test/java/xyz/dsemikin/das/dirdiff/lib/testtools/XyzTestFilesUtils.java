package xyz.dsemikin.das.dirdiff.lib.testtools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class XyzTestFilesUtils {
    public static void deleteDirRecursively(final Path path) {
        if (!Files.isDirectory(path)) {
            throw new RuntimeException("path argument is expected to point to directory.");
        }
        try (final Stream<Path> pathStream = Files.walk(path)) {
            pathStream.sorted(Comparator.reverseOrder()).forEach(
                    childPath -> {
                        try {
                            Files.delete(childPath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
