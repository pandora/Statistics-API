package me.thomas.sapi.test;

import static me.thomas.sapi.lib.Time.nowMillis;
import static me.thomas.sapi.test.helper.AssertionTestHelper.*;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;

import me.thomas.sapi.statistics.Statistics;

/**
 * Run tests concurrently with 4 threads to ensure that in-memory mutating
 * operations are correctly synchronized
 */
@RunWith(ConcurrentTestRunner.class)
public class StatisticsMultiThreadedTest {

    private static final int NO_OF_THREADS = 4;

    private Statistics stats;

    @Before
    public void setupStatistics() {
        stats = new Statistics();
        stats.clear();
    }

    @After
    public void multipleWindowIntervalTest() {
        stats.refresh();
        assertStatisticsEquals(stats, NO_OF_THREADS * 65.5, 16.375, NO_OF_THREADS * 4, 20.5, 10);
    }
    
    @Test
    public void multipleWindowIntervalThreadedTest() {
        stats.setSlidingWindow(10);

        long time = nowMillis();

        // Add statistics into the current time window; they should all be added
        stats.addStatistic(15.0, time - 1500);
        stats.addStatistic(10.0, time - 3000);
        stats.addStatistic(20.0, time - 4000);
        stats.addStatistic(20.5, time - 5000);

        //assertEquals(4, stats.transactionCount());

        // Shorten sliding window to force eviction of three transactions
        stats.setSlidingWindow(2);

        /*
         * Add first item in the new window. After adding the statistic, the stale
         * transaction cleanup should leave one transaction from the previous window
         */
        stats.addStatistic(10.0, time);

        stats.addStatistic(20.0, time);
        stats.addStatistic(20.5, time);

        // Older than time window, so should be ignored completely
        stats.addStatistic(150, time - 50000);
    }

}
