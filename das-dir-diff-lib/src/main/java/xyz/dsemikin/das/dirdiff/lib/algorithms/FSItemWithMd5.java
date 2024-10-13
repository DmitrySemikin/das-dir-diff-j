package xyz.dsemikin.das.dirdiff.lib.algorithms;

import org.apache.commons.codec.digest.DigestUtils;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItemKind;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FSItemWithMd5 {

    private final Path rootDir;
    private final Path relativePath;
    private final Path absolutePath;
    private final FsItemKind kind;
    final private String md5;

    private static final String EMPTY_MD5_FIELD = "                                ";


    public FSItemWithMd5(final Path path, final Path rootDir) {

        this.rootDir = rootDir;
        absolutePath = rootDir.resolve(path).normalize().toAbsolutePath();
        if (!absolutePath.startsWith(rootDir)) {
            throw new RuntimeException("Given path must be subdirectory of the given rootDir. path: " + path + "; rootDir: " + rootDir);
        }
        relativePath = rootDir.relativize(absolutePath);

        if (Files.isRegularFile(absolutePath)) {
            kind = FsItemKind.FS_ITEM_FILE;
            try (final InputStream inputStream = Files.newInputStream(path)) {
                md5 = DigestUtils.md5Hex(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (Files.isDirectory(absolutePath)) {
            kind = FsItemKind.FS_ITEM_DIR;
            md5 = EMPTY_MD5_FIELD;
        } else if (Files.isSymbolicLink(absolutePath)) {
            kind = FsItemKind.FS_ITEM_LINK;
            md5 = EMPTY_MD5_FIELD;
        } else {
            throw new RuntimeException("Unknown kind of FS Item. Path: " + absolutePath);
        }
    }

    public Path getRootDir() {
        return rootDir;
    }

    public Path getRelativePath() {
        return relativePath;
    }

    public Path getAbsolutePath() {
        return absolutePath;
    }

    public FsItemKind getKind() {
        return kind;
    }

    public String getMd5() {
        return md5;
    }
}
