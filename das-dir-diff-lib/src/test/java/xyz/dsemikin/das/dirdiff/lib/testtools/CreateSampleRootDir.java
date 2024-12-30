package xyz.dsemikin.das.dirdiff.lib.testtools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSampleRootDir {
    public static Path createSampleRootDir() {
        try {
            final Path tempDirectory = Files.createTempDirectory("das-dir-diff-lib-test");
            createTestDirContent(tempDirectory, filesToCreate(), emptyDirsToCreate());
            return tempDirectory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTestDirContent(
            final Path rootDirectory,
            final Map<Path, String> filesToCreate,
            final List<Path> emptyDirsToCreate
    ) {
        try {
            for (final Map.Entry<Path, String> filePathAndContent : filesToCreate.entrySet()) {
                final Path path = rootDirectory.resolve(filePathAndContent.getKey());
                final String content = filePathAndContent.getValue();
                Files.createDirectories(path.getParent());
                Files.writeString(path, content, StandardOpenOption.CREATE_NEW);
            }
            for (final Path path : emptyDirsToCreate) {
                Files.createDirectories(rootDirectory.resolve(path));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Path, String> filesToCreate() {
        // The map contains file's path and it's content.
        final Map<Path, String> filesToCreate = new HashMap<>();
        filesToCreate.put(Paths.get("./a.txt"), "This is first file.");
        filesToCreate.put(Paths.get("./b.txt"), "This is second file.");
        filesToCreate.put(Paths.get("./c.txt"), "This is other file.");
        filesToCreate.put(Paths.get("./dir1/aaa.txt"), "ABC.");
        filesToCreate.put(Paths.get("./dir1/bbb.txt"), "CDE.");
        filesToCreate.put(Paths.get("./dir1/ccc.txt"), "More files.");
        filesToCreate.put(Paths.get("./dir2/dir3/one.txt"), "Bla Bla.");
        filesToCreate.put(Paths.get("./dir2/dir3/two.txt"), "FGC.");
        filesToCreate.put(Paths.get("./dir2/dir3/three.txt"), "Even more files here.");
        filesToCreate.put(Paths.get("./dir2/dir3/four.txt"), "Again.");
        return filesToCreate;
    }

    private static List<Path> emptyDirsToCreate() {
        final List<Path> emptyDirsToCreate = new ArrayList<>();
        emptyDirsToCreate.add(Paths.get("./emptyDir1/emptyDir2"));
        emptyDirsToCreate.add(Paths.get("./empty5/empty6/empty7"));
        return emptyDirsToCreate;
    }
}
