package me.thomas.sapi.Transaction;

/**
 * A Transaction object that is used to deserialize the JSON data in the Request body.
 */
public class Transaction {

    private double amount;
    private long timestamp;

    public Transaction() {
        
    }
    
    public Transaction(double amt, long time) {
        amount = amt;
        timestamp = time;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setAmount(double amt) {
        amount = amt;
    }

    public void setTimestamp(long time) {
        timestamp = time;
    }

}
