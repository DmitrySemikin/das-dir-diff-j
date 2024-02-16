package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.PathConverter;

import java.nio.file.Path;

@Parameters(separators = "=", commandDescription = "Inspect content of the dir and print result.")
public final class SubCmdArgsCollectDirFsItems implements SubCmdArgs {

    public static final String COMMAND_NAME = "collect-fs-items-names";

    private Path dirToInspect = null;

    public Path getDirToInspect() {
        return dirToInspect;
    }

    @Parameter(description = "<Path of directory to inspect>", converter = PathConverter.class)
    public void setDirToInspect(final Path dirToInspect) {
        this.dirToInspect = dirToInspect;
    }

}
