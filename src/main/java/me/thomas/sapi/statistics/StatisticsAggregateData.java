package me.thomas.sapi.statistics;

/**
 * 
 */
public class StatisticsAggregateData {

    private long count;
    private double amount, min, max;
    
    public StatisticsAggregateData() {
        
    }

    /**
     * Constructor aggregates data by default, so calculates the sum, count min/max
     * values.
     * 
     * @param amt double amount posted to the API
     */
    public StatisticsAggregateData(double amt) {
        amount = amt;
        count++;

        setMinMax(amt);
    }

    /**
     * Increment sum by passed amount
     * 
     * @param amt
     *            double amount posted to the API
     * @return an instance of this class i.e. a StatisticsAggregateData object
     */
    public StatisticsAggregateData increment(double amt) {
        amount += amt;
        count++;
        setMinMax(amt);

        return this;
    }

    /**
     * @return accumulated sum of amounts
     */
    public double getSum() {
        return amount;
    }

    /**
     * @return current average of accumulated sum and count
     */
    public double getAvg() {
        return amount / count;
    }

    /**
     * @return the smallest passed amount to this instance
     */
    public double getMin() {
        return min;
    }

    /**
     * @return the largest passed amount to this instance
     */
    public double getMax() {
        return max;
    }

    /**
     * @return the number of passed amounts to this instance
     */
    public long getCount() {
        return count;
    }

    /**
     * Private method that maintains min/max values against new incoming amounts
     * 
     * @param amt
     *            double amount posted to the API
     */
    private void setMinMax(double amt) {
        min = (min > 0) && (min <= amt) ? min : amt;
        max = amt >= max ? amt : max;
    }
}
