package xyz.dsemikin.das.dirdiff.lib.utils;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DasNanoTimer {

    private final List<Long> checkpoints;

    private DasNanoTimer() {
        checkpoints = new ArrayList<>();
        checkpoints.add(System.nanoTime());
    }

    public static DasNanoTimer start() {
        return new DasNanoTimer();
    }

    public void checkpoint() {
        checkpoints.add(System.nanoTime());
    }

    public List<Long> getCheckpoints() {
        return checkpoints;
    }

    public long getFullIntervalNanoSec() {
        if (checkpoints.size() < 2) {
            return 0;
        } else {
            return checkpoints.get(checkpoints.size() - 1) - checkpoints.get(0);
        }
    }

    public double getFullIntervalSec() {
        return 1e-9 * ((double) getFullIntervalNanoSec());
    }

    public long getLastIntervalNanoSec() {
        if (checkpoints.size() < 2) {
            return 0;
        } else {
            return checkpoints.get(checkpoints.size() - 1) - checkpoints.get(checkpoints.size() - 2);
        }
    }

    public double getLastIntervalSec() {
        return 1e-9 * ((double) getLastIntervalNanoSec());
    }

    public void checkpointAndLogElapsedTimeSec(final Logger logger) {
        checkpoint();
        logger.atInfo().setMessage("").addKeyValue("Elapsed time(sec)", getFullIntervalSec()).log();
    }

    public void checkpointAndLogLastIntervalSec(final Logger logger) {
        checkpoint();
        logger.atInfo().setMessage("").addKeyValue("Last interval (sec)", getLastIntervalSec()).log();
    }

    public double checkpointAndGetLastIntervalSec() {
        checkpoint();
        return getLastIntervalSec();
    }

}
