package me.thomas.sapi.test;

import static org.junit.Assert.*;

import me.thomas.sapi.statistics.Statistics;

public class AssertionHelper {

    public static void statisticsEquals(Statistics stats, double sum, double avg, long count, double max, double min) {
        assertEquals(sum, stats.getSum(), 0);
        assertEquals(avg, stats.getAvg(), 0.001);
        assertEquals(count, stats.getCount());

        assertEquals(max, stats.getMax(), 0);
        assertEquals(min, stats.getMin(), 0);
    }

}
