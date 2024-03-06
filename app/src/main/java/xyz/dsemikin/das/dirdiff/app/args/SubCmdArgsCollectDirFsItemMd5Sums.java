package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.PathConverter;

import java.nio.file.Path;

@Parameters(separators = "=", commandDescription = "Inspect content of the dir and print result.")
public final class SubCmdArgsCollectDirFsItemMd5Sums implements SubCmdArgs {

    public static final String COMMAND_NAME = "collect-fs-items-md5";

    private Path dirToInspect = null;
    private Path outputFilePath = null;
    private boolean overwrite = false;

    public Path getDirToInspect() {
        return dirToInspect;
    }

    @Parameter(names = {"--dir"}, description = "<Path of directory to inspect>", converter = PathConverter.class)
    public void setDirToInspect(final Path dirToInspect) {
        this.dirToInspect = dirToInspect;
    }

    public Path getOutputFilePath() {
        return outputFilePath;
    }

    @Parameter(names = {"--output-file"}, description = "Path to file to be generated - text file with paths and md5 sums.", converter = PathConverter.class)
    public void setOutputFilePath(Path outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    @Parameter(names = {"--overwrite"}, description = "Overwrite existing output file, if exists.")
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }
}
