package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.PathConverter;

import java.nio.file.Path;

@Parameters(commandNames = "inspect-dir", separators = "=", commandDescription = "Inspect content of the dir and print result.")
public class CommandInspectDir {
    private Path dirToInspect = null;

    public Path getDirToInspect() {
        return dirToInspect;
    }

    @Parameter(description = "Path of directory to inspect.", converter = PathConverter.class)
    public void setDirToInspect(final Path dirToInspect) {
        this.dirToInspect = dirToInspect;
    }
}
