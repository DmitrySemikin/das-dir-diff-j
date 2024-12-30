package xyz.dsemikin.das.dirdiff.lib.testtools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestFilesAndDirsProvider {

    private final Path rootDir;

    public TestFilesAndDirsProvider() {
        rootDir = generateTestRootDir();
    }

    private static Path generateTestRootDir() {
        try {
            return Files.createTempDirectory("das-dir-diff-lib-test");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getRootDir() {
        return rootDir;
    }

    public TestFilesAndDirsInfo generateTestFilesAndDirs() {

        final List<Path> excludeDirsConfig = new ArrayList<>();
        final Set<Path> dirsToBeSkipped = new HashSet<>();
        final Set<Path> filesToBeSkipped = new HashSet<>();

        // The map contains file's path and it's content.
        final Map<Path, String> filesToConsumeWithContent = new HashMap<>();
        filesToConsumeWithContent.put(Paths.get("a.txt"), "This is first file.");
        filesToConsumeWithContent.put(Paths.get("b.txt"), "This is second file.");
        filesToConsumeWithContent.put(Paths.get("c.txt"), "This is other file.");
        filesToConsumeWithContent.put(Paths.get("dir1/aaa.txt"), "ABC.");
        filesToConsumeWithContent.put(Paths.get("dir1/bbb.txt"), "CDE.");
        filesToConsumeWithContent.put(Paths.get("dir1/ccc.txt"), "More files.");
        filesToConsumeWithContent.put(Paths.get("dir2/dir3/one.txt"), "Bla Bla.");
        filesToConsumeWithContent.put(Paths.get("dir2/dir3/two.txt"), "FGC.");
        filesToConsumeWithContent.put(Paths.get("dir2/dir3/three.txt"), "Even more files here.");
        filesToConsumeWithContent.put(Paths.get("dir2/dir3/four.txt"), "Again.");

        final Set<Path> filesToBeConsumed = new HashSet<>(filesToConsumeWithContent.keySet());

        final Set<Path> dirsToBeConsumed = new HashSet<>();
        dirsToBeConsumed.add(Paths.get(""));
        dirsToBeConsumed.add(Paths.get("dir1"));
        dirsToBeConsumed.add(Paths.get("dir2"));
        dirsToBeConsumed.add(Paths.get("dir2/dir3"));

        final List<Path> emptyDirsToProcess = new ArrayList<>();
        emptyDirsToProcess.add(Paths.get("emptyDir1/emptyDir2"));
        emptyDirsToProcess.add(Paths.get("empty5/empty6/empty7"));

        dirsToBeConsumed.add(Paths.get("emptyDir1"));
        dirsToBeConsumed.add(Paths.get("emptyDir1/emptyDir2"));
        dirsToBeConsumed.add(Paths.get("empty5"));
        dirsToBeConsumed.add(Paths.get("empty5/empty6"));
        dirsToBeConsumed.add(Paths.get("empty5/empty6/empty7"));


        final Map<Path, String> filesToSkipWithContent = new HashMap<>();
        final List<Path> emptyDirsToSkip = new ArrayList<>();

        excludeDirsConfig.add(Paths.get("dir4"));
        filesToSkipWithContent.put(Paths.get("dir4/dir44/abc.txt"), "abc abc");
        filesToSkipWithContent.put(Paths.get("dir4/dir55/xyz.txt"), "xyz");

        dirsToBeSkipped.add(Paths.get("dir4"));
        dirsToBeSkipped.add(Paths.get("dir4/dir44"));
        dirsToBeSkipped.add(Paths.get("dir4/dir55"));

        excludeDirsConfig.add(Paths.get("dir2/dir6_skip"));
        filesToSkipWithContent.put(Paths.get("dir2/dir6_skip/skip_file_1.txt"), "asdf qwer");
        filesToSkipWithContent.put(Paths.get("dir2/dir6_skip/dir7/skip_file_2.txt"), "asdf qwer");

        emptyDirsToSkip.add(Paths.get("dir4/empty_dir8"));
        emptyDirsToSkip.add(Paths.get("dir2/dir6_skip/empty_dir9"));
        dirsToBeSkipped.addAll(emptyDirsToSkip);


        final Map<Path, String> filesToCreate = new HashMap<>(filesToConsumeWithContent);
        filesToCreate.putAll(filesToSkipWithContent);

        final List<Path> emptyDirsToCreate = new ArrayList<>(emptyDirsToProcess);
        emptyDirsToCreate.addAll(emptyDirsToSkip);

        CreateSampleRootDir.createTestDirContent(rootDir, filesToCreate, emptyDirsToCreate);

        return new TestFilesAndDirsInfo(
            excludeDirsConfig,
            rootDir,
            dirsToBeConsumed,
            dirsToBeSkipped,
            filesToBeConsumed,
            filesToBeSkipped
        );
    }
}
