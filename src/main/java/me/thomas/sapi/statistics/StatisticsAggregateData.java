package me.thomas.sapi.statistics;

public class StatisticsAggregateData {

    private long count;
    private double amount;
    private double min, max;

    public StatisticsAggregateData(double amt) {
        amount = amt;
        count++;

        setMinMax(amt);
    }

    public StatisticsAggregateData increment(double amt) {
        amount += amt;
        count++;		
        	setMinMax(amt);

        return this;
    }

    public double sum() {
        return amount;
    }

    public double avg() {
        System.out.println(amount + " : " + count);
        return amount / count;
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public long count() {
        return count;
    }

    private void setMinMax(double amt) {
        min = (min > 0) && (min <= amt) ? min : amt;
        max = amt >= max ? amt : max;
    }
}
