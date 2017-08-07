package me.thomas.sapi.test;

import static me.thomas.sapi.test.helper.AssertionTestHelper.*;

import org.junit.Before;
import org.junit.Test;

import me.thomas.sapi.statistics.StatisticsAggregateData;

/**
 * Tests that StatisticsAggregateData aggregates data and 
 * maintains count, min and max values correctly.
 */
public class StatisticsAggregateDataTest {

    private StatisticsAggregateData sa;
    
    @Before
    public void setUp() {
        sa = new StatisticsAggregateData(10.0);
    }
    
    @Test
    public void basicConstructorTest() {
        assertStatisticsAggregateDataEquals(sa, 10.0, 10.0, 1, 10.0, 10.0);
        
        sa.increment(20.0);
        assertStatisticsAggregateDataEquals(sa, 30.0, 15.0, 2, 20.0, 10.0);
    }

}
