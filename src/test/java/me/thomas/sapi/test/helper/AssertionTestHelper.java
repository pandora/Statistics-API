package me.thomas.sapi.test.helper;

import static org.junit.Assert.*;

import me.thomas.sapi.statistics.Statistics;
import me.thomas.sapi.statistics.StatisticsAggregateData;

/**
 * A test helper that contains re-usable assertion methods.
 */
public class AssertionTestHelper {

    /**
     * Asserts that a Statistics object is correctly refreshed.
     * 
     * @param stats a Statistics instance
     * @param sum the sum amount to compare against
     * @param avg the average amount to compare against
     * @param count the count value to compare against
     * @param max the max amount to compare against
     * @param min the min amount to compare against
     */
    public static void assertStatisticsEquals(Statistics stats, double sum, double avg, long count, double max, double min) {
        assertEquals(sum,   stats.getSum(), 0);
        assertEquals(avg,   stats.getAvg(), 0.001);
        assertEquals(count, stats.getCount());

        assertEquals(max,   stats.getMax(), 0);
        assertEquals(min,   stats.getMin(), 0);
    }
    
    /**
     * Asserts that a StatisticsAggregateData object is correctly instantiated and incremented.
     * 
     * @param sa a StatisticsAggregateData instance
     * @param sum the sum amount to compare against
     * @param avg the average amount to compare against
     * @param count the count value to compare against
     * @param max the max amount to compare against
     * @param min the min amount to compare against
     */
    public static void assertStatisticsAggregateDataEquals(StatisticsAggregateData sa, double sum, double avg, long count, double max, double min) {
        assertEquals(sum,   sa.getSum(), 0);
        assertEquals(avg,   sa.getAvg(), 0.001);
        assertEquals(count, sa.getCount());

        assertEquals(max,   sa.getMax(), 0);
        assertEquals(min,   sa.getMin(), 0);
    }

}
