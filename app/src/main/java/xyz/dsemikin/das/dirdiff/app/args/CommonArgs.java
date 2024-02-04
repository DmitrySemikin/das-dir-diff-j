package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.Parameter;

public class CommonArgs {
    private boolean verbose;

    public boolean isVerbose() {
        return verbose;
    }

    @Parameter(names = "--verbose", description = "Ignored for now")
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
