package me.thomas.sapi.statistics;

import static me.thomas.sapi.lib.Time.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import me.thomas.sapi.Transaction.Transaction;

@JsonPropertyOrder( {"sum", "avg", "max", "min", "count"} )
public class Statistics {

    private static long slidingWindow = 60;

    private volatile long count;
    private volatile double sum, avg, max, min;

    private static final ConcurrentHashMap<Long, StatisticsAggregateData> amounts = new ConcurrentHashMap<>();

    private Stream<StatisticsAggregateData> fetchCurrentWindow() {
        return amounts.keySet().stream().filter(key -> (now() - key) <= slidingWindow).map(key -> amounts.get(key));
    }

    private void purgeStaleTransactions() {
        List<Long> keys = amounts.keySet().stream().filter(key -> (now() - key) > slidingWindow).collect(Collectors.toList());
        keys.forEach(key -> amounts.remove(key));
    }

    public Statistics() {
        refresh();
    }

    public void refresh() {
        Stream<StatisticsAggregateData> transactions = fetchCurrentWindow();

        if (transactions.count() > 0) {
            sum = fetchCurrentWindow().map(e -> e.sum()).mapToDouble(d -> d).sum();
            max = fetchCurrentWindow().map(e -> e.max()).mapToDouble(d -> d).max().getAsDouble();
            min = fetchCurrentWindow().map(e -> e.min()).mapToDouble(d -> d).min().getAsDouble();
            count = fetchCurrentWindow().map(e -> e.count()).mapToLong(l -> l).sum();
            avg = sum / count;
        }
    }

    public void clear() {
        amounts.clear();
    }

    public void setSlidingWindow(long seconds) {
        slidingWindow = seconds;
    }

    public synchronized boolean addStatistic(Transaction transaction) {
        return addStatistic(transaction.getAmount(), transaction.getTimestamp());
    }
    
    public boolean addStatistic(double amount, long timestamp) {
        long seconds = (timestamp / 1000);

        if ((now() - seconds) > slidingWindow)
            return false;
        
        synchronized(amounts) {
        		purgeStaleTransactions();
        	
        		StatisticsAggregateData entry = amounts.containsKey(seconds) ? amounts.get(seconds).increment(amount) : new StatisticsAggregateData(amount);
        		amounts.put(seconds, entry);
        }

        return true;
    }
    
    public int transactionCount() {
        return amounts.size();
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }

}
