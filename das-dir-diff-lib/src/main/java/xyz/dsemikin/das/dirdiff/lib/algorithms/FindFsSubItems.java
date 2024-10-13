package xyz.dsemikin.das.dirdiff.lib.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItem;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItemDir;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItemFile;
import xyz.dsemikin.das.dirdiff.lib.fsitem.FsItemLink;
import xyz.dsemikin.das.dirdiff.lib.utils.DasNanoTimer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindFsSubItems {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindFsSubItems.class);
    public static final int ITEMS_PER_PROFILING_CHECKPOINT = 1000;

    private final Path rootDir;
    private final Map<Path, FsItem> fsItems;

    public FindFsSubItems(final Path rootDir) {
        fsItems = new HashMap<>();
        this.rootDir = rootDir;
        if (!Files.isDirectory(rootDir)) {
            throw new RuntimeException("FindFsSubItems: rootDir parameter must be path of existing directory");
        }

        try (Stream<Path> pathStream = Files.walk(rootDir)) {
            final DasNanoTimer timer = DasNanoTimer.start();
            class InnerCounter { public Long counter = 0L; }
            final InnerCounter itemCounter = new InnerCounter();
            pathStream.forEach(
                    path -> {
                        final Path relativePath = rootDir.relativize(path);
                        final FsItem fsItem;
                        if (Files.isDirectory(path)) {
                            fsItem = new FsItemDir(relativePath);
                        } else if (Files.isRegularFile(path)) {
                            fsItem = new FsItemFile(relativePath);
                        } else if (Files.isSymbolicLink(path)) {
                            fsItem = new FsItemLink(relativePath);
                        } else {
                            throw new RuntimeException("Unsupported/unknown FS-Item kind (not one of: dir, file, link): " + path);
                        }
                        fsItems.put(relativePath, fsItem);

                        itemCounter.counter++;
                        if (itemCounter.counter == ITEMS_PER_PROFILING_CHECKPOINT) {
                            itemCounter.counter = 0L;
                            final double lastIntervalSec = timer.checkpointAndGetLastIntervalSec();
                            LOGGER.atInfo()
                                    .setMessage("Processed 1000 items in {} sec with average speed {} items/sec")
                                    .addArgument(lastIntervalSec)
                                    .addArgument(((double)ITEMS_PER_PROFILING_CHECKPOINT)/ lastIntervalSec)
                                    .log();
                        }
                    }
            );
            timer.checkpointAndLogElapsedTimeSec(LOGGER);
            LOGGER.atInfo().setMessage("").addKeyValue("Total items", fsItems.size()).log();
            LOGGER.atInfo().setMessage("").addKeyValue("Average processing time (items/sec)", ((double)fsItems.size())/timer.getFullIntervalSec()).log();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getRootDir() {
        return rootDir;
    }

    public Map<Path, FsItem> getFsItems() {
        return fsItems;
    }

    public <FsItemKindType extends FsItem> List<FsItemKindType> getFsItemsOfType(Class<FsItemKindType> fsItemKindTypeClass) {
        return fsItems.values().stream()
                .filter(fsItemKindTypeClass::isInstance)
                .map(fsItemKindTypeClass::cast)
                .collect(Collectors.toList());
    }

    public List<FsItemDir> getDirs() {
        return getFsItemsOfType(FsItemDir.class);
    }

    public List<FsItemFile> getFiles() {
        return getFsItemsOfType(FsItemFile.class);
    }

    public List<FsItemLink> getLinks() {
        return getFsItemsOfType(FsItemLink.class);
    }
}
