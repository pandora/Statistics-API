package me.thomas.sapi.test;

import static me.thomas.sapi.lib.Time.*;
import static me.thomas.sapi.test.helper.AssertionTestHelper.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import me.thomas.sapi.Transaction.Transaction;
import me.thomas.sapi.statistics.Statistics;

public class StatisticsTest {

    private Statistics stats;

    @Before
    public void setupStatistics() {
        stats = new Statistics();
        stats.clear();
    }

    /**
     * Basic test case. Check that statistics added in the same sliding window
     * are properly calculated / aggregated.
     */
    @Test
    public void sameWindowIntervalTest() {
        stats = new Statistics();

        long time = nowMillis();

        stats.addStatistic(10.0, time);
        stats.addStatistic(20.0, time + 1000);
        stats.addStatistic(20.5, time + 2000);

        stats.refresh();
        assertStatisticsEquals(stats, 50.5, 16.833, 3, 20.5, 10);
    }

    /**
     * Test the adding of statistics over a sliding window that has moved.
     */
    @Test
    public void multipleWindowIntervalTest() {
        stats.setSlidingWindow(10);

        long time = nowMillis();

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
        assertStatisticsEquals(stats, 65.5, 16.375, 4, 20.5, 10);
    }
    
    /**
     * Check that stale transactions are rejected whilst valid transactions are accepted
     */
    @Test
    public void addTestStatisticTest() {
        // Reject stale transactions
        Transaction transaction = new Transaction(10.0, nowMillis() - 61000);
        assertFalse( stats.addStatistic(transaction) );
        
        // Accept transactions in the current sliding window
        transaction = new Transaction(10.0, nowMillis());
        assertTrue( stats.addStatistic(transaction) );
        
        stats.refresh();
        assertStatisticsEquals(stats, 10.0, 10.0, 1, 10.0, 10.0);  
    }
    
    /**
     * Test that stale transactions are purged on insert
     */
    @Test
    public void purgeStaleTransactionsTest() {
        long time = nowMillis();
        
        stats.addStatistic(10.0, time - 550000);
        stats.addStatistic(10.0, time -   1000);
        
        stats.setSlidingWindow(500);
        
        stats.addStatistic(20.0, time);
        assertEquals(2, stats.transactionCount());
        
        stats.refresh();
        assertStatisticsEquals(stats, 30.0, 15.0, 2, 20.0, 10.0);
    }

}
