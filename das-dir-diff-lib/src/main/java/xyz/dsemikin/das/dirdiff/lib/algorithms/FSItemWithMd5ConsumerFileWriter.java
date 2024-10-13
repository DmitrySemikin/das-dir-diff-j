package xyz.dsemikin.das.dirdiff.lib.algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FSItemWithMd5ConsumerFileWriter extends FSItemWithMd5ConsumerWithCounterAbstract {

    private final BufferedWriter outputFile;

    public FSItemWithMd5ConsumerFileWriter(
            final Path outputFilePath,
            final boolean overwrite
    ) {
        if (!overwrite && Files.exists(outputFilePath)) {
            throw new IllegalStateException("File " + outputFilePath + " already exists. Use overwrite option to force overwriting.");
        }

        try {
            outputFile = new BufferedWriter(new FileWriter(outputFilePath.toFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        outputFile.close();
    }

    @Override
    public void consumeFsItemWithMd5Impl(final FSItemWithMd5 fsItem) {
        // 32-char empty field - to denote skipped md5 hash
        final String emptyMd5Field = "                                ";
        final String line = switch (fsItem.getKind()) {
            case FS_ITEM_DIR -> "d " + emptyMd5Field + " " + fsItem.getRelativePath() + "\n";
            case FS_ITEM_FILE -> "f " + fsItem.getMd5() + " " + fsItem.getRelativePath() + "\n";
            case FS_ITEM_LINK -> "l " + emptyMd5Field + " " + fsItem.getRelativePath() + "\n";
            case null, default ->
                    throw new RuntimeException("Unsupported/unknown FS-Item kind (not one of: dir, file, link): " + fsItem.getAbsolutePath());
        };
        writeLineToFile(outputFile, line);
    }

    private static void writeLineToFile(final Writer outputFile, final String line) {
        try {
            outputFile.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
