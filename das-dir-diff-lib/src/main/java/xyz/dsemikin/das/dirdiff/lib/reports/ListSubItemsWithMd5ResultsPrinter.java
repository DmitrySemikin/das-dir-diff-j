package xyz.dsemikin.das.dirdiff.lib.reports;

import xyz.dsemikin.das.dirdiff.lib.algorithms.ListSubItemsWithMd5;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Map.Entry;

public class ListSubItemsWithMd5ResultsPrinter {
    public void print(
            final ListSubItemsWithMd5 results,
            final Path outputFilePath,
            final boolean overwrite
    ) {
        // We want to print following information:
        // * operation name: list MD5 hashes
        // * Input parameters:
        // ** root dir
        // ** exclude dirs
        // ** output file path
        // ** abort on access error
        // * Total items count
        // * Files count
        // * Dirs count
        // * Skipped dirs count and list - exclude dirs
        // * Skipped items count and list - access error
        // * Elapsed time
        final PrintStream printStream = System.out;
        printStream.println("collect-fs-items-md5 - execution results");
        printStream.println("Rood dir: " + results.getRootDir());
        printStream.println("Exclude dirs:");
        for (final Path excludeDir : results.getExcludeDirConfig()) {
            printStream.println("\t" + excludeDir);
        }
        printStream.println("Output file: " + outputFilePath);
        printStream.println("Overwrite: " + overwrite);
        printStream.println("Abort on error: " + results.getAbortOnError());
        printStream.println("Total FS Items processed: " + results.getFsItemConsumer().getConsumedFsItemsTotalCount());
        printStream.println("Files processed: " + results.getFsItemConsumer().getConsumedFilesCount());
        printStream.println("Directories processed: " + results.getFsItemConsumer().getConsumedDirsCount());
        printStream.println("Directories skipped - excluded: " + results.getSkippedDirsExcluded().size());
        for (final Path skippedDirExcluded : results.getSkippedDirsExcluded()) {
            printStream.println("\t" + skippedDirExcluded);
        }
        printStream.println("FS items skipped - access error: " + results.getSkippedDirsNoAccess().size());
        for (final Entry<Path, Exception> skippedItemNoAccess : results.getSkippedDirsNoAccess().entrySet()) {
            printStream.println("\t" + skippedItemNoAccess.getKey() + "\t-\t" + skippedItemNoAccess.getValue());
        }
        // TODO: Add method timer.getFullIntervalFormatted() -> String, which formats time as XX h XX m XX s and use it here.
        printStream.println("Elapsed time (sec): " + results.getTimer().getFullIntervalSec());
    }
}
