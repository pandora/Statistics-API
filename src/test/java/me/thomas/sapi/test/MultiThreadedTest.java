package me.thomas.sapi.test;

import static me.thomas.sapi.test.AssertionHelper.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;

import me.thomas.sapi.statistics.Statistics;

@RunWith(ConcurrentTestRunner.class)
public class MultiThreadedTest {

    private Statistics stats;

    @Before
    public void setupStatistics() {
        stats = new Statistics();
        stats.clear();
    }
    
    @After
    public void sameWindowIntervalTest() {
    		stats.refresh();
        statisticsEquals(stats, 4*50.5, 16.833, 4*3, 20.5, 10);
    }

    @Test
    public void sameWindowIntervalRun() {
        stats = new Statistics();

        long time = System.currentTimeMillis();

        stats.addStatistic(10.0, time);
        stats.addStatistic(20.0, time +1);
        stats.addStatistic(20.5, time +2);
    }

}
