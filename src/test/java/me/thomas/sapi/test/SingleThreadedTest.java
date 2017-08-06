package me.thomas.sapi.test;

import static me.thomas.sapi.test.AssertionHelper.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import me.thomas.sapi.statistics.Statistics;

public class SingleThreadedTest {

    private Statistics stats;

    @Before
    public void setupStatistics() {
        stats = new Statistics();
        stats.clear();
    }

    @Test
    public void sameWindowIntervalTest() {
        stats = new Statistics();

        long time = System.currentTimeMillis();

        stats.addStatistic(10.0, time);
        stats.addStatistic(20.0, time + 1000);
        stats.addStatistic(20.5, time + 2000);

        stats.refresh();
        statisticsEquals(stats, 50.5, 16.833, 3, 20.5, 10);
    }

    @Test
    public void multipleWindowIntervalTest() {
        stats.setSlidingWindow(10);

        long time = System.currentTimeMillis();

        // Add statistics into the current time window; they should all be added
        stats.addStatistic(15.0, time - 1500);
        stats.addStatistic(10.0, time - 3000);
        stats.addStatistic(20.0, time - 4000);
        stats.addStatistic(20.5, time - 5000);

        assertEquals(4, stats.transactionCount());

        // Shorten sliding window to force eviction of three transactions
        stats.setSlidingWindow(2);

        /*
         * Add first item in the new window. After adding the statistic, the stale
         * transaction cleanup should leave one transaction from the previous window
         */
        stats.addStatistic(10.0, time);
        assertEquals(2, stats.transactionCount());

        stats.addStatistic(20.0, time);
        stats.addStatistic(20.5, time);

        // Older than time window, so should be ignored completely
        stats.addStatistic(150, time - 50000);

        stats.refresh();
        statisticsEquals(stats, 65.5, 16.375, 4, 20.5, 10);
    }

}
