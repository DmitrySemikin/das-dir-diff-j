package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.PathConverter;

import java.nio.file.Path;

@Parameters(separators = "=", commandDescription = "Inspect content of the dir and print result.")
public final class CommandInspectDir implements CmdSubcommand {

    public static final String COMMAND_NAME = "inspect-dir";

    private Path dirToInspect = null;
    private boolean ignoredArg;

    public Path getDirToInspect() {
        return dirToInspect;
    }

    public boolean getIgnoredArg() {
        return ignoredArg;
    }

    @Parameter(description = "<Path of directory to inspect>", converter = PathConverter.class)
    public void setDirToInspect(final Path dirToInspect) {
        this.dirToInspect = dirToInspect;
    }

    @Parameter(names = "--ignored-arg", description = "This argument is currently ignored.")
    public void setIgnoredArg(final boolean ignoredArg) {
        this.ignoredArg = ignoredArg;
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
