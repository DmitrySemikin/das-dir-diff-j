package xyz.dsemikin.das.dirdiff.lib.testtools;

import xyz.dsemikin.das.dirdiff.lib.algorithms.FSItemWithMd5;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItemKind;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class SampleFsItemWithMd5InputProvider implements AutoCloseable{

    private static final String SAMPLE_DIR_NAME = "sample_directory";
    private static final String SAMPLE_FILE_NAME = "sample_file.txt";
    private static final String SAMPLE_FILE_CONTENT = "First Line\nOther Line\nBla-bla-bla.";
    private static final String SAMPLE_FILE_CONTENT_MD5 = "5d499091ce9452ed393d017c807f6fe4";
    private static final String SAMPLE_DIR_MD5 = FSItemWithMd5.EMPTY_MD5_FIELD;


    private final SampleRootDirProvider rootDirProvider;
    private Optional<Path> maybeLocalPath;
    private final FsItemKind fsItemKind;


    public static SampleFsItemWithMd5InputProvider file() {
        return new SampleFsItemWithMd5InputProvider(FsItemKind.FS_ITEM_FILE);
    }

    public static SampleFsItemWithMd5InputProvider directory() {
        return new SampleFsItemWithMd5InputProvider(FsItemKind.FS_ITEM_DIR);
    }

    private SampleFsItemWithMd5InputProvider(final FsItemKind fsItemKind) {
        this.fsItemKind = fsItemKind;
        if (fsItemKind.equals(FsItemKind.FS_ITEM_LINK)) {
            throw new RuntimeException("Links are not supported yet.");
        }
        rootDirProvider = new SampleRootDirProvider();
        final Path rootDir = rootDirProvider.getRootDir();
        final Path localPath = switch (fsItemKind) {
            case FS_ITEM_FILE -> createSampleFile(rootDir);
            case FS_ITEM_DIR -> createSampleDirectory(rootDir);
            default -> throw new RuntimeException("Unexpected FSItem kind: " + fsItemKind);
        };
        maybeLocalPath = Optional.of(localPath);
    }

    private Path createSampleDirectory(final Path rootDir) {
        final Path localDirPath = Paths.get(SAMPLE_DIR_NAME);
        final Path dirPath = rootDir.resolve(localDirPath);
        try {
            Files.createDirectory(dirPath); // we expect, that root dir already exists
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return localDirPath;
    }

    private Path createSampleFile(final Path rootDir) {
        final Path localFilePath = Paths.get(SAMPLE_FILE_NAME);
        final Path filePath = rootDir.resolve(localFilePath);
        try {
            Files.writeString(filePath, SAMPLE_FILE_CONTENT, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return localFilePath;
    }

    public Path getRootDir() {
        return rootDirProvider.getRootDir();
    }

    public Path getLocalPath() {
        //noinspection OptionalGetWithoutIsPresent
        return maybeLocalPath.get(); // we want it to throw, if it is empty
    }

    @Override
    public void close() {
        rootDirProvider.close(); // also removes content
        maybeLocalPath = Optional.empty();
    }

    public String getExpectedMd5() {
        return switch (fsItemKind) {
            case FS_ITEM_FILE -> SAMPLE_FILE_CONTENT_MD5;
            case FS_ITEM_DIR -> SAMPLE_DIR_MD5;
            case null, default -> throw new RuntimeException("Unexpected item kind: " + fsItemKind);
        };
    }

    public FsItemKind getKind() {
        return fsItemKind;
    }
}
