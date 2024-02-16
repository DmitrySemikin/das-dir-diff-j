package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SuppressWarnings("SequencedCollectionMethodCanBeUsed")
@Parameters(separators = "=", commandDescription = "Compare content of two directories by comparing item names and kinds.")
public final class SubCmdArgsCompareDirsFsItemsByName implements SubCmdArgs {

    public static final String COMMAND_NAME = "compare-dirs-fs-items-by-name";

    private List<String> dirs;


    public Path getDir1() {
        return Paths.get(dirs.get(0));
    }

    public Path getDir2() {
        return Paths.get(dirs.get(1));
    }

    @Parameter(description = "<dir1> <dir2>", arity = 2)
    public void setDirs(final List<String> dirs) {
        // Notice: this method is called by jCommander multiple times with different lists, as arguments are being parsed.
        this.dirs = dirs;
    }

}
