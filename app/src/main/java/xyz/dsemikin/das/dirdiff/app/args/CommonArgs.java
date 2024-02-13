package xyz.dsemikin.das.dirdiff.app.args;

import com.beust.jcommander.Parameter;

public class CommonArgs {
    private boolean verbose;
    private boolean help;

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isHelp() {
        return help;
    }

    @Parameter(names = "--verbose", description = "Ignored for now")
    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    @Parameter(names = "--help", description = "Print help message", help = true)
    public void setHelp(final boolean help) {
        this.help = help;
    }
}
